package org.imd.kafka.sample1.consumer.model.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuctionFlushEvent {
    private Long auctionId;
    private Boolean remove;
}
