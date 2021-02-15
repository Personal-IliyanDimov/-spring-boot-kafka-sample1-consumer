package org.imd.kafka.sample1.consumer.service;

import lombok.RequiredArgsConstructor;
import org.imd.kafka.sample1.consumer.model.domain.ArbiterData;
import org.imd.kafka.sample1.consumer.model.event.AuctionBidEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.model.event.AuctionFlushEvent;
import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;
import org.imd.kafka.sample1.consumer.service.exception.AuctionAlreadyExistsException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotExistException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotStartedException;
import org.imd.kafka.sample1.consumer.service.strategy.ArbiterContext;
import org.imd.kafka.sample1.consumer.service.strategy.ArbiterStrategy;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
public class ArbiterService {
    private final ArbiterStore<ArbiterData<Long, AuctionEvent, AuctionBidEvent>, Long> arbiterStore;
    private final AuctionStore<AuctionEvent, Long> auctionStore;
    private final ConcurrentMap<AuctionType, ArbiterStrategy> strategyMap;

    public void processAuction(AuctionEvent auctionEvent) throws AuctionAlreadyExistsException {
        final AuctionEvent existingAuctionEvent = auctionStore.findAuction(auctionEvent.getAuctionId());
        if (Objects.nonNull(existingAuctionEvent)) {
            throw new AuctionAlreadyExistsException(auctionEvent.getAuctionId());
        }

        // check strategy
        final ArbiterStrategy arbiterStrategy = strategyMap.get(auctionEvent.getAuctionType());
        if (arbiterStrategy == null) {
            throw new IllegalStateException("Unknown auction type or arbiter strategy: " + auctionEvent.getAuctionType());
        }

        auctionStore.saveAuction(auctionEvent.getAuctionId(), auctionEvent);
    }

    public void processAuctionBid(AuctionBidEvent auctionBidEvent) throws AuctionNotExistException, AuctionNotStartedException {
        // auction exists
        final AuctionEvent auction = auctionStore.findAuction(auctionBidEvent.getAuctionId());
        if (auction == null) {
            throw new AuctionNotExistException(auctionBidEvent.getAuctionId());
        }

        // create initial arbiter data
        ArbiterData<Long, AuctionEvent, AuctionBidEvent> arbiterData
            = arbiterStore.findArbiterData(auctionBidEvent.getAuctionId());
        if (arbiterData == null) {
            arbiterData = new ArbiterData<>();
            arbiterData.setKey(auction.getAuctionId());
            arbiterData.setAuction(auction);
            arbiterData.setWinningBid(null);
        }

        // auction is open
        if (auction.getStartDate().isAfter(auctionBidEvent.getBidDateTime())) {
            throw new AuctionNotStartedException(auction.getAuctionId(), auctionBidEvent.getAuctionBidId());
        }

        // choose strategy
        final ArbiterStrategy arbiterStrategy = strategyMap.get(auction.getAuctionType());
        if (arbiterStrategy == null) {
            throw new IllegalStateException("Unknown auction type or arbiter strategy: " + auction.getAuctionType());
        }

        // check if bid is a winning bid
        final ArbiterContext context = new ArbiterContext(arbiterData, auctionBidEvent);
        if (arbiterStrategy.isBiddingStillAllowed(context) && arbiterStrategy.isWinningBid(context)) {
            arbiterData.setWinningBid(auctionBidEvent);
            arbiterStore.saveArbiterData(arbiterData.getKey(), arbiterData);
        }
    }

    public void processAuctionFlush(AuctionFlushEvent afEvent) throws AuctionNotExistException {
        final ArbiterData<Long, AuctionEvent, AuctionBidEvent> arbiterData = arbiterStore.findArbiterData(afEvent.getAuctionId());
        if (arbiterData == null) {
            throw new AuctionNotExistException(afEvent.getAuctionId());
        }
        auctionStore.remove(afEvent.getAuctionId());
        arbiterStore.removeArbiterData(afEvent.getAuctionId());

        System.out.println(arbiterData.toString());
    }
}
