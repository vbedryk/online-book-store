package com.example.bookstore.service.impl;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParametersDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.book.BookSpecificationBuilder;
import com.example.bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto get(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book by id " + id
                        + " doesn't exist")));
    }

    @Override
    public void deleteById(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
    }

    @Override
    public BookDto update(CreateBookRequestDto requestDto, Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        bookMapper.updateBookFromDto(requestDto, book);
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long categoryId,
                                                                Pageable pageable) {
        return bookRepository.getBooksByCategoryId(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }
}
