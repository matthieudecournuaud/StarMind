package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GlobalChatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalChatDTO.class);
        GlobalChatDTO globalChatDTO1 = new GlobalChatDTO();
        globalChatDTO1.setId(1L);
        GlobalChatDTO globalChatDTO2 = new GlobalChatDTO();
        assertThat(globalChatDTO1).isNotEqualTo(globalChatDTO2);
        globalChatDTO2.setId(globalChatDTO1.getId());
        assertThat(globalChatDTO1).isEqualTo(globalChatDTO2);
        globalChatDTO2.setId(2L);
        assertThat(globalChatDTO1).isNotEqualTo(globalChatDTO2);
        globalChatDTO1.setId(null);
        assertThat(globalChatDTO1).isNotEqualTo(globalChatDTO2);
    }
}
