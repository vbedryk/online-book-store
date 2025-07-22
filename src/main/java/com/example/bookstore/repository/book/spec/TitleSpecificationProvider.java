package com.example.bookstore.repository.book.spec;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationProvider;
import com.example.bookstore.repository.book.BookSpecificationBuilder;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD_TITLE = "title";

    @Override
    public String getKey() {
        return BookSpecificationBuilder.TITLE_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(FIELD_TITLE).in(Arrays.stream(params).toArray());
    }
}
