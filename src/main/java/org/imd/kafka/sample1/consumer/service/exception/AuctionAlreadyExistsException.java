package org.imd.kafka.sample1.consumer.service.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuctionAlreadyExistsException extends Exception {
    private final String auctionId;
}
