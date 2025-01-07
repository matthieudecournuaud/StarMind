package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GlobalChatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static GlobalChat getGlobalChatSample1() {
        return new GlobalChat().id(1L).name("name1");
    }

    public static GlobalChat getGlobalChatSample2() {
        return new GlobalChat().id(2L).name("name2");
    }

    public static GlobalChat getGlobalChatRandomSampleGenerator() {
        return new GlobalChat().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
