package ru.clevertec.cache.impl;

import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.util.YamlManager;

import java.util.Map;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    public static final String APPLICATION_YAML = "\\application.yaml";
    private static final String CAPACITY_KEY = "capacity";
    private static final String ALGORITHM_KEY = "algorithm";

    /**
     * Gets a cache object depending on the settings in application.yaml
     *
     * @return Cache
     */
    @Override
    public Cache<K, V> createCache() {
        Map<String, Object> mapValueYaml = new YamlManager().getValue(APPLICATION_YAML);
        return "LFU".equals(mapValueYaml.get(ALGORITHM_KEY))
                ? new LFUCache<>((Integer) mapValueYaml.get(CAPACITY_KEY))
                : new LRUCache<>((Integer) mapValueYaml.get(CAPACITY_KEY));
    }
}
