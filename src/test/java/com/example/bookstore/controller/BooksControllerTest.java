package com.example.bookstore.controller;

import static com.example.bookstore.util.TestUtil.createBookRequestDto;
import static com.example.bookstore.util.TestUtil.createDefaultBookDto;
import static com.example.bookstore.util.TestUtil.createDefaultBookRequestDto;
import static com.example.bookstore.util.TestUtil.createTwoBookDtosWithSameCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BooksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:/db/scripts/create-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/delete-one-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Save book and get DTO
            """)
    void createBook_WithValidRequest_ShouldReturnValidBookDto() throws Exception {
        CreateBookRequestDto createBookRequestDto = createDefaultBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        BookDto expected = createDefaultBookDto();

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = "classpath:/db/scripts/delete-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/create-two-books-with-same-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/delete-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get book by valid id and get bookDto")
    void getBookById_ValidId_ShouldReturnValidBookDto() throws Exception {
        BookDto expected = createDefaultBookDto();
        Long bookId = 1L;

        MvcResult mvcResult = mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = "classpath:/db/scripts/delete-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/create-two-books-with-same-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/delete-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get valid page with books ")
    void getAll_ShouldReturnValidPage() throws Exception {
        List<BookDto> expected = createTwoBookDtosWithSameCategory();

        MvcResult result = mockMvc.perform(
                        get("/books")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andReturn();

        JavaType javaType = objectMapper.getTypeFactory()
                .constructParametricType(List.class, BookDto.class);
        String content = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("content").toString();
        List<BookDto> actual = objectMapper.readValue(content, javaType);

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:/db/scripts/create-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/delete-one-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create book with invalid data should return BadRequest")
    void createBook_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        CreateBookRequestDto invalidRequest = createBookRequestDto(
                "",
                "Author",
                "invalid-isbn",
                BigDecimal.valueOf(-10),
                "Description",
                "cover.png",
                List.of(1L)
        );
        String jsonRequest = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by non-existing id should return NotFound")
    void getBookById_NonExistingId_ShouldReturnNotFound() throws Exception {
        Long nonExistingId = 999L;

        mockMvc.perform(get("/books/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book with invalid id format should return BadRequest")
    void getBookById_InvalidIdFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/books/{id}", "invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create book without authentication should return Unauthorized")
    void createBook_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        CreateBookRequestDto request = createDefaultBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Create book with USER role should return Forbidden")
    void createBook_WithUserRole_ShouldReturnForbidden() throws Exception {
        CreateBookRequestDto request = createDefaultBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
