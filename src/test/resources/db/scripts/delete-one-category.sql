DELETE FROM books_categories WHERE category_id = 1;
DELETE FROM categories WHERE id = 1;
ALTER TABLE books_categories AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;
