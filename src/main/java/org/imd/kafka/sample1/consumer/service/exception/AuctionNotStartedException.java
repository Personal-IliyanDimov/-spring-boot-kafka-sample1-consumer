package org.imd.kafka.sample1.consumer.service.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuctionNotStartedException extends Exception {
    private final String auctionId;
    private final String actionBiddingId;
}
