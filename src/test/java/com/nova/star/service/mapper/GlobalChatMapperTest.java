package com.nova.star.service.mapper;

import static com.nova.star.domain.GlobalChatAsserts.*;
import static com.nova.star.domain.GlobalChatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GlobalChatMapperTest {

    private GlobalChatMapper globalChatMapper;

    @BeforeEach
    void setUp() {
        globalChatMapper = new GlobalChatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGlobalChatSample1();
        var actual = globalChatMapper.toEntity(globalChatMapper.toDto(expected));
        assertGlobalChatAllPropertiesEquals(expected, actual);
    }
}
