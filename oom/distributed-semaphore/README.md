# Distributed Semaphore Demo

## Deploy the Extension

- This assumes Gemfire installation has already completed
- Download `v0.1.6` `.gfm` file from https://github.com/gemfire/gemfire-distributed-types/releases and place it in:

```bash
$GEMFIRE_HOME/extensions/
```

## Start GemFire Locator and Server

```bash

gfsh -e "start locator --name=oom-locator"
gfsh -e "start server --name=oom-server-x --locators=datasvc06[10334]"
gfsh -e "start server --name=oom-server-y --locators=datasvc06[10334] --server-port=40405"
```

## Confirm the Semaphore function is registered in Gemfire

```bash
gfsh -e "connect" -e "list functions"

(1) Executing - connect

Connecting to Locator at [host=localhost, port=10334] ..
Connecting to Manager at [host=datasvc06, port=1099] ..
Successfully connected to: [host=datasvc06, port=1099]

You are connected to a cluster of version 10.1.2.


(2) Executing - list functions

   Member    | Function
------------ | --------------------------
oom-server-x | dsemaphore-function
oom-server-x | dsemaphore-ping-function
oom-server-x | dtype-collections-function
oom-server-y | dsemaphore-function
oom-server-y | dsemaphore-ping-function
oom-server-y | dtype-collections-function
```

## Create Region

```bash
gfsh -e "connect" -e "create region --name=semaphores --type=REPLICATE"
```

## Build the Demo

```bash
cd distributed-semaphore/
mvn clean package --settings dev/settings.xml

# Build Classpath
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
```

## Run the Client

```bash
java -cp "target/dsemaphore-demo-1.0.jar:$(cat cp.txt)" com.example.SemaphoreClient
```

### Output Example

```bash
[info 2025/06/06 04:44:38.533 UTC dsemaphore-client <poolTimer-default-2> tid=0x1d] Updating membership port.  Port changed from 0 to 40944.  ID is now datasvc06(dsemaphore-client:696852:loner):0:3c238e43:dsemaphore-client

Trying to acquire...
Acquired semaphore!
Releasing semaphore.
[info 2025/06/06 04:44:40.741 UTC dsemaphore-client <main> tid=0x1] GemFireCache[id = 31366484; isClosing = true; isShutDownAll = false; created = Fri Jun 06 04:44:38 UTC 2025; server = false; copyOnRead = false; lockLease = 120; lockTimeout = 60]: Now closing.

```