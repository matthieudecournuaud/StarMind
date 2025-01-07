package com.nova.star.service.mapper;

import static com.nova.star.domain.IdeaHistoryAsserts.*;
import static com.nova.star.domain.IdeaHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdeaHistoryMapperTest {

    private IdeaHistoryMapper ideaHistoryMapper;

    @BeforeEach
    void setUp() {
        ideaHistoryMapper = new IdeaHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIdeaHistorySample1();
        var actual = ideaHistoryMapper.toEntity(ideaHistoryMapper.toDto(expected));
        assertIdeaHistoryAllPropertiesEquals(expected, actual);
    }
}
