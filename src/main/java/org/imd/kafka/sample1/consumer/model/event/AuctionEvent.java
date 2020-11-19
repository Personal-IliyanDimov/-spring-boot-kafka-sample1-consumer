package org.imd.kafka.sample1.consumer.model.event;

import lombok.Getter;
import lombok.Setter;
import org.imd.kafka.sample1.consumer.model.event.type.AuctionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionEvent {
    private Long auctionId;
    private AuctionType auctionType;
    private Long itemId;
    private BigDecimal targetPrice;
    private LocalDateTime startDate;
}
