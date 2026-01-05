-- =============================
-- SCHEMA FOR BookBuddyAppDB
-- =============================


-- =============================
-- DROP ALL TABLES IF EXISTS
-- =============================
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS user_books;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================
-- TABLES WITH ALL CONSTRAINTS AND INDEXES
-- =============================

-- USERS
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    INDEX idx_users_email (email),
    INDEX idx_users_username (username)
);

-- BOOKS
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(250) NOT NULL,
    author VARCHAR(200) NOT NULL,
    isbn VARCHAR(20),
    description VARCHAR(500),
    published_year INT,
    average_rating DOUBLE DEFAULT 0.0,
    rating_count INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    INDEX idx_books_title (title),
    INDEX idx_books_author (author),
    INDEX idx_books_isbn (isbn)
);

-- USER_BOOKS
CREATE TABLE user_books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    added_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_userbooks_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_userbooks_book FOREIGN KEY (book_id) REFERENCES books(id),
    INDEX idx_userbooks_user_id (user_id),
    INDEX idx_userbooks_book_id (book_id),
    INDEX idx_userbooks_status (status)
);

-- RATINGS
CREATE TABLE ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    user_book_id BIGINT NOT NULL,
    value INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_ratings_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_ratings_book FOREIGN KEY (book_id) REFERENCES books(id),
    CONSTRAINT fk_ratings_userbook FOREIGN KEY (user_book_id) REFERENCES user_books(id),
    INDEX idx_ratings_user_id (user_id),
    INDEX idx_ratings_book_id (book_id),
    INDEX idx_ratings_user_book_id (user_book_id),
    INDEX idx_ratings_value (value)
);

-- REVIEWS
CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    user_book_id BIGINT NOT NULL,
    content VARCHAR(2000),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_reviews_book FOREIGN KEY (book_id) REFERENCES books(id),
    CONSTRAINT fk_reviews_userbook FOREIGN KEY (user_book_id) REFERENCES user_books(id),
    INDEX idx_reviews_user_id (user_id),
    INDEX idx_reviews_book_id (book_id),
    INDEX idx_reviews_user_book_id (user_book_id),
    INDEX idx_reviews_created_at (created_at)
);
