package org.imd.kafka.sample1.consumer.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class ArbiterData<K, A, B> {

    private final K key;
    private final A auction;
    private final AtomicReference<B> winningBid;

}
