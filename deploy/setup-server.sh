#!/usr/bin/env bash
#
# One-time bootstrap for the CCC backend on the DigitalOcean droplet.
#
# Installs the systemd unit that supervises the Ktor backend, so the service
# starts on boot and restarts itself on crash or OOM. Safe to re-run: it is
# idempotent and will simply reinstall the unit with the current settings.
#
# Usage (on the droplet, as root):
#   sudo ./setup-server.sh
#   sudo APP_DIR=/srv/ccc APP_USER=deploy ./setup-server.sh
#
set -euo pipefail

APP_DIR="${APP_DIR:-$(pwd)}"
APP_USER="${APP_USER:-${SUDO_USER:-$(id -un)}}"
HEALTH_URL="${HEALTH_URL:-http://127.0.0.1:8080/version}"
JAR_NAME="ccc-backend.jar"
SERVICE_NAME="ccc-backend"
UNIT_PATH="/etc/systemd/system/${SERVICE_NAME}.service"
TEMPLATE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/${SERVICE_NAME}.service.template"

log() { printf '\033[1;34m==>\033[0m %s\n' "$1"; }
die() { printf '\033[1;31mError:\033[0m %s\n' "$1" >&2; exit 1; }

[[ $EUID -eq 0 ]] || die "must run as root (use sudo)"
[[ -f $TEMPLATE ]] || die "unit template not found at $TEMPLATE"
[[ -d $APP_DIR ]] || die "APP_DIR does not exist: $APP_DIR"
id "$APP_USER" &>/dev/null || die "user does not exist: $APP_USER"
APP_GROUP="$(id -gn "$APP_USER")"
command -v java &>/dev/null || die "java not found on PATH; install a JRE 21+ first"
command -v curl &>/dev/null || die "curl not found on PATH; needed for the health check"

# ProtectHome would hide the jar from the service when the deploy directory is
# itself inside a home directory, so only enable it when that is not the case.
if [[ $APP_DIR == /home/* || $APP_DIR == /root/* || $APP_DIR == /home || $APP_DIR == /root ]]; then
    protect_home="no"
else
    protect_home="yes"
fi

log "Deploy dir : $APP_DIR"
log "Run as     : $APP_USER:$APP_GROUP"
log "Java       : $(java -version 2>&1 | head -1)"

# ---------------------------------------------------------------------------
# Establish the stable jar name the unit points at.
#
# Releases currently land as app-<version>.jar. The unit must reference a path
# that never changes, so promote the newest versioned jar to ccc-backend.jar.
# Once CI publishes that name directly this block becomes a no-op.
# ---------------------------------------------------------------------------
if [[ ! -f "$APP_DIR/$JAR_NAME" ]]; then
    newest_jar="$(find "$APP_DIR" -maxdepth 1 -name 'app-*.jar' -printf '%T@ %p\n' 2>/dev/null \
        | sort -rn | head -1 | cut -d' ' -f2-)"
    [[ -n $newest_jar ]] || die "no $JAR_NAME and no app-*.jar found in $APP_DIR"
    log "Promoting $(basename "$newest_jar") -> $JAR_NAME"
    cp "$newest_jar" "$APP_DIR/$JAR_NAME"
fi
chown "$APP_USER:$APP_GROUP" "$APP_DIR/$JAR_NAME"

# ---------------------------------------------------------------------------
# Memory ceiling, derived from the droplet's actual RAM.
#
# MemoryMax is the hard stop that triggers a systemd restart. It sits below
# total RAM so systemd reacts before the kernel OOM killer does — the kernel
# kills without restarting, which is what has been forcing the manual
# intervention. It is also the limit the JVM reads for container awareness, so
# MaxRAMPercentage in the unit resolves against this number.
# ---------------------------------------------------------------------------
total_mb=$(( $(awk '/MemTotal/ {print $2}' /proc/meminfo) / 1024 ))
memory_max="$(( total_mb * 75 / 100 ))M"
log "Total RAM  : ${total_mb}M  (MemoryMax=$memory_max)"

if (( total_mb < 900 )); then
    log "WARNING: less than 900M of RAM detected. The unit's ceilings are tuned"
    log "         for a 1G droplet; review MaxRAMPercentage before relying on it."
fi

# ---------------------------------------------------------------------------
# Stop whatever is currently serving on the port before handing over.
#
# The pattern deliberately only matches this project's jars so that unrelated
# Java processes on the droplet are left alone.
# ---------------------------------------------------------------------------
if systemctl is-active --quiet "$SERVICE_NAME"; then
    log "Stopping existing $SERVICE_NAME service"
    systemctl stop "$SERVICE_NAME"
fi

backend_pattern="java .*-jar .*(app-[0-9].*|${JAR_NAME})$"
stray_pids="$(pgrep -f "$backend_pattern" || true)"
if [[ -n $stray_pids ]]; then
    log "Found a manually started backend; it will be stopped and taken over by systemd:"
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

# ---------------------------------------------------------------------------
# Install and start.
# ---------------------------------------------------------------------------
log "Writing $UNIT_PATH"
sed -e "s|__USER__|$APP_USER|g" \
    -e "s|__GROUP__|$APP_GROUP|g" \
    -e "s|__APP_DIR__|$APP_DIR|g" \
    -e "s|__JAR_NAME__|$JAR_NAME|g" \
    -e "s|__MEMORY_MAX__|$memory_max|g" \
    -e "s|__PROTECT_HOME__|$protect_home|g" \
    "$TEMPLATE" > "$UNIT_PATH"
chmod 644 "$UNIT_PATH"

systemctl daemon-reload
systemctl enable "$SERVICE_NAME"
systemctl restart "$SERVICE_NAME"

log "Waiting for the backend to accept connections on $HEALTH_URL"

# Deliberately not using curl -f. The routes legitimately answer 404 when the
# jar manifest or a bundled resource is missing, and that is still proof that
# the server is up and listening — which is all this check needs to establish.
for _ in {1..30}; do
    if response="$(curl -sS --max-time 3 -w '\n%{http_code}' "$HEALTH_URL" 2>/dev/null)"; then
        log "Backend is up (HTTP $(tail -1 <<<"$response")): $(head -1 <<<"$response")"
        log "Supervised by systemd; it will now restart itself on crash or OOM."
        log "Logs:   journalctl -u $SERVICE_NAME -f"
        log "Status: systemctl status $SERVICE_NAME"
        exit 0
    fi
    sleep 2
done

die "backend did not accept connections in 60s — check: journalctl -u $SERVICE_NAME -n 100"
