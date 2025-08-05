package com.example.bookstore.repository.book;

import com.example.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b from Book b JOIN b.categories c WHERE c = :categoryId")
    Page<Book> getBooksByCategoryId(Long categoryId, Pageable pageable);

    Optional<Book> getBooksById(Long id);
}
