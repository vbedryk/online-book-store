package com.example.bookstore.service;

import static com.example.bookstore.util.TestUtil.createTestBook;
import static com.example.bookstore.util.TestUtil.createTestBookDto;
import static com.example.bookstore.util.TestUtil.createUpdatedBookRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.category.CategoryRepository;
import com.example.bookstore.service.impl.BookServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;

    @BeforeEach
    void setUp() {
        book = createTestBook();
        bookDto = createTestBookDto();
        createBookRequestDto = createUpdatedBookRequestDto();
    }

    @Test
    @DisplayName("Save book - should return BookDto when valid CreateBookRequestDto provided")
    void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        // Given
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(categoryRepository.findAllById(any())).thenReturn(Collections.emptyList());

        // When
        BookDto actual = bookService.save(createBookRequestDto);

        // Then
        assertNotNull(actual);
        assertEquals(bookDto, actual);
        verify(bookMapper).toModel(createBookRequestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("""
            Find all books and return Page
            """)
    void findAll_ShouldReturnPageOfBooks() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDto> expected = new PageImpl<>(List.of(bookDto), pageable, 1L);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1L);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        // When
        Page<BookDto> actual = bookService.findAll(pageable);
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Throw EntityNotFoundException by non exist id
            """)
    void get_NonExistId_ShouldReturnException() {
        // Given
        long nonExistentId = 0L;
        String expectedMessage = "Book by id " + nonExistentId
                + " doesn't exist";
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> bookService.get(nonExistentId));
        //Then
        assertEquals(expectedMessage, actual.getMessage());
        verify(bookRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("Delete book by existed id")
    void deleteById_ExistId_ShouldDeleteBook() {
        // Given
        long existedId = 1L;
        when(bookRepository.existsById(existedId)).thenReturn(true);
        // When
        bookService.deleteById(existedId);
        //Then
        verify(bookRepository).existsById(existedId);
        verify(bookRepository).deleteById(existedId);
    }

    @Test
    @DisplayName("Update book by not existed id")
    void update_NonExistId_ShouldReturnException() {
        // Given
        long nonExistedId = 100L;
        String expectedMessage = "Book not found with id: " + nonExistedId;
        when(bookRepository.findById(nonExistedId)).thenReturn(Optional.empty());
        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(createBookRequestDto, nonExistedId));
        // Then
        assertEquals(expectedMessage, exception.getMessage());
        verify(bookRepository).findById(nonExistedId);
    }

    @Test
    @DisplayName("Update book by existed id")
    void update_ExistId_ShouldReturnBookDto() {
        // Given
        long existedId = 1L;
        when(bookRepository.findById(existedId)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateBookFromDto(createBookRequestDto, book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        // When
        BookDto actual = bookService.update(createBookRequestDto, existedId);

        // Then
        assertNotNull(actual);
        assertEquals(bookDto, actual);
        verify(bookRepository).findById(existedId);
        verify(bookMapper).updateBookFromDto(createBookRequestDto, book);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }
}
