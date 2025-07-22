package com.example.bookstore.repository.book.spec;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationProvider;
import com.example.bookstore.repository.book.BookSpecificationBuilder;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD_ISBN = "isbn";

    @Override
    public String getKey() {
        return BookSpecificationBuilder.ISBN_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(FIELD_ISBN).in(Arrays.stream(params).toArray());
    }
}
