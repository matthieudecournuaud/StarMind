package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RewardHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RewardHistory getRewardHistorySample1() {
        return new RewardHistory().id(1L).action("action1");
    }

    public static RewardHistory getRewardHistorySample2() {
        return new RewardHistory().id(2L).action("action2");
    }

    public static RewardHistory getRewardHistoryRandomSampleGenerator() {
        return new RewardHistory().id(longCount.incrementAndGet()).action(UUID.randomUUID().toString());
    }
}
