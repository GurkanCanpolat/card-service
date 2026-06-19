package com.gurkan.card_service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TransactionMetrics {
    private static final String METRIC_NAME = "card.transactions.processed";

    private final Counter approvedCounter;
    private final Counter declinedCounter;
    private final Counter reversedCounter;

    public TransactionMetrics(MeterRegistry meterRegistry) {
        this.approvedCounter = createCounter(meterRegistry, "APPROVED");
        this.declinedCounter = createCounter(meterRegistry, "DECLINED");
        this.reversedCounter = createCounter(meterRegistry, "REVERSED");
    }

    private Counter createCounter(MeterRegistry meterRegistry, String status) {
        return Counter.builder(METRIC_NAME)
                .description("Number of card transactions grouped by business status")
                .tag("status", status)
                .register(meterRegistry);
    }

    public void recordApproved() {
        approvedCounter.increment();
    }

    public void recordDeclined() {
        declinedCounter.increment();
    }

    public void recordReversed() {
        reversedCounter.increment();
    }
}
