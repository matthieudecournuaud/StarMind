package com.nova.star.domain;

import static com.nova.star.domain.RewardHistoryTestSamples.*;
import static com.nova.star.domain.RewardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RewardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reward.class);
        Reward reward1 = getRewardSample1();
        Reward reward2 = new Reward();
        assertThat(reward1).isNotEqualTo(reward2);

        reward2.setId(reward1.getId());
        assertThat(reward1).isEqualTo(reward2);

        reward2 = getRewardSample2();
        assertThat(reward1).isNotEqualTo(reward2);
    }

    @Test
    void rewardHistoriesTest() {
        Reward reward = getRewardRandomSampleGenerator();
        RewardHistory rewardHistoryBack = getRewardHistoryRandomSampleGenerator();

        reward.addRewardHistories(rewardHistoryBack);
        assertThat(reward.getRewardHistories()).containsOnly(rewardHistoryBack);
        assertThat(rewardHistoryBack.getReward()).isEqualTo(reward);

        reward.removeRewardHistories(rewardHistoryBack);
        assertThat(reward.getRewardHistories()).doesNotContain(rewardHistoryBack);
        assertThat(rewardHistoryBack.getReward()).isNull();

        reward.rewardHistories(new HashSet<>(Set.of(rewardHistoryBack)));
        assertThat(reward.getRewardHistories()).containsOnly(rewardHistoryBack);
        assertThat(rewardHistoryBack.getReward()).isEqualTo(reward);

        reward.setRewardHistories(new HashSet<>());
        assertThat(reward.getRewardHistories()).doesNotContain(rewardHistoryBack);
        assertThat(rewardHistoryBack.getReward()).isNull();
    }
}
