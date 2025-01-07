package com.nova.star.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProspectEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProspectEntry getProspectEntrySample1() {
        return new ProspectEntry().id(1L);
    }

    public static ProspectEntry getProspectEntrySample2() {
        return new ProspectEntry().id(2L);
    }

    public static ProspectEntry getProspectEntryRandomSampleGenerator() {
        return new ProspectEntry().id(longCount.incrementAndGet());
    }
}
