package com.example.bookstore.repository.book;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;

public class IsbnSpecification {
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("isbn").in(Arrays.stream(params).toArray());
    }
}
