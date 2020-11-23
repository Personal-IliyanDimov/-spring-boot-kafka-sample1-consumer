package org.imd.kafka.sample1.consumer.model.event;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionBidEvent {
    private Long auctionBidId;
    private Long auctionId;
    private Long userId;
    private BigDecimal bidPrice;
    private LocalDateTime bidDateTime;
}
