package com.nova.star.service.mapper;

import static com.nova.star.domain.LikeHistoryAsserts.*;
import static com.nova.star.domain.LikeHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikeHistoryMapperTest {

    private LikeHistoryMapper likeHistoryMapper;

    @BeforeEach
    void setUp() {
        likeHistoryMapper = new LikeHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLikeHistorySample1();
        var actual = likeHistoryMapper.toEntity(likeHistoryMapper.toDto(expected));
        assertLikeHistoryAllPropertiesEquals(expected, actual);
    }
}
