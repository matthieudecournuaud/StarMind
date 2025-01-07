package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdeaChatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdeaChatDTO.class);
        IdeaChatDTO ideaChatDTO1 = new IdeaChatDTO();
        ideaChatDTO1.setId(1L);
        IdeaChatDTO ideaChatDTO2 = new IdeaChatDTO();
        assertThat(ideaChatDTO1).isNotEqualTo(ideaChatDTO2);
        ideaChatDTO2.setId(ideaChatDTO1.getId());
        assertThat(ideaChatDTO1).isEqualTo(ideaChatDTO2);
        ideaChatDTO2.setId(2L);
        assertThat(ideaChatDTO1).isNotEqualTo(ideaChatDTO2);
        ideaChatDTO1.setId(null);
        assertThat(ideaChatDTO1).isNotEqualTo(ideaChatDTO2);
    }
}
