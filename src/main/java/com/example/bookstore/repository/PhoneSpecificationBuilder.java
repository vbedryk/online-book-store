package com.example.bookstore.repository;

import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.AuthorSpecification;
import com.example.bookstore.repository.book.IsbnSpecification;
import com.example.bookstore.repository.book.TitleSpecification;
import org.springframework.data.jpa.domain.Specification;

public class PhoneSpecificationBuilder implements SpecificationBuilder {
    @Override
    public Specification build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.allOf();
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(new AuthorSpecification().getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            spec = spec.and(new IsbnSpecification().getSpecification(searchParameters.isbn()));
        }
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(new TitleSpecification().getSpecification(searchParameters.title()));
        }
        return spec;
    }
}
