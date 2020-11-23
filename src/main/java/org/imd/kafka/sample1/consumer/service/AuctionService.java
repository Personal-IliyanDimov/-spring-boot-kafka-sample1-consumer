package org.imd.kafka.sample1.consumer.service;

import lombok.RequiredArgsConstructor;
import org.imd.kafka.sample1.consumer.model.event.AuctionEvent;
import org.imd.kafka.sample1.consumer.service.exception.AuctionAlreadyExistsException;

import java.util.Objects;

@RequiredArgsConstructor
public class AuctionService {
    private final AuctionStore<AuctionEvent, Long> auctionStore;

    public void processAuction(AuctionEvent auctionEvent) throws AuctionAlreadyExistsException {
        final AuctionEvent existingAuctionEvent = auctionStore.findAuction(auctionEvent.getAuctionId());
        if (Objects.nonNull(existingAuctionEvent)) {
            throw new AuctionAlreadyExistsException(auctionEvent.getAuctionId());
        }

        auctionStore.saveAuction(auctionEvent.getAuctionId(), auctionEvent);
    }
}
