package com.nova.star.domain;

import static com.nova.star.domain.ProspectBoardTestSamples.*;
import static com.nova.star.domain.ProspectEntryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProspectBoardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProspectBoard.class);
        ProspectBoard prospectBoard1 = getProspectBoardSample1();
        ProspectBoard prospectBoard2 = new ProspectBoard();
        assertThat(prospectBoard1).isNotEqualTo(prospectBoard2);

        prospectBoard2.setId(prospectBoard1.getId());
        assertThat(prospectBoard1).isEqualTo(prospectBoard2);

        prospectBoard2 = getProspectBoardSample2();
        assertThat(prospectBoard1).isNotEqualTo(prospectBoard2);
    }

    @Test
    void entriesTest() {
        ProspectBoard prospectBoard = getProspectBoardRandomSampleGenerator();
        ProspectEntry prospectEntryBack = getProspectEntryRandomSampleGenerator();

        prospectBoard.addEntries(prospectEntryBack);
        assertThat(prospectBoard.getEntries()).containsOnly(prospectEntryBack);
        assertThat(prospectEntryBack.getProspectBoard()).isEqualTo(prospectBoard);

        prospectBoard.removeEntries(prospectEntryBack);
        assertThat(prospectBoard.getEntries()).doesNotContain(prospectEntryBack);
        assertThat(prospectEntryBack.getProspectBoard()).isNull();

        prospectBoard.entries(new HashSet<>(Set.of(prospectEntryBack)));
        assertThat(prospectBoard.getEntries()).containsOnly(prospectEntryBack);
        assertThat(prospectEntryBack.getProspectBoard()).isEqualTo(prospectBoard);

        prospectBoard.setEntries(new HashSet<>());
        assertThat(prospectBoard.getEntries()).doesNotContain(prospectEntryBack);
        assertThat(prospectEntryBack.getProspectBoard()).isNull();
    }
}
