package ru.clevertec.cache.impl;

import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.util.YmlManager;

import java.io.FileNotFoundException;
import java.util.Map;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    private static final String FILENAME = "src/main/resources/application.yaml";
    private static final String CAPACITY_KEY = "capacity";
    private static final String ALGORITHM_KEY = "algorithm";

    /**
     * Gets a cache object depending on the settings in application.yaml
     *
     * @return Cache
     */
    @Override
    public Cache<K, V> createCache() {
        try {
            Map<String, Object> mapValueYaml = YmlManager.getValue(FILENAME);
            return "LFU".equals(mapValueYaml.get(ALGORITHM_KEY))
                    ? new LFUCache<>((Integer) mapValueYaml.get(CAPACITY_KEY))
                    : new LRUCache<>((Integer) mapValueYaml.get(CAPACITY_KEY));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
