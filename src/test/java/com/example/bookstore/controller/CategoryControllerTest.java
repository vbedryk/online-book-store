package com.example.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("Should return list of categories")
    @Sql(scripts = "classpath:/db/scripts/delete-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/create-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/delete-one-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllCategories_ShouldReturnCategories() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).contains("Fantasy");
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    @Sql(scripts = "classpath:/db/scripts/delete-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create new category")
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto(
                "Sci-Fi",
                "Science fiction books"
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto responseDto = objectMapper.readValue(mvcResult
                        .getResponse().getContentAsString(),
                CategoryResponseDto.class);

        assertThat(responseDto.name()).isEqualTo("Sci-Fi");
        assertThat(responseDto.description())
                .isEqualTo("Science fiction books");
        assertThat(responseDto.id()).isNotNull();
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("Get category by id")
    @Sql(scripts = "classpath:/db/scripts/delete-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/create-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/delete-one-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ShouldReturnCategory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}",
                        1L))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto category = objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(),
                CategoryResponseDto.class);

        assertThat(category.id()).isEqualTo(1L);
        assertThat(category.name()).isEqualTo("Fantasy");
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    @DisplayName(" Delete category by id")
    @Sql(scripts = "classpath:/db/scripts/delete-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/create-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L))
                .andExpect(status().isOk());
    }
}
