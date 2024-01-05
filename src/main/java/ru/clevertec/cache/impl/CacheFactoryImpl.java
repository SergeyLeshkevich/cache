package ru.clevertec.cache.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    private static final String CAPACITY_KEY = "capacity";
    private static final String ALGORITHM_KEY = "algorithm";

    private final Environment environment;

    /**
     * Gets a cache object depending on the settings in application.yaml
     *
     * @return Cache
     */
    @Override
    public Cache<K, V> createCache() {
        return "LFU".equals(environment.getProperty(ALGORITHM_KEY))
                ? new LFUCache<>(Integer.parseInt(Objects.requireNonNull(environment.getProperty(CAPACITY_KEY))))
                : new LRUCache<>(Integer.parseInt(Objects.requireNonNull(environment.getProperty(CAPACITY_KEY))));
    }
}
