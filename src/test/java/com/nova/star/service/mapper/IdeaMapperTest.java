package com.nova.star.service.mapper;

import static com.nova.star.domain.IdeaAsserts.*;
import static com.nova.star.domain.IdeaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdeaMapperTest {

    private IdeaMapper ideaMapper;

    @BeforeEach
    void setUp() {
        ideaMapper = new IdeaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIdeaSample1();
        var actual = ideaMapper.toEntity(ideaMapper.toDto(expected));
        assertIdeaAllPropertiesEquals(expected, actual);
    }
}
