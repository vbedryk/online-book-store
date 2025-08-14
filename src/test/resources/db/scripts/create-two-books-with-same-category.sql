INSERT INTO categories (id, name, description) VALUES (1, 'Fantasy', 'Good category');

INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (1, 'First', 'Admin', '1234-5678-9', 10.99, 'Something', 'image.png'),
       (2, 'Second', 'User', '1234-5678-8', 100.99, 'Something good', 'image2.png');

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (2, 1);
