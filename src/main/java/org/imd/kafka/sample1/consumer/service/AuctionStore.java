package org.imd.kafka.sample1.consumer.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class AuctionStore<V,K> {

    private HashMap<K,V> auctionMap;

    public <K,V> AuctionStore() {
        ConcurrentMap<K,V> auctionMap = new ConcurrentHashMap<>();
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
