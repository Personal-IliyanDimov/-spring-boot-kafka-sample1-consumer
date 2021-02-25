package org.imd.kafka.sample1.consumer.service.strategy;

import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;
import org.springframework.stereotype.Component;

@Component
public class BestPriceStrategy implements ArbiterStrategy {

    @Override
    public AuctionType getType() {
        return AuctionType.BEST_PRICE;
    }

    @Override
    public boolean isWinningBid(ArbiterContext context) {
        return isBetterBid(context);
    }

    private boolean isBetterBid(ArbiterContext context) {
        final AuctionBidEvent bestBid = context.getBestBid();
        final AuctionBidEvent pretenderBid = context.getPretenderBid();
        return isInitialBid(bestBid, pretenderBid) || isGreaterBid(bestBid, pretenderBid);
    }
    private boolean isInitialBid(AuctionBidEvent bestBid, AuctionBidEvent pretenderBid) {
        return (bestBid == null) && (pretenderBid != null);
    }

    private boolean isGreaterBid(AuctionBidEvent bestBid, AuctionBidEvent pretenderBid) {
        return bestBid.getBidPrice().compareTo(pretenderBid.getBidPrice()) == -1;
    }

    @Override
    public boolean isBiddingStillAllowed(ArbiterContext context) {
        return true;
    }
}
