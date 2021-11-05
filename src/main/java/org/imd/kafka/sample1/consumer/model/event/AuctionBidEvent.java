package org.imd.kafka.sample1.consumer.model.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AuctionBidEvent {
    private String auctionBidId;
    private String auctionId;
    private String userId;
    private BigDecimal bidPrice;
    private LocalDateTime bidDateTime;
}
