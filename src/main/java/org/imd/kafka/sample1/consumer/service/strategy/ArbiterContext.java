package org.imd.kafka.sample1.consumer.service.strategy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;

@RequiredArgsConstructor
@Getter
public class ArbiterContext {
    private final AuctionEvent auction;
    private final AuctionBidEvent bestBid;
    private final AuctionBidEvent pretenderBid;
}
