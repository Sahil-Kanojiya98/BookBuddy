-- =============================
-- SAMPLE DATA
-- =============================

-- Users
INSERT INTO users (email, password_hash, username, created_at, updated_at)
VALUES
('user1@example.com', '$2a$05$mv8pmqSJ10cbQ3EdNMcg2.a2cwnnvBIRECLZwVWgC9F0ZDptdstZ.', 'user1', NOW(), NOW()),
('user2@example.com', '$2a$05$mv8pmqSJ10cbQ3EdNMcg2.a2cwnnvBIRECLZwVWgC9F0ZDptdstZ.', 'user2', NOW(), NOW()),
('user3@example.com', '$2a$05$mv8pmqSJ10cbQ3EdNMcg2.a2cwnnvBIRECLZwVWgC9F0ZDptdstZ.', 'user3', NOW(), NOW()),
('user4@example.com', '$2a$05$mv8pmqSJ10cbQ3EdNMcg2.a2cwnnvBIRECLZwVWgC9F0ZDptdstZ.', 'user4', NOW(), NOW());

-- Books
INSERT INTO books (title, author, isbn, description, published_year, average_rating, rating_count, created_at, updated_at)
VALUES
('The Hobbit', 'J.R.R. Tolkien', '978-0547928227', 'A fantasy novel about a hobbit''s adventure.', 1937, 4.7, 10, NOW(), NOW()),
('1984', 'George Orwell', '978-0451524935', 'Dystopian novel about totalitarianism.', 1949, 4.6, 15, NOW(), NOW()),
('Pride and Prejudice', 'Jane Austen', '978-1503290563', 'A classic romance novel.', 1813, 4.5, 20, NOW(), NOW()),
('To Kill a Mockingbird', 'Harper Lee', '978-0060935467', 'A novel about racial injustice.', 1960, 4.8, 12, NOW(), NOW()),
('The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 'A story of the American dream.', 1925, 4.4, 18, NOW(), NOW()),
('Moby Dick', 'Herman Melville', '978-1503280786', 'A tale of obsession and revenge.', 1851, 4.2, 9, NOW(), NOW()),
('War and Peace', 'Leo Tolstoy', '978-1420954309', 'Epic historical novel.', 1869, 4.3, 7, NOW(), NOW()),
('The Catcher in the Rye', 'J.D. Salinger', '978-0316769488', 'A story of teenage rebellion.', 1951, 4.0, 14, NOW(), NOW()),
('Brave New World', 'Aldous Huxley', '978-0060850524', 'A futuristic dystopian novel.', 1932, 4.5, 11, NOW(), NOW()),
('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', '978-0590353427', 'A young wizard''s journey begins.', 1997, 4.9, 25, NOW(), NOW());

-- UserBooks (user1 and user2 with 3-4 books each)
INSERT INTO user_books (user_id, book_id, status, added_at, updated_at)
VALUES
-- user1
(1, 1, 'READING', NOW(), NOW()),
(1, 3, 'TO_READ', NOW(), NOW()),
(1, 5, 'FINISHED', NOW(), NOW()),
(1, 7, 'READING', NOW(), NOW()),
-- user2
(2, 2, 'FINISHED', NOW(), NOW()),
(2, 4, 'READING', NOW(), NOW()),
(2, 6, 'TO_READ', NOW(), NOW()),
(2, 8, 'FINISHED', NOW(), NOW());

-- Ratings
INSERT INTO ratings (user_id, book_id, user_book_id, value, created_at, updated_at)
VALUES
-- user1
(1, 1, 1, 5, NOW(), NOW()),
(1, 3, 2, 4, NOW(), NOW()),
-- user2
(2, 2, 5, 5, NOW(), NOW()),
(2, 4, 6, 4, NOW(), NOW());

-- Reviews
INSERT INTO reviews (user_id, book_id, user_book_id, content, created_at, updated_at)
VALUES
-- user1
(1, 1, 1, 'Loved the adventure and world-building!', NOW(), NOW()),
(1, 3, 2, 'Classic romance, really enjoyed it.', NOW(), NOW()),
-- user2
(2, 2, 5, 'Very thought-provoking dystopian story.', NOW(), NOW()),
(2, 4, 6, 'A powerful message about justice.', NOW(), NOW());
