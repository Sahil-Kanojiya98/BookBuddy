package com.example.controller;

import com.example.dto.request.RatingRequest;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.RatingResponse;
import com.example.security.authentication.UserPrincipal;
import com.example.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/{bookId}/rating")
    public ResponseEntity<ApiResponse<RatingResponse>> addOrUpdateRating(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("bookId") Long bookId,
            @RequestBody RatingRequest ratingRequest) {
        RatingResponse response = ratingService.addOrUpdateRating(userPrincipal, bookId, ratingRequest);
        return ApiResponse.build(HttpStatus.OK, "Rating created or updated", response);
    }

    @DeleteMapping("/{bookId}/rating")
    public ResponseEntity<ApiResponse<Void>> deleteRating(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("bookId") Long bookId) {
        ratingService.deleteRating(userPrincipal, bookId);
        return ApiResponse.build(HttpStatus.NO_CONTENT, "Rating removed");
    }
}