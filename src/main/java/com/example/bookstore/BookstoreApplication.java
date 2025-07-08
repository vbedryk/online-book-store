package com.example.bookstore;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {

    private final BookService bookService;

    @Autowired
    public BookstoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();

                book.setTitle("Clean Code");
                book.setAuthor("Robert C. Martin");
                book.setIsbn("9780132350884");
                book.setPrice(new BigDecimal("45.99"));
                book.setDescription("A Handbook of Agile Software Craftsmanship");
                book.setCoverImage("https://example.com/images/clean-code.jpg");

                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }
}
