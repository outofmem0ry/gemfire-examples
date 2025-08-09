package com.example.ssl;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.*;
import java.util.Properties;

public class GemFireProbe {

  public static void main(String[] args) {
    if (args.length < 5) {
      System.err.println("Usage: java GemFireProbe <locatorHost> <locatorPort> <regionName> <keyname> <value>");
      System.err.println("Pass SSL via -Dgemfire.ssl-* system properties (see run scripts).");
      System.exit(2);
    }
    String locatorHost = args[0];
    int locatorPort = Integer.parseInt(args[1]);
    String regionName = args[2];
    String regionKey = args[3];
    int regionValue = Integer.parseInt(args[4]);

    Properties p = new Properties();
    putIfSet(p, "ssl-enabled-components", "cluster");
    putIfSet(p, "ssl-keystore", System.getProperty("gemfire.ssl-keystore"));
    putIfSet(p, "ssl-keystore-password", System.getProperty("gemfire.ssl-keystore-password"));
    putIfSet(p, "ssl-truststore", System.getProperty("gemfire.ssl-truststore"));
    putIfSet(p, "ssl-truststore-password", System.getProperty("gemfire.ssl-truststore-password"));
    putIfSet(p, "ssl-protocols", System.getProperty("gemfire.ssl-protocols", "TLSv1.2"));
    putIfSet(p, "ssl-ciphers", System.getProperty("gemfire.ssl-ciphers"));
    putIfSet(p, "ssl-endpoint-identification-enabled",
        System.getProperty("gemfire.ssl-endpoint-identification-enabled", "false"));

    System.setProperty("jdk.tls.client.protocols",
        System.getProperty("jdk.tls.client.protocols", p.getProperty("ssl-protocols", "TLSv1.2")));

    System.out.printf("Connecting to %s[%d], region=%s%n", locatorHost, locatorPort, regionName);

    try (ClientCache cache = new ClientCacheFactory(p)
        .addPoolLocator(locatorHost, locatorPort)
        .setPdxReadSerialized(false)
        .create()) {

      Region<String, Integer> r = cache.<String, Integer>createClientRegionFactory(ClientRegionShortcut.PROXY)
          .create(regionName);

      Integer before = r.get(regionKey);
      r.put(regionKey, regionValue);
      Integer after = r.get(regionKey);

      System.out.printf("##### SUCCESS region=%s key=%s before=%s after=%s%n ######", regionName,
          regionKey, before, after);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void putIfSet(Properties p, String k, String v) {
    if (v != null && !v.isBlank()) p.setProperty(k, v);
  }
}
