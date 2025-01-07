package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProspectEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProspectEntryDTO.class);
        ProspectEntryDTO prospectEntryDTO1 = new ProspectEntryDTO();
        prospectEntryDTO1.setId(1L);
        ProspectEntryDTO prospectEntryDTO2 = new ProspectEntryDTO();
        assertThat(prospectEntryDTO1).isNotEqualTo(prospectEntryDTO2);
        prospectEntryDTO2.setId(prospectEntryDTO1.getId());
        assertThat(prospectEntryDTO1).isEqualTo(prospectEntryDTO2);
        prospectEntryDTO2.setId(2L);
        assertThat(prospectEntryDTO1).isNotEqualTo(prospectEntryDTO2);
        prospectEntryDTO1.setId(null);
        assertThat(prospectEntryDTO1).isNotEqualTo(prospectEntryDTO2);
    }
}
