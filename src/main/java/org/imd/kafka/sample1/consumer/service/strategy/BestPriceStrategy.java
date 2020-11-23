package org.imd.kafka.sample1.consumer.service.strategy;

import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;

public class BestPriceStrategy implements ArbiterStrategy {
    @Override
    public AuctionType getType() {
        return AuctionType.BEST_PRICE;
    }

    @Override
    public boolean isWinningBid(ArbiterContext context) {
        if (isBidAboveAuctionTarget(context)) {
            if (isArbiterInitialPrice(context)) {
                return true;
            } else if (isBetterBid(context)) {
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

    private boolean isBetterBid(ArbiterContext context) {
        final AuctionBidEvent arbiterBid = context.getArbiterData().getWinningBid();
        final AuctionBidEvent pretenderBid = context.getAuctionBidEvent();
        return (arbiterBid.getBidPrice().compareTo(pretenderBid.getBidPrice()) == -1);
    }

    @Override
    public boolean isBiddingStillAllowed(ArbiterContext context) {
        return true;
    }
}
