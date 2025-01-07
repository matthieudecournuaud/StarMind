package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdeaHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdeaHistoryDTO.class);
        IdeaHistoryDTO ideaHistoryDTO1 = new IdeaHistoryDTO();
        ideaHistoryDTO1.setId(1L);
        IdeaHistoryDTO ideaHistoryDTO2 = new IdeaHistoryDTO();
        assertThat(ideaHistoryDTO1).isNotEqualTo(ideaHistoryDTO2);
        ideaHistoryDTO2.setId(ideaHistoryDTO1.getId());
        assertThat(ideaHistoryDTO1).isEqualTo(ideaHistoryDTO2);
        ideaHistoryDTO2.setId(2L);
        assertThat(ideaHistoryDTO1).isNotEqualTo(ideaHistoryDTO2);
        ideaHistoryDTO1.setId(null);
        assertThat(ideaHistoryDTO1).isNotEqualTo(ideaHistoryDTO2);
    }
}
