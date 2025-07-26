package com.example.bookstore.dto.book;

public record BookSearchParametersDto(String[] titles, String[] authors, String[] isbns) {
}
