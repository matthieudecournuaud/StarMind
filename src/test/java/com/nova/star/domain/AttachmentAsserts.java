package com.nova.star.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AttachmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttachmentAllPropertiesEquals(Attachment expected, Attachment actual) {
        assertAttachmentAutoGeneratedPropertiesEquals(expected, actual);
        assertAttachmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttachmentAllUpdatablePropertiesEquals(Attachment expected, Attachment actual) {
        assertAttachmentUpdatableFieldsEquals(expected, actual);
        assertAttachmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttachmentAutoGeneratedPropertiesEquals(Attachment expected, Attachment actual) {
        assertThat(expected)
            .as("Verify Attachment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttachmentUpdatableFieldsEquals(Attachment expected, Attachment actual) {
        assertThat(expected)
            .as("Verify Attachment relevant properties")
            .satisfies(e -> assertThat(e.getFileName()).as("check fileName").isEqualTo(actual.getFileName()))
            .satisfies(e -> assertThat(e.getFileType()).as("check fileType").isEqualTo(actual.getFileType()))
            .satisfies(e -> assertThat(e.getData()).as("check data").isEqualTo(actual.getData()))
            .satisfies(e -> assertThat(e.getDataContentType()).as("check data contenty type").isEqualTo(actual.getDataContentType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttachmentUpdatableRelationshipsEquals(Attachment expected, Attachment actual) {
        assertThat(expected)
            .as("Verify Attachment relationships")
            .satisfies(e -> assertThat(e.getRelatedIdea()).as("check relatedIdea").isEqualTo(actual.getRelatedIdea()));
    }
}
