package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdeaHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IdeaHistory getIdeaHistorySample1() {
        return new IdeaHistory().id(1L).action("action1").likes("likes1");
    }

    public static IdeaHistory getIdeaHistorySample2() {
        return new IdeaHistory().id(2L).action("action2").likes("likes2");
    }

    public static IdeaHistory getIdeaHistoryRandomSampleGenerator() {
        return new IdeaHistory().id(longCount.incrementAndGet()).action(UUID.randomUUID().toString()).likes(UUID.randomUUID().toString());
    }
}
