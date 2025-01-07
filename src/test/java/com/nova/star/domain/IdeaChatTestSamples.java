package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdeaChatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IdeaChat getIdeaChatSample1() {
        return new IdeaChat().id(1L).name("name1");
    }

    public static IdeaChat getIdeaChatSample2() {
        return new IdeaChat().id(2L).name("name2");
    }

    public static IdeaChat getIdeaChatRandomSampleGenerator() {
        return new IdeaChat().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
