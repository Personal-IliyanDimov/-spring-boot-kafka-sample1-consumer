package org.imd.kafka.sample1.consumer.config;

import org.imd.kafka.sample1.consumer.model.domain.ArbiterData;
import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;
import org.imd.kafka.sample1.consumer.service.ArbiterService;
import org.imd.kafka.sample1.consumer.service.ArbiterStore;
import org.imd.kafka.sample1.consumer.service.AuctionService;
import org.imd.kafka.sample1.consumer.service.AuctionStore;
import org.imd.kafka.sample1.consumer.service.strategy.ArbiterStrategy;
import org.imd.kafka.sample1.consumer.service.strategy.BestPriceStrategy;
import org.imd.kafka.sample1.consumer.service.strategy.HitTheTargetStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class ServiceConfig {

    private ArbiterStore<ArbiterData<Long, AuctionEvent, AuctionBidEvent>, Long> arbiterStore;
    private AuctionStore<AuctionEvent, Long> auctionStore;
    private ConcurrentMap<AuctionType, ArbiterStrategy> strategyMap;

    public ServiceConfig() {
        arbiterStore = new ArbiterStore<>();
        auctionStore = new AuctionStore<>();

        strategyMap = new ConcurrentHashMap<>();
        strategyMap.put(AuctionType.HIT_THE_TARGET, new HitTheTargetStrategy());
        strategyMap.put(AuctionType.BEST_PRICE, new BestPriceStrategy());
    }

    @Bean
    public AuctionService getAuctionService() {
        return new AuctionService(auctionStore);
    }

    @Bean
    public ArbiterService getArbiterService() {
        return new ArbiterService(arbiterStore, auctionStore, strategyMap);
    }
}
