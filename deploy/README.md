# Backend deployment

The backend is a Ktor/Netty service running on a small DigitalOcean droplet. This
directory contains the systemd unit that supervises it.

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

## What the unit does

**Caps every region explicitly** rather than letting it be inferred from total RAM:
heap via `MaxRAMPercentage`, off-heap via `MaxDirectMemorySize`, plus
`MaxMetaspaceSize`. This is the part that actually fixes the problem — a hard
ceiling holds regardless of how eagerly any given allocator returns memory.

`UseSerialGC` is set for determinism rather than effect: JVM ergonomics already
selects SerialGC below 2 CPUs and ~1792 MB, so on the current droplet this only
guarantees the choice survives a resize to a larger droplet.

**Makes recovery automatic.** `Restart=always` means a crash or an OOM brings the
service straight back. `MemoryMax` sits below total RAM so *systemd* stops the
service and restarts it, rather than the kernel OOM killer picking a victim and
leaving nothing running. `ExitOnOutOfMemoryError` ensures a heap exhaustion exits
promptly instead of thrashing.

There is deliberately **no `MemoryHigh`**. It throttles the cgroup and forces
reclaim, but a JVM's footprint is almost entirely anonymous pages, which cannot be
reclaimed on a droplet with no swap — the service would stall under sustained
pressure rather than fail cleanly and restart.

**Survives reboots**, via `WantedBy=multi-user.target`.

### The numbers on a 1 GB droplet

`MemoryMax` also becomes the cgroup limit the JVM reads for container awareness, so
`MaxRAMPercentage` resolves against that ceiling rather than the whole droplet:

| | |
| --- | --- |
| `MemTotal` | ~981 MB |
| `MemoryMax` (75%) | ~735 MB — leaves ~245 MB for the OS |
| Max heap (40% of 735) | ~294 MB |
| Max direct | 96 MB |
| Max metaspace | 128 MB |
| Code cache, stacks, GC | ~90 MB |
| **Worst-case total** | **~608 MB, against a 735 MB ceiling** |

Metaspace and direct memory are caps rather than reservations, so steady-state usage
sits well below this. The headroom is what keeps a traffic spike from tripping the
cgroup limit.

## One-time setup

Copy this directory to the droplet and run the script from the deploy directory —
the one already holding the jar and `application_database.sqlite.db`:

```sh
scp -r deploy/ user@droplet:/tmp/ccc-deploy
ssh user@droplet
cd /path/to/current/deploy/dir
sudo /tmp/ccc-deploy/setup-server.sh
```

Override the defaults if the script cannot infer them:

```sh
sudo APP_DIR=/srv/ccc APP_USER=deploy /tmp/ccc-deploy/setup-server.sh
```

The script is idempotent, so re-running it after changing the unit template is the
supported way to apply new settings. It will:

1. promote the newest `app-<version>.jar` to the stable name `ccc-backend.jar`;
2. derive `MemoryHigh`/`MemoryMax` from the droplet's actual RAM;
3. stop any manually started backend and hand supervision to systemd;
4. install, enable and start the unit, then wait for the port to answer.

The working directory is left unchanged, so the existing SQLite file is picked up
in place.

## Day-to-day

```sh
systemctl status ccc-backend          # is it running, and how much memory
journalctl -u ccc-backend -f          # follow logs
journalctl -u ccc-backend -n 200      # recent logs
systemctl restart ccc-backend         # manual restart, rarely needed now
systemd-cgtop -m                      # live memory against the cgroup limit
```

To confirm the memory ceiling is doing its job, watch `Memory:` in
`systemctl status` over a few days — it should plateau rather than climb.
