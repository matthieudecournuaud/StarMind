package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdeaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdeaDTO.class);
        IdeaDTO ideaDTO1 = new IdeaDTO();
        ideaDTO1.setId(1L);
        IdeaDTO ideaDTO2 = new IdeaDTO();
        assertThat(ideaDTO1).isNotEqualTo(ideaDTO2);
        ideaDTO2.setId(ideaDTO1.getId());
        assertThat(ideaDTO1).isEqualTo(ideaDTO2);
        ideaDTO2.setId(2L);
        assertThat(ideaDTO1).isNotEqualTo(ideaDTO2);
        ideaDTO1.setId(null);
        assertThat(ideaDTO1).isNotEqualTo(ideaDTO2);
    }
}
