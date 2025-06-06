package com.example;

import dev.gemfire.dtype.DSemaphore;
import dev.gemfire.dtype.DTypeFactory;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;

public class SemaphoreClient {
    private static DTypeFactory factory;
    public static void main(String[] args) throws Exception {
        ClientCache cache = new ClientCacheFactory()
                .set("name", "dsemaphore-client")
                .set("cache-xml-file", "client-cache.xml")
                .create();
        factory = new DTypeFactory(cache);
        DSemaphore semaphore = factory.createDSemaphore("oom-resource-lock",2);

        try {
            System.out.println("Trying to acquire...");
            semaphore.acquire();
            System.out.println("Acquired semaphore!");
            Thread.sleep(2000); // simulate work
        } finally {
            System.out.println("Releasing semaphore.");
            semaphore.release();
        }

        cache.close();
    }
}
