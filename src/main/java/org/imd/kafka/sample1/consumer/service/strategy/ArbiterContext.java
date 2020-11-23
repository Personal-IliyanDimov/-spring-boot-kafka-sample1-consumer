package org.imd.kafka.sample1.consumer.service.strategy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.imd.kafka.sample1.consumer.model.domain.ArbiterData;
import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;

@Getter
@Setter
@RequiredArgsConstructor
public class ArbiterContext {
    private final ArbiterData<Long, AuctionEvent, AuctionBidEvent> arbiterData;
    private final AuctionBidEvent auctionBidEvent;
}
