package org.imd.kafka.sample1.consumer.service.strategy;

import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;
import org.springframework.stereotype.Component;

@Component
public class HitFirstTheTargetStrategy implements ArbiterStrategy {

    @Override
    public AuctionType getType() {
        return AuctionType.HIT_FIRST_THE_TARGET;
    }

    @Override
    public boolean isWinningBid(ArbiterContext context) {
        final AuctionEvent auction = context.getAuction();
        final AuctionBidEvent bestBid = context.getBestBid();
        final AuctionBidEvent pretenderBid = context.getPretenderBid();

        return isInitialBid(bestBid, pretenderBid) || isBetterBid(auction, bestBid, pretenderBid) ;
    }

    private boolean isBetterBid(AuctionEvent auction, AuctionBidEvent bestBid, AuctionBidEvent pretenderBid) {
        boolean result = false;

        if (targetIsNotReached(auction, bestBid)) {
            result = pretenderIsGt(bestBid, pretenderBid);
        }
        else {
            result = happensBeforeBestBid(bestBid, pretenderBid) && (pretenderIsGt(bestBid, pretenderBid));
        }

        return result;
    }

    private boolean pretenderIsGt(AuctionBidEvent bestBid, AuctionBidEvent pretenderBid) {
        return bestBid.getBidPrice().compareTo(pretenderBid.getBidPrice()) == -1;
    }


    @Override
    public boolean isBiddingStillAllowed(ArbiterContext context) {
        final AuctionEvent auction = context.getAuction();
        final AuctionBidEvent bestBid = context.getBestBid();
        final AuctionBidEvent pretenderBid = context.getPretenderBid();

        return isInitialBid(bestBid, pretenderBid) ||
               targetIsNotReached(auction, bestBid) ||
               happensBeforeBestBid(bestBid, pretenderBid);
    }

    private boolean isInitialBid(AuctionBidEvent bestBid, AuctionBidEvent pretenderBid) {
        return (bestBid == null) && (pretenderBid != null);
    }

    private boolean targetIsNotReached(AuctionEvent auction, AuctionBidEvent bestBid) {
        return (bestBid.getBidPrice().compareTo(auction.getTargetPrice()) == -1);
    }

    private boolean happensBeforeBestBid(AuctionBidEvent bestBid, AuctionBidEvent pretenderBid) {
        return (pretenderBid.getBidDateTime().compareTo(bestBid.getBidDateTime()) == -1);
    }
}
