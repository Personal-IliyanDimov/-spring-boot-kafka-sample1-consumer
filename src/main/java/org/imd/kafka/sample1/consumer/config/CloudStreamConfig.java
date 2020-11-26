package org.imd.kafka.sample1.consumer.config;

import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.service.ArbiterService;
import org.imd.kafka.sample1.consumer.service.AuctionService;
import org.imd.kafka.sample1.consumer.service.exception.AuctionAlreadyExistsException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotExistException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotStartedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.config.RoutingFunction;
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
    public RoutingFunction routingFunction() {
        return null;
    }

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


//    @Bean
//    public static Function<Flux<AuctionBidEvent>, Tuple2<Flux<AuctionBidEvent>, Flux<AuctionBidEvent>>> scatter() {
//        return flux -> {
//            Flux<AuctionBidEvent> connectedFlux = flux.publish().autoConnect(2);
//            UnicastProcessor htlUnicastProcessor = UnicastProcessor.create();
//            UnicastProcessor bpUnicastProcessor = UnicastProcessor.create();
//            Flux<AuctionBidEvent> evenFlux = connectedFlux.filter(bidEvent -> number % 2 == 0).doOnNext(bidEvent -> htlUnicastProcessor.onNext(bitEvent));
//            Flux<AuctionBidEvent> oddFlux = connectedFlux.filter(bidEvent -> number % 2 != 0).doOnNext(number -> odd.onNext("ODD: " + number));
//
//            return Tuples.of(Flux.from(even).doOnSubscribe(x -> evenFlux.subscribe()), Flux.from(odd).doOnSubscribe(x -> oddFlux.subscribe()));
//        };
//    }

}
