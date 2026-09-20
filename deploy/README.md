# Backend deployment

The backend is a Ktor/Netty service running on a small DigitalOcean droplet. This
directory contains the templated systemd unit that supervises it, one instance per
environment.

## Why this exists

Before this, the jar was copied to the droplet by CI and then started by hand over
SSH, and the process had to be manually restarted every 10-20 days because it
gradually consumed the droplet's RAM until the kernel OOM killer ended it.

Nothing in the application was leaking. The cause was an unconstrained JVM:

| Region | Default when unset | Effect on a 1 GB droplet |
| --- | --- | --- |
| Heap | ¼ of total RAM | ~256 MB, silently |
| Direct memory | equal to max heap | another ~256 MB on top |
| Metaspace | unbounded | grows with loaded classes |
| Thread stacks | 1 MB each | `Dispatchers.IO` alone allows 64 threads |

Heap and direct memory are each derived from total RAM, so together they
oversubscribe the box. The creep is gradual rather than immediate because the heap
expands toward its maximum over time and the JVM does not return that memory to the
OS — which is why it took a week or two to surface each time.

The reason it became a *manual* chore is that when the kernel OOM killer is what
acts, nothing restarts the service.

## What the unit does

**Caps every region explicitly** rather than letting it be inferred from total RAM:
heap via `MaxRAMPercentage`, off-heap via `MaxDirectMemorySize`, plus
`MaxMetaspaceSize`. This is the part that actually fixes the problem — a hard
ceiling holds regardless of how eagerly any given allocator returns memory.

**Makes recovery automatic.** `Restart=always` means a crash or an OOM brings the
service straight back. `MemoryMax` sits below total RAM so *systemd* stops and
restarts the service, rather than the kernel OOM killer picking a victim and leaving
nothing running. `ExitOnOutOfMemoryError` ensures heap exhaustion exits promptly
instead of thrashing.

There is deliberately **no `MemoryHigh`**. It throttles the cgroup and forces
reclaim, but a JVM's footprint is almost entirely anonymous pages. Under sustained
reclaim pressure the service would stall rather than fail cleanly and restart.

`UseSerialGC` is set for determinism rather than effect: JVM ergonomics already
selects SerialGC below 2 CPUs and ~1792 MB, so on the current droplet this only
guarantees the choice survives a resize to a larger droplet.

**Survives reboots**, via `WantedBy=multi-user.target`.

## Two environments on one droplet

The unit is a systemd *template* — `ccc-backend@.service`, where `%i` is the
instance name. One file, two independent instances:

```sh
systemctl start ccc-backend@prod    # :8080
systemctl start ccc-backend@test    # :8081
```

Each instance gets its own working directory (`/opt/ccc/prod`, `/opt/ccc/test`) and
therefore its own SQLite file — `application_database.sqlite.db` is created relative
to the working directory, so two instances sharing one directory would corrupt a
single database.

| | prod | test |
| --- | --- | --- |
| Port | 8080 | 8081 |
| Syncs from the premium API | yes | **no** |
| `MemoryMax` | 560 MB | 240 MB |
| `MemorySwapMax` | **0** | 768 MB |
| Max heap | ~224 MB | ~108 MB |
| JIT | full tiered | C1 only |

### How both fit in 1 GB

A Ktor/Netty/Koin/SQLDelight JVM has a non-heap floor of roughly 170-220 MB —
metaspace, code cache, thread stacks, GC structures — before any heap at all, and
that cost is paid per instance. Three things make the second instance affordable:

1. **Swap.** DO droplets ship without any. A 2 GB swapfile costs disk, not RAM.
2. **`MemorySwapMax` asymmetry.** prod is pinned out of swap (`0`) so its GC pauses
   stay predictable, while the mostly-idle test instance is allowed to page out.
   This is the part plain "add swap" would not give you.
3. **test does not sync.** No premium API calls, no periodic writes, so it sits idle
   and its pages are good swap candidates. It also keeps the test environment off
   the premium API quota entirely.

`vm.swappiness` is set to 10 so swap acts as a safety valve rather than routine
behaviour.

The ceilings total 800 MB against ~981 MB of `MemTotal`, leaving ~180 MB for the OS.

> A JVM whose heap has been swapped out has slow GC pauses when it next wakes. That
> is acceptable for a test instance and is exactly why prod is pinned out of swap.
> If the test environment ever needs to behave like production, resize the droplet
> to 2 GB rather than relaxing `MemorySwapMax` on prod.

### Where per-instance settings live

| What | Where |
| --- | --- |
| Runtime env (`SERVER_PORT`, `SYNC_ENABLED`, `JVM_OPTS`) | `/etc/ccc-backend/<instance>.env` |
| Cgroup limits (`MemoryMax`, `MemorySwapMax`) | `/etc/systemd/system/ccc-backend@<instance>.service.d/memory.conf` |

The split is forced by systemd: `EnvironmentFile` values reach the *process*, and
`${VAR}` is only expanded in `ExecStart` — never in directives like `MemoryMax`. So
anything that is a unit setting rather than a process setting has to be a drop-in.

Both are generated by `setup-server.sh`. Edit the script and re-run it rather than
editing them in place.

## One-time setup

Copy this directory to the droplet and run the script as root:

```sh
scp -r deploy/ user@droplet:/tmp/ccc-deploy
ssh user@droplet
sudo MIGRATE_FROM=/path/to/current/deploy/dir /tmp/ccc-deploy/setup-server.sh prod
```

`MIGRATE_FROM` copies the existing database and newest `app-<version>.jar` into
`/opt/ccc/prod`. It **copies** rather than moves, so the originals remain as a
rollback path. Drop it once the migration has happened.

Add the test instance once the backend build reads `SERVER_PORT` from the
environment:

```sh
sudo /tmp/ccc-deploy/setup-server.sh prod test
```

> The test instance binds 8081. On a build that still hardcodes port 8080 it will
> fail to start with a bind conflict against prod. The script warns about this.

Other overrides:

```sh
sudo APP_ROOT=/srv/ccc APP_USER=deploy SWAP_SIZE_GB=1 ./setup-server.sh prod test
```

The script is idempotent, so re-running it after changing the unit template or the
per-instance sizing is the supported way to apply new settings.

## Day-to-day

```sh
systemctl status 'ccc-backend@*'          # both instances at a glance
systemctl status ccc-backend@prod         # is it running, and how much memory
journalctl -u ccc-backend@prod -f         # follow logs
journalctl -u ccc-backend@test -n 200     # recent logs
systemctl restart ccc-backend@prod        # manual restart, rarely needed now
systemd-cgtop -m                          # live memory against the cgroup limits
swapon --show                             # confirm swap is active
```

To confirm the memory ceilings are doing their job, watch `Memory:` in
`systemctl status ccc-backend@prod` over a few days — it should plateau rather than
climb.
