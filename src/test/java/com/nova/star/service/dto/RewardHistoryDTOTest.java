package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RewardHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RewardHistoryDTO.class);
        RewardHistoryDTO rewardHistoryDTO1 = new RewardHistoryDTO();
        rewardHistoryDTO1.setId(1L);
        RewardHistoryDTO rewardHistoryDTO2 = new RewardHistoryDTO();
        assertThat(rewardHistoryDTO1).isNotEqualTo(rewardHistoryDTO2);
        rewardHistoryDTO2.setId(rewardHistoryDTO1.getId());
        assertThat(rewardHistoryDTO1).isEqualTo(rewardHistoryDTO2);
        rewardHistoryDTO2.setId(2L);
        assertThat(rewardHistoryDTO1).isNotEqualTo(rewardHistoryDTO2);
        rewardHistoryDTO1.setId(null);
        assertThat(rewardHistoryDTO1).isNotEqualTo(rewardHistoryDTO2);
    }
}
