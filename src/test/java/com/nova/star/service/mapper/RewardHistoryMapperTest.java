package com.nova.star.service.mapper;

import static com.nova.star.domain.RewardHistoryAsserts.*;
import static com.nova.star.domain.RewardHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RewardHistoryMapperTest {

    private RewardHistoryMapper rewardHistoryMapper;

    @BeforeEach
    void setUp() {
        rewardHistoryMapper = new RewardHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRewardHistorySample1();
        var actual = rewardHistoryMapper.toEntity(rewardHistoryMapper.toDto(expected));
        assertRewardHistoryAllPropertiesEquals(expected, actual);
    }
}
