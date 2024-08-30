package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LikeHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LikeHistory getLikeHistorySample1() {
        return new LikeHistory().id(1L).action("action1").oldLikes("oldLikes1").newLikes("newLikes1");
    }

    public static LikeHistory getLikeHistorySample2() {
        return new LikeHistory().id(2L).action("action2").oldLikes("oldLikes2").newLikes("newLikes2");
    }

    public static LikeHistory getLikeHistoryRandomSampleGenerator() {
        return new LikeHistory()
            .id(longCount.incrementAndGet())
            .action(UUID.randomUUID().toString())
            .oldLikes(UUID.randomUUID().toString())
            .newLikes(UUID.randomUUID().toString());
    }
}
