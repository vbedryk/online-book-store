package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> get(Long id);
}
