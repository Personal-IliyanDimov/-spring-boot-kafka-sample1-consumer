package org.imd.kafka.sample1.consumer.config;

import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionFlushEvent;
import org.imd.kafka.sample1.consumer.service.ArbiterService;
import org.imd.kafka.sample1.consumer.service.AuctionService;
import org.imd.kafka.sample1.consumer.service.exception.AuctionAlreadyExistsException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotExistException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotStartedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class CloudStreamConfig {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private ArbiterService arbiterService;

    @Bean
    public Consumer<Flux<AuctionEvent>> auctionEventConsumer() {
        return (flux) -> flux
            .subscribe(aEvent -> {
                try {
                    auctionService.processAuction(aEvent);
                } catch (AuctionAlreadyExistsException e) {
                    e.printStackTrace();
                }
            });
    }

    @Bean
    public Consumer<Flux<AuctionBidEvent>> auctionBidEventConsumer() {
        return (flux) -> flux
            .subscribe(abEvent -> {
                 try {
                    arbiterService.processAuctionBid(abEvent);
                 } catch (AuctionNotStartedException e) {
                     e.printStackTrace();
                 } catch (AuctionNotExistException e) {
                     e.printStackTrace();
                 }
            });
    }

    @Bean
    public Consumer<Flux<AuctionFlushEvent>> auctionFlushEventConsumer() {
        return (flux) -> flux
            .subscribe(abEvent -> {
                try {
                    arbiterService.processAuctionFlush(abEvent);
                } catch (AuctionNotExistException e) {
                    e.printStackTrace();
                }
            });
    }
}
