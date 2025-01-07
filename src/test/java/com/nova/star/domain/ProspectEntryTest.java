package com.nova.star.domain;

import static com.nova.star.domain.ProspectBoardTestSamples.*;
import static com.nova.star.domain.ProspectEntryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProspectEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProspectEntry.class);
        ProspectEntry prospectEntry1 = getProspectEntrySample1();
        ProspectEntry prospectEntry2 = new ProspectEntry();
        assertThat(prospectEntry1).isNotEqualTo(prospectEntry2);

        prospectEntry2.setId(prospectEntry1.getId());
        assertThat(prospectEntry1).isEqualTo(prospectEntry2);

        prospectEntry2 = getProspectEntrySample2();
        assertThat(prospectEntry1).isNotEqualTo(prospectEntry2);
    }

    @Test
    void prospectBoardTest() {
        ProspectEntry prospectEntry = getProspectEntryRandomSampleGenerator();
        ProspectBoard prospectBoardBack = getProspectBoardRandomSampleGenerator();

        prospectEntry.setProspectBoard(prospectBoardBack);
        assertThat(prospectEntry.getProspectBoard()).isEqualTo(prospectBoardBack);

        prospectEntry.prospectBoard(null);
        assertThat(prospectEntry.getProspectBoard()).isNull();
    }
}
