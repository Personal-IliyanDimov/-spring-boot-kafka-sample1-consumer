package org.imd.kafka.sample1.consumer.service.strategy;

import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;

public interface ArbiterStrategy {
    public AuctionType getType();
    public boolean isWinningBid(ArbiterContext context);
    public boolean isBiddingStillAllowed(ArbiterContext context);
}
