package com.nova.star.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Attachment getAttachmentSample1() {
        return new Attachment().id(1L).fileName("fileName1").fileType("fileType1");
    }

    public static Attachment getAttachmentSample2() {
        return new Attachment().id(2L).fileName("fileName2").fileType("fileType2");
    }

    public static Attachment getAttachmentRandomSampleGenerator() {
        return new Attachment()
            .id(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .fileType(UUID.randomUUID().toString());
    }
}
