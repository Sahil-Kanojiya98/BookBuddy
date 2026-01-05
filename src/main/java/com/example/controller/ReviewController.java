package com.example.controller;

import com.example.config.SwaggerConfig;
import com.example.dto.request.ReviewRequest;
import com.example.dto.request.ReviewSearchRequest;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.MyReviewResponse;
import com.example.dto.response.PaginatedResponse;
import com.example.dto.response.ReviewResponse;
import com.example.security.authentication.UserPrincipal;
import com.example.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping("/books/{bookId}/review")
	public ResponseEntity<ApiResponse<Void>> addOrUpdateReview(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PathVariable("bookId") Long bookId,
			@RequestBody ReviewRequest reviewRequest) {
		log.info("Add or update request received. bookId: {}", bookId);
		reviewService.addOrUpdateReview(userPrincipal, bookId, reviewRequest);
		return ApiResponse.build(HttpStatus.NO_CONTENT, "Review created or updated");
	}

	@DeleteMapping("/books/{bookId}/review")
	public ResponseEntity<ApiResponse<Void>> deleteReview(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PathVariable("bookId") Long bookId) {
		log.info("Delete review request received. bookId: {}", bookId);
		reviewService.deleteReview(userPrincipal, bookId);
		return ApiResponse.build(HttpStatus.NO_CONTENT, "Review removed");
	}

	@GetMapping("/users/me/reviews")
	public ResponseEntity<ApiResponse<PaginatedResponse<MyReviewResponse>>> getMyReviews(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@Valid @ModelAttribute ReviewSearchRequest reviewSearchRequest) {
		log.info("Get my reviews request received. reviewSearchRequest: {}", reviewSearchRequest);
		PaginatedResponse<MyReviewResponse> paginatedMyReviewResponse = reviewService.searchMyReviews(userPrincipal,
				reviewSearchRequest);
		return ApiResponse.build(HttpStatus.OK, paginatedMyReviewResponse);
	}

	@GetMapping("/reviews/search")
	public ResponseEntity<ApiResponse<PaginatedResponse<ReviewResponse>>> searchReviews(
			@Valid @ModelAttribute ReviewSearchRequest reviewSearchRequest) {
		log.info("Search reviews request received. reviewSearchRequest: {}", reviewSearchRequest);
		PaginatedResponse<ReviewResponse> paginatedReviewResponse = reviewService.searchReviews(reviewSearchRequest);
		return ApiResponse.build(HttpStatus.OK, paginatedReviewResponse);
	}
}
