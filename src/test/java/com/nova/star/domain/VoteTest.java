package com.nova.star.domain;

import static com.nova.star.domain.IdeaTestSamples.*;
import static com.nova.star.domain.VoteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vote.class);
        Vote vote1 = getVoteSample1();
        Vote vote2 = new Vote();
        assertThat(vote1).isNotEqualTo(vote2);

        vote2.setId(vote1.getId());
        assertThat(vote1).isEqualTo(vote2);

        vote2 = getVoteSample2();
        assertThat(vote1).isNotEqualTo(vote2);
    }

    @Test
    void ideaTest() {
        Vote vote = getVoteRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        vote.setIdea(ideaBack);
        assertThat(vote.getIdea()).isEqualTo(ideaBack);

        vote.idea(null);
        assertThat(vote.getIdea()).isNull();
    }
}
