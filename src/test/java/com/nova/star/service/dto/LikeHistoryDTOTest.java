package com.nova.star.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeHistoryDTO.class);
        LikeHistoryDTO likeHistoryDTO1 = new LikeHistoryDTO();
        likeHistoryDTO1.setId(1L);
        LikeHistoryDTO likeHistoryDTO2 = new LikeHistoryDTO();
        assertThat(likeHistoryDTO1).isNotEqualTo(likeHistoryDTO2);
        likeHistoryDTO2.setId(likeHistoryDTO1.getId());
        assertThat(likeHistoryDTO1).isEqualTo(likeHistoryDTO2);
        likeHistoryDTO2.setId(2L);
        assertThat(likeHistoryDTO1).isNotEqualTo(likeHistoryDTO2);
        likeHistoryDTO1.setId(null);
        assertThat(likeHistoryDTO1).isNotEqualTo(likeHistoryDTO2);
    }
}
