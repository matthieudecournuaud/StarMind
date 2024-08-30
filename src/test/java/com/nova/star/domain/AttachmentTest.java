package com.nova.star.domain;

import static com.nova.star.domain.AttachmentTestSamples.*;
import static com.nova.star.domain.IdeaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attachment.class);
        Attachment attachment1 = getAttachmentSample1();
        Attachment attachment2 = new Attachment();
        assertThat(attachment1).isNotEqualTo(attachment2);

        attachment2.setId(attachment1.getId());
        assertThat(attachment1).isEqualTo(attachment2);

        attachment2 = getAttachmentSample2();
        assertThat(attachment1).isNotEqualTo(attachment2);
    }

    @Test
    void relatedIdeaTest() {
        Attachment attachment = getAttachmentRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        attachment.setRelatedIdea(ideaBack);
        assertThat(attachment.getRelatedIdea()).isEqualTo(ideaBack);

        attachment.relatedIdea(null);
        assertThat(attachment.getRelatedIdea()).isNull();
    }
}
