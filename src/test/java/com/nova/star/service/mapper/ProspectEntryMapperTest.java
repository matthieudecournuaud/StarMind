package com.nova.star.service.mapper;

import static com.nova.star.domain.ProspectEntryAsserts.*;
import static com.nova.star.domain.ProspectEntryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProspectEntryMapperTest {

    private ProspectEntryMapper prospectEntryMapper;

    @BeforeEach
    void setUp() {
        prospectEntryMapper = new ProspectEntryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProspectEntrySample1();
        var actual = prospectEntryMapper.toEntity(prospectEntryMapper.toDto(expected));
        assertProspectEntryAllPropertiesEquals(expected, actual);
    }
}
