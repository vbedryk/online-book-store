package com.example.bookstore.repository.book;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;
import java.util.Arrays;

public class AuthorSpecification {
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("author").in(Arrays.stream(params).toArray());
    }
}
