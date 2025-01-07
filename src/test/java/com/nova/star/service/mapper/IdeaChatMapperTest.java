package com.nova.star.service.mapper;

import static com.nova.star.domain.IdeaChatAsserts.*;
import static com.nova.star.domain.IdeaChatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdeaChatMapperTest {

    private IdeaChatMapper ideaChatMapper;

    @BeforeEach
    void setUp() {
        ideaChatMapper = new IdeaChatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIdeaChatSample1();
        var actual = ideaChatMapper.toEntity(ideaChatMapper.toDto(expected));
        assertIdeaChatAllPropertiesEquals(expected, actual);
    }
}
