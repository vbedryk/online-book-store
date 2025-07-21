package com.example.bookstore.repository;

import com.example.bookstore.dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder {
    @Override
    public Specification build(BookSearchParametersDto bookSearchParametersDto) {
        return null;
    }
}
