package com.example.repository;

import com.example.dto.response.BookResponse;
import com.example.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("""
			    SELECT new com.example.dto.response.BookResponse(
			        b.id, b.title, b.author, b.isbn, b.description,
			         b.publishedYear, b.averageRating, b.ratingCount
			    )
			    FROM Book b
			    WHERE b.id = :id
			""")
	Optional<BookResponse> findBookResponseById(@Param("id") Long id);

	@Query("""
			    SELECT new com.example.dto.response.BookResponse(
			        b.id, b.title, b.author, b.isbn, b.description,
			         b.publishedYear, b.averageRating, b.ratingCount
			    )
			    FROM Book b
			    WHERE (:title IS NULL OR b.title LIKE %:title%)
			    AND (:author IS NULL OR b.author LIKE %:author%)
			    AND (:minRating IS NULL OR b.averageRating >= :minRating)
			    AND (:publishedYearFrom IS NULL OR b.publishedYear >= :publishedYearFrom)
			""")
	Page<BookResponse> searchBooks(
			@Param("title") String title, @Param("author") String author, @Param("minRating") Float minRating,
			@Param("publishedYearFrom") Integer publishedYearFrom,
			PageRequest pageRequest);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
			    UPDATE Book b
			    SET
			        b.averageRating = (
			            SELECT COALESCE(AVG(r.value), 0)
			            FROM Rating r
			            WHERE r.book.id = :bookId
			        ),
			        b.ratingCount = (
			            SELECT COUNT(r)
			            FROM Rating r
			            WHERE r.book.id = :bookId
			        )
			    WHERE b.id = :bookId
			""")
	void updateAverageRatingAndRatingCount(@Param("bookId") Long bookId);
}
