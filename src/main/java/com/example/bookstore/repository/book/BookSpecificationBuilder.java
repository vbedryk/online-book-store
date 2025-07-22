package com.example.bookstore.repository.book;

import com.example.bookstore.dto.BookSearchParametersDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationBuilder;
import com.example.bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String AUTHOR_KEY = "author";
    public static final String ISBN_KEY = "isbn";
    public static final String TITLE_KEY = "title";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.allOf();
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.isbns() != null && searchParameters.isbns().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParameters.isbns()));
        }
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParameters.titles()));
        }
        return spec;
    }
}
