package com.nova.star.domain;

import static com.nova.star.domain.IdeaTestSamples.*;
import static com.nova.star.domain.RewardHistoryTestSamples.*;
import static com.nova.star.domain.RewardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RewardHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RewardHistory.class);
        RewardHistory rewardHistory1 = getRewardHistorySample1();
        RewardHistory rewardHistory2 = new RewardHistory();
        assertThat(rewardHistory1).isNotEqualTo(rewardHistory2);

        rewardHistory2.setId(rewardHistory1.getId());
        assertThat(rewardHistory1).isEqualTo(rewardHistory2);

        rewardHistory2 = getRewardHistorySample2();
        assertThat(rewardHistory1).isNotEqualTo(rewardHistory2);
    }

    @Test
    void rewardTest() {
        RewardHistory rewardHistory = getRewardHistoryRandomSampleGenerator();
        Reward rewardBack = getRewardRandomSampleGenerator();

        rewardHistory.setReward(rewardBack);
        assertThat(rewardHistory.getReward()).isEqualTo(rewardBack);

        rewardHistory.reward(null);
        assertThat(rewardHistory.getReward()).isNull();
    }

    @Test
    void ideaTest() {
        RewardHistory rewardHistory = getRewardHistoryRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        rewardHistory.setIdea(ideaBack);
        assertThat(rewardHistory.getIdea()).isEqualTo(ideaBack);

        rewardHistory.idea(null);
        assertThat(rewardHistory.getIdea()).isNull();
    }
}
