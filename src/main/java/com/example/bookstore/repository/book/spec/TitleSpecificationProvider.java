package com.example.bookstore.repository.book.spec;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("title").in(Arrays.stream(params).toArray());
    }
}
