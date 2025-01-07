package com.nova.star.domain;

import static com.nova.star.domain.CategoryTestSamples.*;
import static com.nova.star.domain.CommentTestSamples.*;
import static com.nova.star.domain.IdeaTestSamples.*;
import static com.nova.star.domain.LikeHistoryTestSamples.*;
import static com.nova.star.domain.RewardTestSamples.*;
import static com.nova.star.domain.VoteTestSamples.*;
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
    void votesTest() {
        Idea idea = getIdeaRandomSampleGenerator();
        Vote voteBack = getVoteRandomSampleGenerator();

        idea.addVotes(voteBack);
        assertThat(idea.getVotes()).containsOnly(voteBack);
        assertThat(voteBack.getIdea()).isEqualTo(idea);

        idea.removeVotes(voteBack);
        assertThat(idea.getVotes()).doesNotContain(voteBack);
        assertThat(voteBack.getIdea()).isNull();

        idea.votes(new HashSet<>(Set.of(voteBack)));
        assertThat(idea.getVotes()).containsOnly(voteBack);
        assertThat(voteBack.getIdea()).isEqualTo(idea);

        idea.setVotes(new HashSet<>());
        assertThat(idea.getVotes()).doesNotContain(voteBack);
        assertThat(voteBack.getIdea()).isNull();
    }

    @Test
    void likeHistoriesTest() {
        Idea idea = getIdeaRandomSampleGenerator();
        LikeHistory likeHistoryBack = getLikeHistoryRandomSampleGenerator();

        idea.addLikeHistories(likeHistoryBack);
        assertThat(idea.getLikeHistories()).containsOnly(likeHistoryBack);
        assertThat(likeHistoryBack.getIdea()).isEqualTo(idea);

        idea.removeLikeHistories(likeHistoryBack);
        assertThat(idea.getLikeHistories()).doesNotContain(likeHistoryBack);
        assertThat(likeHistoryBack.getIdea()).isNull();

        idea.likeHistories(new HashSet<>(Set.of(likeHistoryBack)));
        assertThat(idea.getLikeHistories()).containsOnly(likeHistoryBack);
        assertThat(likeHistoryBack.getIdea()).isEqualTo(idea);

        idea.setLikeHistories(new HashSet<>());
        assertThat(idea.getLikeHistories()).doesNotContain(likeHistoryBack);
        assertThat(likeHistoryBack.getIdea()).isNull();
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
}
