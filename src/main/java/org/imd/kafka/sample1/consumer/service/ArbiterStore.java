package org.imd.kafka.sample1.consumer.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ArbiterStore<V, K> {

    private ConcurrentMap<K,V> arbiterMap;

    public <K,V> ArbiterStore() {
        this.arbiterMap = new ConcurrentHashMap<>();
    }

    public V findArbiterData(K key) {
        return arbiterMap.get(key);
    }

    public void saveArbiterData(K key, V auction) {
        arbiterMap.put(key, auction);
    }

    public void removeArbiterData(K key) {
        arbiterMap.remove(key);
    }
}
