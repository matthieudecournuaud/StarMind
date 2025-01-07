package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IdeaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Idea getIdeaSample1() {
        return new Idea().id(1L).title("title1").likes(1).impact("impact1");
    }

    public static Idea getIdeaSample2() {
        return new Idea().id(2L).title("title2").likes(2).impact("impact2");
    }

    public static Idea getIdeaRandomSampleGenerator() {
        return new Idea()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .likes(intCount.incrementAndGet())
            .impact(UUID.randomUUID().toString());
    }
}
