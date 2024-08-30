package com.nova.star.domain;

import static com.nova.star.domain.CommentTestSamples.*;
import static com.nova.star.domain.IdeaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comment.class);
        Comment comment1 = getCommentSample1();
        Comment comment2 = new Comment();
        assertThat(comment1).isNotEqualTo(comment2);

        comment2.setId(comment1.getId());
        assertThat(comment1).isEqualTo(comment2);

        comment2 = getCommentSample2();
        assertThat(comment1).isNotEqualTo(comment2);
    }

    @Test
    void relatedIdeaTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        comment.setRelatedIdea(ideaBack);
        assertThat(comment.getRelatedIdea()).isEqualTo(ideaBack);

        comment.relatedIdea(null);
        assertThat(comment.getRelatedIdea()).isNull();
    }

    @Test
    void ideaTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        comment.setIdea(ideaBack);
        assertThat(comment.getIdea()).isEqualTo(ideaBack);

        comment.idea(null);
        assertThat(comment.getIdea()).isNull();
    }
}
