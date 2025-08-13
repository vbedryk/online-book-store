package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.config.CustomMySqlContainer;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.util.TestUtil;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Container
    private static MySQLContainer<?> mysql = CustomMySqlContainer.getInstance()
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("testdb");

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = "classpath:db/scripts/create-two-books-with-same-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find two books by single category
            """)
    void findAllByCategoryId_TwoBooksByCategory_Ok() {
        // Given
        List<Book> expected = TestUtil.createTwoBooksWithSameCategory();
        Pageable pageable = PageRequest.of(0, 10);

        // When
        List<Book> actual = bookRepository.getBooksByCategoryId(1L, pageable).toList();

        // Then
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(1).getTitle(), actual.get(1).getTitle());
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-two-books-with-same-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Find two books by single category
            """)
    void findAllByCategoryId_NonExistCategory_ExpectNull() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookRepository.getBooksByCategoryId(3L, pageable).toList();

        assertEquals(expected, actual);
    }
}
