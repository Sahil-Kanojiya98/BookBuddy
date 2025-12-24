package com.example.service;

import com.example.dto.request.RatingRequest;
import com.example.dto.response.RatingResponse;
import com.example.repository.RatingRepository;
import com.example.security.authentication.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    @Transactional
    public RatingResponse addOrUpdateRating(UserPrincipal userPrincipal, Long bookId, RatingRequest ratingRequest) {
        return null;
    }

    @Transactional
    public void deleteRating(UserPrincipal userPrincipal, Long bookId) {
    }
}
