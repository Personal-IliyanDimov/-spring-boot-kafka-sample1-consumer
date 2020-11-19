package org.imd.kafka.sample1.consumer.config;

import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.springframework.cloud.function.context.config.RoutingFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.Function;

@Configuration
public class TopicConfig {

    @Bean
    public RoutingFunction routingFunction() {

        return null;
    }

    public Function<Flux<Auction>> auctionEventConsumer() {
        return null;
    }


    @Bean
    public static Function<Flux<AuctionBidEvent>, Tuple2<Flux<AuctionBidEvent>, Flux<AuctionBidEvent>>> scatter() {
        return flux -> {
            Flux<AuctionBidEvent> connectedFlux = flux.publish().autoConnect(2);
            UnicastProcessor htlUnicastProcessor = UnicastProcessor.create();
            UnicastProcessor bpUnicastProcessor = UnicastProcessor.create();
            Flux<AuctionBidEvent> evenFlux = connectedFlux.filter(bidEvent -> number % 2 == 0).doOnNext(bidEvent -> htlUnicastProcessor.onNext(bitEvent));
            Flux<AuctionBidEvent> oddFlux = connectedFlux.filter(bidEvent -> number % 2 != 0).doOnNext(number -> odd.onNext("ODD: " + number));

            return Tuples.of(Flux.from(even).doOnSubscribe(x -> evenFlux.subscribe()), Flux.from(odd).doOnSubscribe(x -> oddFlux.subscribe()));
        };
    }
}
