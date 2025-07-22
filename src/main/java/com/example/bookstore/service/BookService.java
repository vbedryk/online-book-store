package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto get(Long id);

    void deleteById(Long id);

    BookDto update(CreateBookRequestDto requestDto, Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
