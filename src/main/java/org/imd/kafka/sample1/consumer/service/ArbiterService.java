package org.imd.kafka.sample1.consumer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
public class ArbiterService {
    private final ArbiterStore<ArbiterData<Long, AuctionEvent, AuctionBidEvent>, Long> arbiterStore;
    private final ConcurrentMap<AuctionType, ArbiterStrategy> strategyMap;

    public void processAuction(AuctionEvent auctionEvent) throws AuctionAlreadyExistsException {
        ArbiterData<Long, AuctionEvent, AuctionBidEvent> arbiterData =
            arbiterStore.findArbiterData(auctionEvent.getAuctionId());

        if (Objects.isNull(arbiterData)) {
            // prepare arbiter data
            arbiterData = new ArbiterData<>(auctionEvent.getAuctionId(), auctionEvent);
            arbiterData.setWinningBid(null);

            arbiterStore.saveArbiterData(auctionEvent.getAuctionId(), arbiterData);
        } else {
            throw new AuctionAlreadyExistsException(auctionEvent.getAuctionId());
        }

        // check strategy exists
        final ArbiterStrategy arbiterStrategy = strategyMap.get(auctionEvent.getAuctionType());
        if (arbiterStrategy == null) {
            throw new IllegalStateException("Unknown auction type or arbiter strategy: " + auctionEvent.getAuctionType());
        }
    }

    public void processAuctionBid(AuctionBidEvent auctionBidEvent) throws AuctionNotExistException, AuctionNotStartedException {

        // check arbiter data
        final ArbiterData<Long, AuctionEvent, AuctionBidEvent> arbiterData
            = arbiterStore.findArbiterData(auctionBidEvent.getAuctionId());
        if (arbiterData == null) {
            throw new AuctionNotExistException(auctionBidEvent.getAuctionId());
        }

        // auction is open
        final AuctionEvent auction = arbiterData.getAuction();
        if (auction.getStartDate().isAfter(auctionBidEvent.getBidDateTime())) {
            throw new AuctionNotStartedException(auction.getAuctionId(), auctionBidEvent.getAuctionBidId());
        }

        // choose strategy exists
        final ArbiterStrategy arbiterStrategy = strategyMap.get(auction.getAuctionType());
        if (arbiterStrategy == null) {
            throw new IllegalStateException("Unknown auction type or arbiter strategy: " + auction.getAuctionType());
        }

        // check if bid is a winning bid
        ArbiterContext context;
        do {
            final AuctionBidEvent bestBid = arbiterData.getWinningBid().get();
            context = new ArbiterContext(auction, bestBid, auctionBidEvent);
        }
        while ( arbiterStrategy.isBiddingStillAllowed(context) &&
                arbiterStrategy.isWinningBid(context) &&
                (! arbiterData.getWinningBid().compareAndSet(context.getBestBid(), auctionBidEvent)));
    }

    public void processAuctionFlush(AuctionFlushEvent afEvent) throws AuctionNotExistException {
        final ArbiterData<Long, AuctionEvent, AuctionBidEvent> arbiterData = arbiterStore.findArbiterData(afEvent.getAuctionId());
        if (arbiterData == null) {
            throw new AuctionNotExistException(afEvent.getAuctionId());
        }

        if (Boolean.TRUE.equals(afEvent.getRemove())) {
            arbiterStore.removeArbiterData(afEvent.getAuctionId());
        }

        System.out.println(arbiterData.toString());
    }
}
