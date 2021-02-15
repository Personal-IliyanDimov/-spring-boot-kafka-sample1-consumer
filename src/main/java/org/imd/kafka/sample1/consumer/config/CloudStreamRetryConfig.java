package org.imd.kafka.sample1.consumer.config;

import org.imd.kafka.sample1.consumer.service.exception.AuctionAlreadyExistsException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotExistException;
import org.imd.kafka.sample1.consumer.service.exception.AuctionNotStartedException;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudStreamRetryConfig {

    @StreamRetryTemplate()
    public RetryTemplate auctionRetryTemplate() {
        final Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(AuctionAlreadyExistsException.class, Boolean.FALSE);
        retryableExceptions.put(IllegalStateException.class, Boolean.FALSE);
        retryableExceptions.put(Exception.class, Boolean.TRUE);
        retryableExceptions.put(RuntimeException.class, Boolean.TRUE);

        final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, retryableExceptions);
        final CircuitBreakerRetryPolicy cbRetryPolicy = new CircuitBreakerRetryPolicy(simpleRetryPolicy);

        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(cbRetryPolicy);

        return retryTemplate;
    }

    @StreamRetryTemplate()
    public RetryTemplate auctionBidRetryTemplate() {
        final Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(AuctionNotExistException.class, Boolean.FALSE);
        retryableExceptions.put(AuctionNotStartedException.class, Boolean.FALSE);
        retryableExceptions.put(IllegalStateException.class, Boolean.FALSE);
        retryableExceptions.put(Exception.class, Boolean.TRUE);
        retryableExceptions.put(RuntimeException.class, Boolean.TRUE);

        final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, retryableExceptions);
        final CircuitBreakerRetryPolicy cbRetryPolicy = new CircuitBreakerRetryPolicy(simpleRetryPolicy);

        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(cbRetryPolicy);

        return retryTemplate;
    }

    @StreamRetryTemplate()
    public RetryTemplate auctionFlushRetryTemplate() {
        final Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(AuctionNotExistException.class, Boolean.FALSE);
        retryableExceptions.put(Exception.class, Boolean.TRUE);
        retryableExceptions.put(RuntimeException.class, Boolean.TRUE);

        final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, retryableExceptions);
        final CircuitBreakerRetryPolicy cbRetryPolicy = new CircuitBreakerRetryPolicy(simpleRetryPolicy);

        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(cbRetryPolicy);

        return retryTemplate;
    }
}
