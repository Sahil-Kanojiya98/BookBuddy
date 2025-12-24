-- =============================
-- SAMPLE DATA
-- =============================

-- Users
INSERT INTO users (email, password_hash, username, created_at, updated_at)
VALUES
('alice@example.com', '$2a$05$h8QUZbjeXpHNkreELWDpU.P9F4qCT2weavlARGDK9sQdeSQV4wqKq', 'alice', NOW(), NOW()),
('bob@example.com', '$2a$05$Be8LKCTFg4fN/YHWBGfhauiJkIIWNy1KG3./u.iTe5qYwewnRhHMO', 'bob', NOW(), NOW());
-- Passwords are: password1, password2

-- Books
INSERT INTO books (title, author, isbn, description, published_year, average_rating, rating_count, created_at, updated_at)
VALUES
('The Hobbit', 'J.R.R. Tolkien', '978-0547928227', 'A fantasy novel about a hobbit''s adventure.', 1937, 4.7, 10, NOW(), NOW()),
('1984', 'George Orwell', '978-0451524935', 'Dystopian novel about totalitarianism.', 1949, 4.6, 15, NOW(), NOW());

-- UserBooks
INSERT INTO user_books (user_id, book_id, status, added_at, updated_at)
VALUES
(1, 1, 'READING', NOW(), NOW()),
(1, 2, 'TO_READ', NOW(), NOW()),
(2, 2, 'COMPLETED', NOW(), NOW());

-- Ratings
INSERT INTO ratings (user_id, book_id, user_book_id, value, created_at, updated_at)
VALUES
(1, 1, 1, 5, NOW(), NOW()),
(2, 2, 3, 4, NOW(), NOW());

-- Reviews
INSERT INTO reviews (user_id, book_id, user_book_id, content, created_at, updated_at)
VALUES
(1, 1, 1, 'Amazing adventure, highly recommend!', NOW(), NOW()),
(2, 2, 3, 'A thought-provoking read.', NOW(), NOW());
