package org.imd.kafka.sample1.consumer.service.strategy;

import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;
import org.springframework.stereotype.Component;

@Component
public class HitTheTargetStrategy implements ArbiterStrategy {

    @Override
    public AuctionType getType() {
        return AuctionType.HIT_THE_TARGET;
    }

    @Override
    public boolean isWinningBid(ArbiterContext context) {
        if (isBidAboveAuctionTarget(context)) {
            if (isArbiterInitialPrice(context)) {
                return true;
            }
        }

        return false;
    }

    private boolean isBidAboveAuctionTarget(ArbiterContext context) {
        final AuctionEvent auction = context.getArbiterData().getAuction();
        final AuctionBidEvent auctionBidEvent = context.getAuctionBidEvent();

        return (auction.getTargetPrice().compareTo(auctionBidEvent.getBidPrice()) < 1);
    }

    private boolean isArbiterInitialPrice(ArbiterContext context) {
        return context.getArbiterData().getWinningBid() == null;
    }

    @Override
    public boolean isBiddingStillAllowed(ArbiterContext context) {
        return isArbiterInitialPrice(context);
    }
}
