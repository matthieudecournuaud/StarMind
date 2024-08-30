package com.nova.star.domain;

import static com.nova.star.domain.IdeaTestSamples.*;
import static com.nova.star.domain.LikeHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeHistory.class);
        LikeHistory likeHistory1 = getLikeHistorySample1();
        LikeHistory likeHistory2 = new LikeHistory();
        assertThat(likeHistory1).isNotEqualTo(likeHistory2);

        likeHistory2.setId(likeHistory1.getId());
        assertThat(likeHistory1).isEqualTo(likeHistory2);

        likeHistory2 = getLikeHistorySample2();
        assertThat(likeHistory1).isNotEqualTo(likeHistory2);
    }

    @Test
    void ideaTest() {
        LikeHistory likeHistory = getLikeHistoryRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        likeHistory.setIdea(ideaBack);
        assertThat(likeHistory.getIdea()).isEqualTo(ideaBack);

        likeHistory.idea(null);
        assertThat(likeHistory.getIdea()).isNull();
    }
}
