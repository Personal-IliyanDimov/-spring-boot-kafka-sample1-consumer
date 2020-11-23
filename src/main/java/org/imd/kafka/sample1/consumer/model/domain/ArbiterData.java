package org.imd.kafka.sample1.consumer.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArbiterData<K, A, B> {

    private K key;
    private A auction;
    private B winningBid;

}
