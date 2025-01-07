package com.nova.star.domain;

import static com.nova.star.domain.CategoryTestSamples.*;
import static com.nova.star.domain.IdeaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void ideasTest() {
        Category category = getCategoryRandomSampleGenerator();
        Idea ideaBack = getIdeaRandomSampleGenerator();

        category.addIdeas(ideaBack);
        assertThat(category.getIdeas()).containsOnly(ideaBack);
        assertThat(ideaBack.getCategory()).isEqualTo(category);

        category.removeIdeas(ideaBack);
        assertThat(category.getIdeas()).doesNotContain(ideaBack);
        assertThat(ideaBack.getCategory()).isNull();

        category.ideas(new HashSet<>(Set.of(ideaBack)));
        assertThat(category.getIdeas()).containsOnly(ideaBack);
        assertThat(ideaBack.getCategory()).isEqualTo(category);

        category.setIdeas(new HashSet<>());
        assertThat(category.getIdeas()).doesNotContain(ideaBack);
        assertThat(ideaBack.getCategory()).isNull();
    }
}
