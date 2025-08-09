#!/usr/bin/env bash
set -euox pipefail

# ---- Edit these or pass as env vars ----
: "${LOCATOR_HOST:=192.168.0.201}"
: "${LOCATOR_PORT:=10334}"
: "${REGION:=oom}"
: "${KEY:=probeKey}"
: "${VALUE:=8}"
: "${KEYSTORE:=/opt/certs/client-keystore.jks}"
: "${KSPASS:=password}"
: "${TRUSTSTORE:=/opt/certs/client-truststore.jks}"
: "${TSPASS:=password}"
: "${TLS_PROTOCOL:=TLSv1.2}"  # match your cluster
: "${ENDPOINT_ID_ENABLED:=false}"  # set true if server cert SAN matches hostname
: "${CIPHERS:=}"  # e.g., TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
# ----------------------------------------

mvn -q -DskipTests clean package --settings $HOME/settings.xml

# Uncomment to include TLS debug output
# export JAVA_TOOL_OPTIONS="-Djavax.net.debug=ssl" 
mvn -q exec:java \
-Dexec.mainClass=com.example.ssl.GemFireProbe \
-Dexec.args="${LOCATOR_HOST} ${LOCATOR_PORT} ${REGION} ${KEY} ${VALUE}" \
-Dgemfire.ssl-enabled-components=all \
-Dgemfire.ssl-keystore="${KEYSTORE}" \
-Dgemfire.ssl-keystore-password="${KSPASS}" \
-Dgemfire.ssl-truststore="${TRUSTSTORE}" \
-Dgemfire.ssl-truststore-password="${TSPASS}" \
-Dgemfire.ssl-protocols="${TLS_PROTOCOL}" \
-Dgemfire.ssl-endpoint-identification-enabled="${ENDPOINT_ID_ENABLED}"
