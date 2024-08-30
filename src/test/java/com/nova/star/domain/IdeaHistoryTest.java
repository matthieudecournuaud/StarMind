package com.nova.star.domain;

import static com.nova.star.domain.IdeaHistoryTestSamples.*;
import static com.nova.star.domain.IdeaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdeaHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdeaHistory.class);
        IdeaHistory ideaHistory1 = getIdeaHistorySample1();
        IdeaHistory ideaHistory2 = new IdeaHistory();
        assertThat(ideaHistory1).isNotEqualTo(ideaHistory2);

        ideaHistory2.setId(ideaHistory1.getId());
        assertThat(ideaHistory1).isEqualTo(ideaHistory2);

        ideaHistory2 = getIdeaHistorySample2();
        assertThat(ideaHistory1).isNotEqualTo(ideaHistory2);
    }

    @Test
    void ideaTest() {
        IdeaHistory ideaHistory = getIdeaHistoryRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        ideaHistory.setIdea(ideaBack);
        assertThat(ideaHistory.getIdea()).isEqualTo(ideaBack);

        ideaHistory.idea(null);
        assertThat(ideaHistory.getIdea()).isNull();
    }
}
