package com.example.repository;

import com.example.dto.response.UserBookResponse;
import com.example.model.UserBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {

	Optional<UserBook> findByUserIdAndBookId(Long userId, Long bookId);

	@Query("""
			    SELECT new com.example.dto.response.UserBookResponse(
			        ub.id,
			        new com.example.dto.response.BookResponse(
			            b.id,
			            b.title,
			            b.author,
			            b.isbn,
			            b.description,
			            b.publishedYear,
			            b.averageRating,
			            b.ratingCount
			        ),
			        ub.status,
			        ub.addedAt
			    )
			    FROM UserBook ub
			    JOIN ub.book b
			    WHERE ub.user.id = :userId
			      AND b.id = :bookId
			""")
	Optional<UserBookResponse> findUserBookResponseByUserIdAndBookId(@Param("userId") Long userId,
			@Param("bookId") Long bookId);

	@Query("""
			    SELECT new com.example.dto.response.UserBookResponse(
			        ub.id,
			        new com.example.dto.response.BookResponse(
			            b.id,
			            b.title,
			            b.author,
			            b.isbn,
			            b.description,
			            b.publishedYear,
			            b.averageRating,
			            b.ratingCount
			        ),
			        ub.status,
			        ub.addedAt
			    )
			    FROM UserBook ub
			    JOIN ub.book b
			    WHERE ub.user.id = :userId
			      AND (:title IS NULL OR b.title LIKE %:title%)
			      AND (:author IS NULL OR b.author LIKE %:author%)
			      AND (:minRating IS NULL OR b.averageRating >= :minRating)
			      AND (:publishedYearFrom IS NULL OR b.publishedYear >= :publishedYearFrom)
			""")
	Page<UserBookResponse> searchMyLibraryBooks(
			@Param("userId") Long userId,
			@Param("title") String title,
			@Param("author") String author,
			@Param("minRating") Integer minRating,
			@Param("publishedYearFrom") Integer publishedYearFrom,
			Pageable pageable);
}
