package org.imd.kafka.sample1.consumer.config;

import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionFlushEvent;
import org.imd.kafka.sample1.consumer.service.ArbiterService;
import org.imd.kafka.sample1.consumer.service.exception.AuctionAlreadyExistsException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotExistException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotStartedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;

@Configuration
public class CloudStreamConfig {

    @Autowired
    private ArbiterService arbiterService;

    @Bean
    public Consumer<Flux<AuctionEvent>> auctionEventConsumer() {
        return (flux) -> flux
            .name("auction-event-metrics")
            .metrics()
            .parallel()
            .runOn(Schedulers.newElastic("auction-thread"))
            .subscribe(aEvent -> {
                try {
                    arbiterService.processAuction(aEvent);
                } catch (AuctionAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }, e -> System.out.println(e) );
    }

    @Bean
    public Consumer<Flux<AuctionBidEvent>> auctionBidEventConsumer() {
        return (flux) -> flux
            .name("auction-bid-event-metrics")
            .metrics()
            //.publishOn(createFixedThreadPoolScheduler("auction-bid-thread"))
            .subscribe(abEvent -> {
                 try {
                    arbiterService.processAuctionBid(abEvent);
                 } catch (AuctionNotStartedException e) {
                     e.printStackTrace();
                 } catch (AuctionNotExistException e) {
                     e.printStackTrace();
                 }
            }, e -> System.out.println(e) );
    }

    @Bean
    public Consumer<Flux<AuctionFlushEvent>> auctionFlushEventConsumer() {
        return (flux) -> flux
            .name("auction-flush-event-metrics")
            .metrics()
            // .publishOn(createFixedThreadPoolScheduler("auction-flush-thread"))
            .subscribe(abEvent -> {
                try {
                    arbiterService.processAuctionFlush(abEvent);
                } catch (AuctionNotExistException e) {
                    e.printStackTrace();
                }
            }, e -> System.out.println(e) );
    }
}
