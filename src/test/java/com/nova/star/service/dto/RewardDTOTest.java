package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RewardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RewardDTO.class);
        RewardDTO rewardDTO1 = new RewardDTO();
        rewardDTO1.setId(1L);
        RewardDTO rewardDTO2 = new RewardDTO();
        assertThat(rewardDTO1).isNotEqualTo(rewardDTO2);
        rewardDTO2.setId(rewardDTO1.getId());
        assertThat(rewardDTO1).isEqualTo(rewardDTO2);
        rewardDTO2.setId(2L);
        assertThat(rewardDTO1).isNotEqualTo(rewardDTO2);
        rewardDTO1.setId(null);
        assertThat(rewardDTO1).isNotEqualTo(rewardDTO2);
    }
}
