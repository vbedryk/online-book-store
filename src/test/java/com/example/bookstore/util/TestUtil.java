package com.example.bookstore.util;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TestUtil {
    private TestUtil() {
    }

    public static Category createDefaultCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        category.setDescription("Good category");
        return category;
    }

    public static CategoryRequestDto createDefaultCategoryRequestDto() {
        return new CategoryRequestDto("Fantasy", "Good category");
    }

    public static CategoryRequestDto createSciFiCategoryRequestDto() {
        return new CategoryRequestDto("Sci-Fi", "Science fiction books");
    }

    public static CategoryResponseDto createDefaultCategoryResponseDto() {
        return new CategoryResponseDto(1L, "Fantasy", "Good category");
    }

    public static CategoryResponseDto createSciFiCategoryResponseDto() {
        return new CategoryResponseDto(1L, "Sci-Fi", "Science fiction books");
    }
    public static Book createDefaultBook() {
        Category category = createDefaultCategory();
        Book book = new Book();
        book.setId(1L);
        book.setTitle("First");
        book.setAuthor("Admin");
        book.setIsbn("1234-5678-9");
        book.setPrice(BigDecimal.valueOf(10.00));
        book.setDescription("Something");
        book.setCoverImage("image.png");
        book.setCategories(Set.of(category));
        return book;
    }

    public static Book createSecondBook() {
        Category category = createDefaultCategory();
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Second");
        book.setAuthor("User");
        book.setIsbn("1234-5678-8");
        book.setPrice(BigDecimal.valueOf(100.00));
        book.setDescription("Something good");
        book.setCoverImage("image2.png");
        book.setCategories(Set.of(category));
        return book;
    }

    public static Book createTestBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("978-1234567890");
        book.setPrice(BigDecimal.valueOf(29.99));
        book.setDescription("Test Description");
        book.setCoverImage("test-cover.jpg");
        return book;
    }

    public static BookDto createDefaultBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("First");
        bookDto.setAuthor("Admin");
        bookDto.setIsbn("1234-5678-9");
        bookDto.setPrice(BigDecimal.valueOf(10.00));
        bookDto.setDescription("Something");
        bookDto.setCoverImage("image.png");
        bookDto.setCategoryIds(List.of(1L));
        return bookDto;
    }

    public static BookDto createSecondBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(2L);
        bookDto.setTitle("Second");
        bookDto.setAuthor("User");
        bookDto.setIsbn("1234-5678-8");
        bookDto.setPrice(BigDecimal.valueOf(100.00));
        bookDto.setDescription("Something good");
        bookDto.setCoverImage("image2.png");
        bookDto.setCategoryIds(List.of(1L));
        return bookDto;
    }

    public static BookDto createTestBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");
        bookDto.setIsbn("978-1234567890");
        bookDto.setPrice(BigDecimal.valueOf(29.99));
        bookDto.setDescription("Test Description");
        bookDto.setCoverImage("test-cover.jpg");
        return bookDto;
    }

    public static CreateBookRequestDto createDefaultBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("First");
        requestDto.setAuthor("Admin");
        requestDto.setIsbn("1234-5678-9");
        requestDto.setPrice(BigDecimal.valueOf(10.00));
        requestDto.setDescription("Something");
        requestDto.setCoverImage("image.png");
        requestDto.setCategoriesId(List.of(1L));
        return requestDto;
    }

    public static CreateBookRequestDto createUpdatedBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Book Title");
        requestDto.setAuthor("Updated Author");
        requestDto.setIsbn("978-1234567890");
        requestDto.setPrice(BigDecimal.valueOf(39.99));
        requestDto.setDescription("Updated Description");
        requestDto.setCoverImage("updated-cover.jpg");
        return requestDto;
    }

    public static List<Book> createTwoBooksWithSameCategory() {
        return List.of(createDefaultBook(), createSecondBook());
    }

    public static List<BookDto> createTwoBookDtosWithSameCategory() {
        return List.of(createDefaultBookDto(), createSecondBookDto());
    }
}