# Simple Java Client to validate SSL/TLS connectivity with Gemfire

## GemFireProbe

- Minimal GemFire client that connects to the locator (over SSL), discovers servers, and does a `put/get` on a region.
- Both scripts are instrumented with `-Djavax.net.debug=ssl` so you can see the TLS handshakes and any errors.

## Prerequisites
- JDK **21**
- Apache Maven 3.8+

## Quick start

```bash
export LOCATOR_HOST="192.168.0.102"
export KEYSTORE="$HOME/oomgf/trusted.keystore"
export TRUSTSTORE="$HOME/oomgf/trusted.keystore"
export KEY="oomProbeKey"
export VALUE="8"

# Create a test region
gfsh -e "run --file=gfsh/setup-region.gfsh"

./scripts/run-gemfire-probe.sh
```

## gfsh helper

`gfsh/setup-region.gfsh` creates a simple PARTITION region named `/oom` and seeds a few entries.
If your `gfsh` is not already SSL-enabled via `gemfire.properties`, pass the same `-Dgemfire.ssl-*` system properties you use for the GemFire probe, e.g.:

```bash
gfsh \
--J=-Dgemfire.ssl-enabled-components=all \
--J=-Dgemfire.ssl-keystore=$HOME/oomgf/trusted.keystore \
--J=-Dgemfire.ssl-keystore-password=password \
--J=-Dgemfire.ssl-truststore=$HOME/oomgf/trusted.keystore \
--J=-Dgemfire.ssl-truststore-password=password \
-e "run --file=gfsh/setup-region.gfsh"
```
