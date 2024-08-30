package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdeaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Idea getIdeaSample1() {
        return new Idea().id(1L).title("title1").likes("likes1");
    }

    public static Idea getIdeaSample2() {
        return new Idea().id(2L).title("title2").likes("likes2");
    }

    public static Idea getIdeaRandomSampleGenerator() {
        return new Idea().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).likes(UUID.randomUUID().toString());
    }
}
