package com.example.bookstore.repository.book.spec;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationProvider;
import com.example.bookstore.repository.book.BookSpecificationBuilder;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD_AUTHOR = "author";

    @Override
    public String getKey() {
        return BookSpecificationBuilder.AUTHOR_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(FIELD_AUTHOR).in(Arrays.stream(params).toArray());
    }
}
