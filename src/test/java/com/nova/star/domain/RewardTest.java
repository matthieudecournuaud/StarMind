package com.nova.star.domain;

import static com.nova.star.domain.IdeaTestSamples.*;
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
    void ideasTest() {
        Reward reward = getRewardRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        reward.addIdeas(ideaBack);
        assertThat(reward.getIdeas()).containsOnly(ideaBack);
        assertThat(ideaBack.getReward()).isEqualTo(reward);

        reward.removeIdeas(ideaBack);
        assertThat(reward.getIdeas()).doesNotContain(ideaBack);
        assertThat(ideaBack.getReward()).isNull();

        reward.ideas(new HashSet<>(Set.of(ideaBack)));
        assertThat(reward.getIdeas()).containsOnly(ideaBack);
        assertThat(ideaBack.getReward()).isEqualTo(reward);

        reward.setIdeas(new HashSet<>());
        assertThat(reward.getIdeas()).doesNotContain(ideaBack);
        assertThat(ideaBack.getReward()).isNull();
    }
}
