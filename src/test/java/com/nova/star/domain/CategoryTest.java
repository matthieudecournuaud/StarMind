package com.nova.star.domain;

import static com.nova.star.domain.CategoryTestSamples.*;
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

    @Test
    void subcategoriesTest() {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.addSubcategories(categoryBack);
        assertThat(category.getSubcategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getSuperCategory()).isEqualTo(category);

        category.removeSubcategories(categoryBack);
        assertThat(category.getSubcategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getSuperCategory()).isNull();

        category.subcategories(new HashSet<>(Set.of(categoryBack)));
        assertThat(category.getSubcategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getSuperCategory()).isEqualTo(category);

        category.setSubcategories(new HashSet<>());
        assertThat(category.getSubcategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getSuperCategory()).isNull();
    }

    @Test
    void parentCategoryTest() {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.setParentCategory(categoryBack);
        assertThat(category.getParentCategory()).isEqualTo(categoryBack);

        category.parentCategory(null);
        assertThat(category.getParentCategory()).isNull();
    }

    @Test
    void superCategoryTest() {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.setSuperCategory(categoryBack);
        assertThat(category.getSuperCategory()).isEqualTo(categoryBack);

        category.superCategory(null);
        assertThat(category.getSuperCategory()).isNull();
    }
}
