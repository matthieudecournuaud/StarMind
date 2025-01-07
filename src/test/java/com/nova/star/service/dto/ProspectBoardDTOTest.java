package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProspectBoardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProspectBoardDTO.class);
        ProspectBoardDTO prospectBoardDTO1 = new ProspectBoardDTO();
        prospectBoardDTO1.setId(1L);
        ProspectBoardDTO prospectBoardDTO2 = new ProspectBoardDTO();
        assertThat(prospectBoardDTO1).isNotEqualTo(prospectBoardDTO2);
        prospectBoardDTO2.setId(prospectBoardDTO1.getId());
        assertThat(prospectBoardDTO1).isEqualTo(prospectBoardDTO2);
        prospectBoardDTO2.setId(2L);
        assertThat(prospectBoardDTO1).isNotEqualTo(prospectBoardDTO2);
        prospectBoardDTO1.setId(null);
        assertThat(prospectBoardDTO1).isNotEqualTo(prospectBoardDTO2);
    }
}
