package com.nova.star.domain;

import static com.nova.star.domain.CategoryTestSamples.*;
import static com.nova.star.domain.CommentTestSamples.*;
import static com.nova.star.domain.IdeaTestSamples.*;
import static com.nova.star.domain.RewardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class IdeaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Idea.class);
        Idea idea1 = getIdeaSample1();
        Idea idea2 = new Idea();
        assertThat(idea1).isNotEqualTo(idea2);

        idea2.setId(idea1.getId());
        assertThat(idea1).isEqualTo(idea2);

        idea2 = getIdeaSample2();
        assertThat(idea1).isNotEqualTo(idea2);
    }

    @Test
    void commentsTest() {
        Idea idea = getIdeaRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        idea.addComments(commentBack);
        assertThat(idea.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getIdea()).isEqualTo(idea);

        idea.removeComments(commentBack);
        assertThat(idea.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getIdea()).isNull();

        idea.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(idea.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getIdea()).isEqualTo(idea);

        idea.setComments(new HashSet<>());
        assertThat(idea.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getIdea()).isNull();
    }

    @Test
    void ideaCategoryTest() {
        Idea idea = getIdeaRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        idea.setIdeaCategory(categoryBack);
        assertThat(idea.getIdeaCategory()).isEqualTo(categoryBack);

        idea.ideaCategory(null);
        assertThat(idea.getIdeaCategory()).isNull();
    }

    @Test
    void assignedRewardTest() {
        Idea idea = getIdeaRandomSampleGenerator();
        Reward rewardBack = getRewardRandomSampleGenerator();

        idea.setAssignedReward(rewardBack);
        assertThat(idea.getAssignedReward()).isEqualTo(rewardBack);

        idea.assignedReward(null);
        assertThat(idea.getAssignedReward()).isNull();
    }

    @Test
    void categoryTest() {
        Idea idea = getIdeaRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        idea.setCategory(categoryBack);
        assertThat(idea.getCategory()).isEqualTo(categoryBack);

        idea.category(null);
        assertThat(idea.getCategory()).isNull();
    }

    @Test
    void rewardTest() {
        Idea idea = getIdeaRandomSampleGenerator();
        Reward rewardBack = getRewardRandomSampleGenerator();

        idea.setReward(rewardBack);
        assertThat(idea.getReward()).isEqualTo(rewardBack);

        idea.reward(null);
        assertThat(idea.getReward()).isNull();
    }
}
