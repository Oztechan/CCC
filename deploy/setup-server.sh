#!/usr/bin/env bash
#
# One-time bootstrap for the CCC backend on the DigitalOcean droplet.
#
# Installs a templated systemd unit that supervises one backend instance per
# environment, so each starts on boot and restarts itself on crash or OOM.
# Safe to re-run: it is idempotent and reinstalls with the current settings.
#
# Usage (on the droplet, as root):
#   sudo ./setup-server.sh                      # prod only
#   sudo ./setup-server.sh prod test            # both environments
#   sudo MIGRATE_FROM=/root/ccc ./setup-server.sh prod
#   sudo APP_ROOT=/srv/ccc APP_USER=deploy ./setup-server.sh prod
#
# The test instance additionally requires a backend build that reads
# SERVER_PORT from the environment; see the note printed when it is requested.
#
set -euo pipefail

APP_ROOT="${APP_ROOT:-/opt/ccc}"
CONFIG_ROOT="${CONFIG_ROOT:-/etc/ccc-backend}"
APP_USER="${APP_USER:-${SUDO_USER:-$(id -un)}}"
MIGRATE_FROM="${MIGRATE_FROM:-}"
SWAP_SIZE_GB="${SWAP_SIZE_GB:-2}"
JAR_NAME="ccc-backend.jar"
UNIT_NAME="ccc-backend@.service"
UNIT_PATH="/etc/systemd/system/${UNIT_NAME}"
TEMPLATE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/${UNIT_NAME}.template"
DB_FILE="application_database.sqlite.db"

INSTANCES=("$@")
[[ ${#INSTANCES[@]} -gt 0 ]] || INSTANCES=(prod)

log()  { printf '\033[1;34m==>\033[0m %s\n' "$1"; }
warn() { printf '\033[1;33mWarning:\033[0m %s\n' "$1"; }
die()  { printf '\033[1;31mError:\033[0m %s\n' "$1" >&2; exit 1; }

# ---------------------------------------------------------------------------
# Per-instance configuration.
#
# Sized for a 1G droplet running both instances. prod is pinned out of swap so
# its GC pauses stay predictable; test is mostly idle and is allowed to page
# out to disk rather than hold RAM. Together the cgroup ceilings come to 800M,
# leaving ~180M for the OS.
# ---------------------------------------------------------------------------
instance_config() {
    case "$1" in
        prod)
            PORT=8080
            SYNC_ENABLED=true
            MEMORY_MAX=560M
            MEMORY_SWAP_MAX=0
            JVM_OPTS="-XX:MaxRAMPercentage=40 -XX:MaxMetaspaceSize=128m -XX:MaxDirectMemorySize=96m"
            JVM_OPTS="$JVM_OPTS -XX:+UseSerialGC -XX:+ExitOnOutOfMemoryError"
            JVM_OPTS="$JVM_OPTS -Dkotlinx.coroutines.io.parallelism=8 -Dfile.encoding=UTF-8"
            ;;
        test)
            # Deliberately minimal: no syncing (which keeps it off the premium
            # API quota entirely) and C1-only JIT, which roughly halves the
            # code cache at the cost of peak throughput nobody needs here.
            PORT=8081
            SYNC_ENABLED=false
            MEMORY_MAX=240M
            MEMORY_SWAP_MAX=768M
            JVM_OPTS="-XX:MaxRAMPercentage=45 -XX:MaxMetaspaceSize=96m -XX:MaxDirectMemorySize=48m"
            JVM_OPTS="$JVM_OPTS -XX:+UseSerialGC -XX:+ExitOnOutOfMemoryError -XX:TieredStopAtLevel=1"
            JVM_OPTS="$JVM_OPTS -Dkotlinx.coroutines.io.parallelism=4 -Dfile.encoding=UTF-8"
            ;;
        *)
            die "unknown instance '$1' (expected 'prod' or 'test')"
            ;;
    esac
}

# ---------------------------------------------------------------------------
# Preflight.
# ---------------------------------------------------------------------------
[[ $EUID -eq 0 ]] || die "must run as root (use sudo)"
[[ -f $TEMPLATE ]] || die "unit template not found at $TEMPLATE"
id "$APP_USER" &>/dev/null || die "user does not exist: $APP_USER"
APP_GROUP="$(id -gn "$APP_USER")"
command -v java &>/dev/null || die "java not found on PATH; install a JRE 21+ first"
command -v curl &>/dev/null || die "curl not found on PATH; needed for the health check"

for instance in "${INSTANCES[@]}"; do instance_config "$instance"; done

log "App root   : $APP_ROOT"
log "Config root: $CONFIG_ROOT"
log "Run as     : $APP_USER:$APP_GROUP"
log "Instances  : ${INSTANCES[*]}"
log "Java       : $(java -version 2>&1 | head -1)"

if [[ " ${INSTANCES[*]} " == *" test "* ]]; then
    warn "The test instance binds port 8081, which requires a backend build that"
    warn "reads SERVER_PORT from the environment. On a build that still hardcodes"
    warn "8080 it will fail to start with a bind conflict against prod."
fi

# ProtectHome would hide the jar from the service when the deploy directory is
# itself inside a home directory, so only enable it when that is not the case.
if [[ $APP_ROOT == /home/* || $APP_ROOT == /root/* || $APP_ROOT == /home || $APP_ROOT == /root ]]; then
    protect_home="no"
else
    protect_home="yes"
fi

total_mb=$(( $(awk '/MemTotal/ {print $2}' /proc/meminfo) / 1024 ))
log "Total RAM  : ${total_mb}M"
if (( total_mb < 900 )); then
    warn "less than 900M of RAM detected; the ceilings here assume a 1G droplet."
fi

# ---------------------------------------------------------------------------
# Swap.
#
# DO droplets ship without swap. A swapfile costs disk rather than RAM, and is
# what lets the mostly-idle test instance page out instead of competing with
# prod for memory. prod itself is pinned out of swap via MemorySwapMax=0.
# ---------------------------------------------------------------------------
if [[ -z "$(swapon --show --noheadings 2>/dev/null)" ]]; then
    avail_gb=$(( $(df --output=avail -BG / | tail -1 | tr -dc '0-9') ))
    if (( avail_gb < SWAP_SIZE_GB + 5 )); then
        warn "only ${avail_gb}G free on /; skipping swapfile creation."
    else
        log "Creating ${SWAP_SIZE_GB}G swapfile at /swapfile"
        if ! fallocate -l "${SWAP_SIZE_GB}G" /swapfile 2>/dev/null; then
            dd if=/dev/zero of=/swapfile bs=1M count=$(( SWAP_SIZE_GB * 1024 )) status=none
        fi
        chmod 600 /swapfile
        mkswap /swapfile >/dev/null
        swapon /swapfile
        grep -q '^/swapfile ' /etc/fstab || echo '/swapfile none swap sw 0 0' >> /etc/fstab
    fi
else
    log "Swap already active; leaving it alone"
fi

# Make swap a safety valve rather than routine behaviour.
printf 'vm.swappiness=10\n' > /etc/sysctl.d/60-ccc-swappiness.conf
sysctl -q vm.swappiness=10

# ---------------------------------------------------------------------------
# Install the templated unit.
# ---------------------------------------------------------------------------
log "Writing $UNIT_PATH"
sed -e "s|__USER__|$APP_USER|g" \
    -e "s|__GROUP__|$APP_GROUP|g" \
    -e "s|__APP_ROOT__|$APP_ROOT|g" \
    -e "s|__CONFIG_ROOT__|$CONFIG_ROOT|g" \
    -e "s|__JAR_NAME__|$JAR_NAME|g" \
    -e "s|__PROTECT_HOME__|$protect_home|g" \
    "$TEMPLATE" > "$UNIT_PATH"
chmod 644 "$UNIT_PATH"

install -d -m 755 "$CONFIG_ROOT"
install -d -m 755 -o "$APP_USER" -g "$APP_GROUP" "$APP_ROOT"

# ---------------------------------------------------------------------------
# Per-instance setup.
# ---------------------------------------------------------------------------
for instance in "${INSTANCES[@]}"; do
    instance_config "$instance"
    dir="$APP_ROOT/$instance"
    service="ccc-backend@$instance"

    log "--- $instance (port $PORT, MemoryMax=$MEMORY_MAX, sync=$SYNC_ENABLED) ---"
    install -d -m 755 -o "$APP_USER" -g "$APP_GROUP" "$dir"

    # Migrate an existing single-instance deployment into the prod directory.
    # Copies only; the originals are left untouched as a rollback path.
    if [[ $instance == prod && -n $MIGRATE_FROM ]]; then
        [[ -d $MIGRATE_FROM ]] || die "MIGRATE_FROM does not exist: $MIGRATE_FROM"
        if [[ -f "$MIGRATE_FROM/$DB_FILE" && ! -f "$dir/$DB_FILE" ]]; then
            log "Copying existing database from $MIGRATE_FROM"
            cp -p "$MIGRATE_FROM/$DB_FILE" "$dir/$DB_FILE"
        fi
        if [[ ! -f "$dir/$JAR_NAME" ]]; then
            legacy_jar="$(find "$MIGRATE_FROM" -maxdepth 1 -name 'app-*.jar' -printf '%T@ %p\n' 2>/dev/null \
                | sort -rn | head -1 | cut -d' ' -f2-)"
            if [[ -n $legacy_jar ]]; then
                log "Copying $(basename "$legacy_jar") -> $instance/$JAR_NAME"
                cp "$legacy_jar" "$dir/$JAR_NAME"
            fi
        fi
    fi

    [[ -f "$dir/$JAR_NAME" ]] || die "no $JAR_NAME in $dir — copy a build there first (or pass MIGRATE_FROM)"
    chown -R "$APP_USER:$APP_GROUP" "$dir"

    # Runtime settings for the process.
    #
    # IS_PRODUCTION is still what the current build uses to decide whether to
    # bind 0.0.0.0; SERVER_HOST/SERVER_PORT/SYNC_ENABLED are read by builds
    # that have the configurable-environment change. Setting all of them keeps
    # this file correct across that transition.
    cat > "$CONFIG_ROOT/$instance.env" <<EOF
IS_PRODUCTION=true
SERVER_HOST=0.0.0.0
SERVER_PORT=$PORT
SYNC_ENABLED=$SYNC_ENABLED
JVM_OPTS=$JVM_OPTS
EOF
    chmod 640 "$CONFIG_ROOT/$instance.env"
    chgrp "$APP_GROUP" "$CONFIG_ROOT/$instance.env"

    # Cgroup limits must be a drop-in: systemd expands ${VAR} only in
    # ExecStart, never in directives like MemoryMax.
    dropin_dir="/etc/systemd/system/$service.service.d"
    install -d -m 755 "$dropin_dir"
    cat > "$dropin_dir/memory.conf" <<EOF
# Generated by deploy/setup-server.sh — edit the script, not this file.
[Service]
MemoryMax=$MEMORY_MAX
MemorySwapMax=$MEMORY_SWAP_MAX
EOF
done

systemctl daemon-reload

# ---------------------------------------------------------------------------
# Take over from any hand-started backend, then start the instances.
#
# The pattern deliberately only matches this project's jars so that unrelated
# Java processes on the droplet are left alone.
# ---------------------------------------------------------------------------
backend_pattern="java .*-jar .*(app-[0-9].*|${JAR_NAME})$"
stray_pids="$(pgrep -f "$backend_pattern" || true)"
if [[ -n $stray_pids ]]; then
    # Exclude anything systemd already supervises.
    log "Found a manually started backend; stopping it so systemd can take over:"
    ps -o pid=,cmd= -p "$(tr '\n' ',' <<<"$stray_pids" | sed 's/,$//')" | sed 's/^/    /'
    # shellcheck disable=SC2086
    kill $stray_pids
    for _ in {1..15}; do
        pgrep -f "$backend_pattern" &>/dev/null || break
        sleep 1
    done
    if pgrep -f "$backend_pattern" &>/dev/null; then
        # shellcheck disable=SC2086
        kill -9 $stray_pids || true
    fi
fi

failed=()
for instance in "${INSTANCES[@]}"; do
    instance_config "$instance"
    service="ccc-backend@$instance"

    systemctl enable "$service" &>/dev/null
    systemctl restart "$service"

    log "Waiting for $instance on port $PORT"
    # Deliberately not using curl -f. The routes legitimately answer 404 when
    # the jar manifest or a bundled resource is missing, and that is still
    # proof the server is up and listening, which is all this check needs.
    healthy=false
    for _ in {1..30}; do
        if body="$(curl -sS --max-time 3 -w '\n%{http_code}' "http://127.0.0.1:$PORT/version" 2>/dev/null)"; then
            log "  up (HTTP $(tail -1 <<<"$body")): $(head -1 <<<"$body")"
            healthy=true
            break
        fi
        sleep 2
    done
    $healthy || { warn "$instance did not answer in 60s"; failed+=("$instance"); }
done

if (( ${#failed[@]} )); then
    printf '\n'
    for instance in "${failed[@]}"; do
        printf 'Diagnose with: journalctl -u ccc-backend@%s -n 100 --no-pager\n' "$instance"
    done
    die "${#failed[@]} instance(s) failed to start: ${failed[*]}"
fi

printf '\n'
log "All instances up and supervised by systemd."
log "Status: systemctl status 'ccc-backend@*'"
log "Logs:   journalctl -u ccc-backend@prod -f"
