package com.example.controller;

import com.example.dto.request.ReviewRequest;
import com.example.dto.request.ReviewSearchRequest;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.ReviewResponse;
import com.example.security.authentication.UserPrincipal;
import com.example.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/books/{bookId}/review")
    public ResponseEntity<ApiResponse<Void>> addOrUpdateReview(
            @PathVariable("bookId") Long bookId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ReviewRequest reviewRequest) {
        log.info("Add or update request received. bookId: {}", bookId);
        reviewService.addOrUpdateReview(userPrincipal, bookId, reviewRequest);
        return ApiResponse.build(HttpStatus.NO_CONTENT, "Review created or updated");
    }

    @DeleteMapping("/books/{bookId}/review")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable("bookId") Long bookId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("Delete review request received. bookId: {}", bookId);
        reviewService.deleteReview(userPrincipal, bookId);
        return ApiResponse.build(HttpStatus.NO_CONTENT, "Review removed");
    }

    @GetMapping("/users/me/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getMyReviews(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute ReviewSearchRequest reviewSearchRequest
    ) {
        log.info("Get my reviews request received. reviewSearchRequest: {}", reviewSearchRequest);
        Page<ReviewResponse> reviewResponsePage = reviewService.searchMyReviews(userPrincipal, reviewSearchRequest);
        return ApiResponse.build(HttpStatus.OK, reviewResponsePage);
    }

    @GetMapping("/reviews/search")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> searchReviews(
            @ModelAttribute ReviewSearchRequest reviewSearchRequest
    ) {
        log.info("Search reviews request received. reviewSearchRequest: {}", reviewSearchRequest);
        Page<ReviewResponse> reviews = reviewService.searchReviews(reviewSearchRequest);
        return ApiResponse.build(HttpStatus.OK, reviews);
    }
}
