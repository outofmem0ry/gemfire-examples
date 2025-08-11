package com.example.reset_region_val_ssl;

import org.apache.geode.cache.client.*;
import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class GemfireClientConfig {
    @Value("${gemfire.locator.host}")
    private String locatorHost;
    @Value("${gemfire.locator.port}")
    private int locatorPort;
    @Value("${gemfire.region.name}")
    private String regionName;

    // SSL
    @Value("${gemfire.ssl.enabled-components:all}")
    private String enabledComponent;
    @Value("${gemfire.ssl.enabled:true}")
    private boolean sslEnabled;
    @Value("${gemfire.ssl.keystore}")
    private String keyStore;
    @Value("${gemfire.ssl.keystorePassword}")
    private String keyStorePassword;
    @Value("${gemfire.ssl.truststore}")
    private String trustStore;
    @Value("${gemfire.ssl.truststorePassword}")
    private String trustStorePassword;
    @Value("${gemfire.ssl.protocols:TLSv1.2}")
    private String sslProtocols;
    @Value("${gemfire.ssl.ciphers:}")
    private String sslCiphers;

    @Bean(destroyMethod = "close")
    public ClientCache clientCache() {
        Properties props = new Properties();

        if (sslEnabled) {
            // Client-to-cluster SSL
            props.setProperty("ssl-enabled-components", enabledComponent);
            props.setProperty("ssl-keystore", keyStore);
            props.setProperty("ssl-keystore-password", keyStorePassword);
            props.setProperty("ssl-truststore", trustStore);
            props.setProperty("ssl-truststore-password", trustStorePassword);
            if (!sslProtocols.isBlank())
                props.setProperty("ssl-protocols", sslProtocols);
            if (!sslCiphers.isBlank())
                props.setProperty("ssl-ciphers", sslCiphers);
        }

        // Optional: enable PDX if you use PDX-serialized objects
        return new ClientCacheFactory(props)
                .addPoolLocator(locatorHost, locatorPort)
                .setPdxReadSerialized(false)
                .create();
    }

    @Bean
    public Region<String, Integer> region(ClientCache cache) {
        ClientRegionFactory<String, Integer> crf = cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
        return crf.create(regionName);
    }
}
