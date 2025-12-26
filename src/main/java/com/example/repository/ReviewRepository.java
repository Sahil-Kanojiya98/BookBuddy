package com.example.repository;

import com.example.dto.response.MyReviewResponse;
import com.example.dto.response.ReviewResponse;
import com.example.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query("""
			    SELECT new com.example.dto.response.MyReviewResponse(
			        r.id, b.id, r.content, r.createdAt, r.updatedAt, b.title
			    )
			    FROM Review r
			    JOIN r.book b
			    JOIN r.user u
			    WHERE u.id = :userId
			    AND (:title IS NULL OR b.title LIKE %:title%)
			    AND (:author IS NULL OR b.author LIKE %:author%)
			    AND (:keyword IS NULL OR r.content LIKE %:keyword%)
			""")
	Page<MyReviewResponse> searchMyReviews(
			@Param("userId") Long userId, @Param("title") String title, @Param("author") String author,
			@Param("keyword") String keyword, Pageable pageable);

	@Query("""
			    SELECT new com.example.dto.response.ReviewResponse(
			        r.id, b.id, r.content, r.createdAt, r.updatedAt, b.title, u.username
			    )
			    FROM Review r
			    JOIN r.book b
			    JOIN r.user u
			    WHERE (:title IS NULL OR b.title LIKE %:title%)
			    AND (:author IS NULL OR b.author LIKE %:author%)
			    AND (:keyword IS NULL OR r.content LIKE %:keyword%)
			""")
	Page<ReviewResponse> searchReviews(
			String title, String author, String keyword, Pageable pageable);
}