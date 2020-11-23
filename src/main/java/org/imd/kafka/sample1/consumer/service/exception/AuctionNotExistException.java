package org.imd.kafka.sample1.consumer.service.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuctionNotExistException extends Exception {
    private final Long auctionId;
}
