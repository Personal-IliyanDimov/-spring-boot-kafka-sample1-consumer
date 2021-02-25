package org.imd.kafka.sample1.consumer.config;

import org.imd.kafka.sample1.consumer.model.domain.ArbiterData;
import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;
import org.imd.kafka.sample1.consumer.service.ArbiterService;
import org.imd.kafka.sample1.consumer.service.ArbiterStore;
import org.imd.kafka.sample1.consumer.service.strategy.ArbiterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class ServiceConfig {

    @Autowired
    private List<ArbiterStrategy> arbiterStrategies;

    private ConcurrentMap<AuctionType, ArbiterStrategy> strategyMap;

    @PostConstruct
    protected void postConstruct() {
        strategyMap = new ConcurrentHashMap<>();
        arbiterStrategies.stream().forEach(
            s -> strategyMap.putIfAbsent(s.getType(), s)
        );
    }

    @Bean
    public ArbiterService getArbiterService() {
        final ArbiterStore<ArbiterData<Long, AuctionEvent, AuctionBidEvent>, Long> arbiterStore = new ArbiterStore<>();
        return new ArbiterService(arbiterStore, strategyMap);
    }
}
