package com.nova.star.service.mapper;

import static com.nova.star.domain.ProspectBoardAsserts.*;
import static com.nova.star.domain.ProspectBoardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProspectBoardMapperTest {

    private ProspectBoardMapper prospectBoardMapper;

    @BeforeEach
    void setUp() {
        prospectBoardMapper = new ProspectBoardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProspectBoardSample1();
        var actual = prospectBoardMapper.toEntity(prospectBoardMapper.toDto(expected));
        assertProspectBoardAllPropertiesEquals(expected, actual);
    }
}
