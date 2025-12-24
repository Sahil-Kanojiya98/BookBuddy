package com.example.service;

import com.example.dto.request.ReviewRequest;
import com.example.dto.request.ReviewSearchRequest;
import com.example.dto.response.ReviewResponse;
import com.example.security.authentication.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    @Transactional
    public void addOrUpdateReview(UserPrincipal userPrincipal, Long bookId, ReviewRequest reviewRequest) {
    }

    @Transactional
    public void deleteReview(UserPrincipal userPrincipal, Long bookId) {
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByUser(UserPrincipal userPrincipal) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> searchMyReviews(UserPrincipal userPrincipal, ReviewSearchRequest reviewSearchRequest) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> searchReviews(ReviewSearchRequest reviewSearchRequest) {
        return null;
    }
}
