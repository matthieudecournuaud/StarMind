package com.nova.star.service.mapper;

import static com.nova.star.domain.CommentAsserts.*;
import static com.nova.star.domain.CommentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        commentMapper = new CommentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommentSample1();
        var actual = commentMapper.toEntity(commentMapper.toDto(expected));
        assertCommentAllPropertiesEquals(expected, actual);
    }
}
