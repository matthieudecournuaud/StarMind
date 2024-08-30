package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RewardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reward getRewardSample1() {
        return new Reward().id(1L).name("name1").description("description1");
    }

    public static Reward getRewardSample2() {
        return new Reward().id(2L).name("name2").description("description2");
    }

    public static Reward getRewardRandomSampleGenerator() {
        return new Reward().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
