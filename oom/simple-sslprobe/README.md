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

cat > gfsh/gemfire.properties << EOF
ssl-enabled-components=all
ssl-keystore=$HOME/oomgf/trusted.keystore
ssl-keystore-password=password
ssl-truststore=$HOME/oomgf/trusted.keystore
ssl-truststore-password=password
ssl-ciphers=TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
ssl-endpoint-identification-enabled=false
ssl-truststore-type=JKS
ssl-keystore-type=JKS
ssl-protocols=any
EOF

# Create a test region
gfsh -e "connect --locator=192.168.0.102[10334] --security-properties-file=gfsh/gemfire.properties" \
-e "run --file=gfsh/setup-region.gfsh"

./scripts/run-gemfire-probe.sh
```

## gfsh helper

`gfsh/setup-region.gfsh` creates a simple PARTITION region named `/oom` and seeds a few entries.

