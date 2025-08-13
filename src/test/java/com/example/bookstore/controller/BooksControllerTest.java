package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.model.Category;
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
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("First");
        createBookRequestDto.setAuthor("Admin");
        createBookRequestDto.setIsbn("1234-5678-9");
        createBookRequestDto.setPrice(BigDecimal.valueOf(10.00));
        createBookRequestDto.setDescription("Something");
        createBookRequestDto.setCoverImage("image.png");
        createBookRequestDto.setCategoriesId(List.of(categoryId));
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        Long bookId = 1L;
        BookDto expected = new BookDto();
        expected.setId(bookId);
        expected.setTitle("First");
        expected.setAuthor("Admin");
        expected.setIsbn("1234-5678-9");
        expected.setPrice(BigDecimal.valueOf(10.00));
        expected.setDescription("Something");
        expected.setCoverImage("image.png");
        expected.setCategoryIds(List.of(categoryId));

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
    void getBookById_ValidId_ShouldReturnValidBookDto() throws Exception {
        Long categoryId = 1L;
        Long bookId = 1L;
        BookDto expected = new BookDto();
        expected.setId(bookId);
        expected.setTitle("First");
        expected.setAuthor("Admin");
        expected.setIsbn("1234-5678-9");
        expected.setPrice(BigDecimal.valueOf(10.00));
        expected.setDescription("Something");
        expected.setCoverImage("image.png");
        expected.setCategoryIds(List.of(categoryId));
        Long existedId = 1L;

        MvcResult mvcResult = mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        assertEquals(expected.getIsbn(), actual.getIsbn());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = "classpath:/db/scripts/delete-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/create-two-books-with-same-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/db/scripts/delete-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ShouldReturnValidPage() throws Exception {
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setTitle("First");
        bookDto1.setAuthor("Admin");
        bookDto1.setIsbn("1234-5678-9");
        bookDto1.setPrice(BigDecimal.valueOf(10.00));
        bookDto1.setDescription("Something");
        bookDto1.setCoverImage("image.png");
        bookDto1.setCategoryIds(List.of(1L));

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Second");
        bookDto2.setAuthor("User");
        bookDto2.setIsbn("1234-5678-8");
        bookDto2.setPrice(BigDecimal.valueOf(100.00));
        bookDto2.setDescription("Something good");
        bookDto2.setCoverImage("image2.png");
        bookDto2.setCategoryIds(List.of(1L));

        List<BookDto> expected = List.of(bookDto1, bookDto2);

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
}
