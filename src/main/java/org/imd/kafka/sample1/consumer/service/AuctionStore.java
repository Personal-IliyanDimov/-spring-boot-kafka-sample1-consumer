package org.imd.kafka.sample1.consumer.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuctionStore<V,K> {

    private final Map<K,V> auctionMap;

    public <K,V> AuctionStore() {
        this.auctionMap = new ConcurrentHashMap<>();
    }

    public V findAuction(K key) {
        return auctionMap.get(key);
    }

    public void saveAuction(K key, V auction) {
       auctionMap.put(key, auction);
    }

    public void remove(K key) {
        auctionMap.remove(key);
    }
}
