package com.nova.star.domain;

import static com.nova.star.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class IdeaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIdeaAllPropertiesEquals(Idea expected, Idea actual) {
        assertIdeaAutoGeneratedPropertiesEquals(expected, actual);
        assertIdeaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIdeaAllUpdatablePropertiesEquals(Idea expected, Idea actual) {
        assertIdeaUpdatableFieldsEquals(expected, actual);
        assertIdeaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIdeaAutoGeneratedPropertiesEquals(Idea expected, Idea actual) {
        assertThat(expected)
            .as("Verify Idea auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIdeaUpdatableFieldsEquals(Idea expected, Idea actual) {
        assertThat(expected)
            .as("Verify Idea relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getValidation()).as("check validation").isEqualTo(actual.getValidation()))
            .satisfies(e -> assertThat(e.getRewardType()).as("check rewardType").isEqualTo(actual.getRewardType()))
            .satisfies(e -> assertThat(e.getLikes()).as("check likes").isEqualTo(actual.getLikes()))
            .satisfies(e ->
                assertThat(e.getCreatedDate())
                    .as("check createdDate")
                    .usingComparator(zonedDataTimeSameInstant)
                    .isEqualTo(actual.getCreatedDate())
            )
            .satisfies(e ->
                assertThat(e.getModifiedDate())
                    .as("check modifiedDate")
                    .usingComparator(zonedDataTimeSameInstant)
                    .isEqualTo(actual.getModifiedDate())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIdeaUpdatableRelationshipsEquals(Idea expected, Idea actual) {
        assertThat(expected)
            .as("Verify Idea relationships")
            .satisfies(e -> assertThat(e.getIdeaCategory()).as("check ideaCategory").isEqualTo(actual.getIdeaCategory()))
            .satisfies(e -> assertThat(e.getAssignedReward()).as("check assignedReward").isEqualTo(actual.getAssignedReward()))
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()))
            .satisfies(e -> assertThat(e.getReward()).as("check reward").isEqualTo(actual.getReward()));
    }
}
