package com.example.reset_region_val_ssl;

import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("/api")

public class ResetController {

    private final Region<String, Integer> region;

    // Optional fast seeding via env/property
    private final int seedCount;

    public ResetController(Region<String, Integer> region,
        @Value("${seed.count:0}") int seedCount) {
        this.region = region;
        this.seedCount = seedCount;

        if (this.seedCount > 0) {
            SecureRandom rnd = new SecureRandom();
            for (int i = 1; i <= this.seedCount; i++) {
                region.putIfAbsent("K" + i, rnd.nextInt(10_000));
            }
        }
    }

    @GetMapping("/get/{key}")
    public Map<String, Object> get(@PathVariable String key) {
        Integer v = region.get(key);
        return Map.of("key", key, "value", v);
    }

    @PostMapping("/put/{key}/{value}")
    public Map<String, Object> put(@PathVariable String key, @PathVariable Integer value) {
        region.put(key, value);
        return Map.of("key", key, "value", value);
    }

    // The main ask: reset key to zero
    @PostMapping("/reset/{key}")
    public Map<String, Object> reset(@PathVariable String key) {
        region.put(key, 0);
        return Map.of("key", key, "value", 0);
    }

    // Batch reset: POST a JSON array of keys: ["K1","K2",...]
    @PostMapping("/reset-batch")
    public ResponseEntity<?> resetBatch(@RequestBody List<String> keys) {
        keys.forEach(k -> region.put(k, 0));
        return ResponseEntity.ok(Map.of("resetCount", keys.size()));
    }
}
