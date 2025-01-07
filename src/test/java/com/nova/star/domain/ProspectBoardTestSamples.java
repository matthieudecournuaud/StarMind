package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProspectBoardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProspectBoard getProspectBoardSample1() {
        return new ProspectBoard().id(1L).name("name1");
    }

    public static ProspectBoard getProspectBoardSample2() {
        return new ProspectBoard().id(2L).name("name2");
    }

    public static ProspectBoard getProspectBoardRandomSampleGenerator() {
        return new ProspectBoard().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
