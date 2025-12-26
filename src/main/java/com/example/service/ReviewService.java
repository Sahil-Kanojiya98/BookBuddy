package com.example.service;

import com.example.dto.request.ReviewRequest;
import com.example.dto.request.ReviewSearchRequest;
import com.example.dto.response.MyReviewResponse;
import com.example.dto.response.PaginatedResponse;
import com.example.dto.response.ReviewResponse;
import com.example.exception.RatingNotFoundException;
import com.example.exception.UserBookNotFoundException;
import com.example.model.Review;
import com.example.model.UserBook;
import com.example.repository.ReviewRepository;
import com.example.repository.UserBookRepository;
import com.example.repository.UserRepository;
import com.example.security.authentication.UserPrincipal;
import com.example.validation.ValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ValidatorService validatorService;
	private final UserBookRepository userBookRepository;
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;

	@Transactional
	public void addOrUpdateReview(UserPrincipal userPrincipal, Long bookId, ReviewRequest reviewRequest) {
		validatorService.validatePositiveBookId(bookId);

		Optional<UserBook> optionalUserBook = userBookRepository.findByUserIdAndBookId(userPrincipal.getId(), bookId);
		if (optionalUserBook.isEmpty()) {
			throw new UserBookNotFoundException("User has not added this book to their collection");
		}

		UserBook userBook = optionalUserBook.get();
		Review review = userBook.getReview();
		if (review != null) {
			review.setContent(reviewRequest.getContent());
			reviewRepository.save(review);
			log.info("Updated review. bookId {} userId {}", bookId, userPrincipal.getId());
		} else {
			review = new Review();
			review.setUser(userBook.getUser());
			review.setBook(userBook.getBook());
			review.setUserBook(userBook);
			review.setContent(reviewRequest.getContent());

			reviewRepository.save(review);
			log.info("Added review for book ID {} by user ID {}", bookId, userPrincipal.getId());
		}
	}

	@Transactional
	public void deleteReview(UserPrincipal userPrincipal, Long bookId) {
		validatorService.validatePositiveBookId(bookId);
		Optional<UserBook> optionalUserBook = userBookRepository.findByUserIdAndBookId(userPrincipal.getId(), bookId);

		if (optionalUserBook.isEmpty()) {
			throw new UserBookNotFoundException("User has not added this book to their collection");
		}

		UserBook userBook = optionalUserBook.get();
		Review review = userBook.getReview();
		if (review != null) {
			reviewRepository.delete(review);
			log.info("Deleted review. bookId {} userId {}", bookId, userPrincipal.getId());
		} else
			throw new RatingNotFoundException(String.format("Rating not found. bookId: %d", bookId));
	}

	@Transactional(readOnly = true)
	public PaginatedResponse<MyReviewResponse> searchMyReviews(UserPrincipal userPrincipal,
			ReviewSearchRequest reviewSearchRequest) {
		Long userId = userPrincipal.getId();

		Pageable pageable = this.buildPageable(reviewSearchRequest);

		Page<MyReviewResponse> myReviewResponsePage = reviewRepository.searchMyReviews(
				userId,
				reviewSearchRequest.getTitle(),
				reviewSearchRequest.getAuthor(),
				reviewSearchRequest.getKeyword(),
				pageable);
		return PaginatedResponse.build(myReviewResponsePage);
	}

	@Transactional(readOnly = true)
	public PaginatedResponse<ReviewResponse> searchReviews(ReviewSearchRequest reviewSearchRequest) {
		Pageable pageable = this.buildPageable(reviewSearchRequest);

		Page<ReviewResponse> reviewResponsePage = reviewRepository.searchReviews(
				reviewSearchRequest.getTitle(),
				reviewSearchRequest.getAuthor(),
				reviewSearchRequest.getKeyword(),
				pageable);

		return PaginatedResponse.build(reviewResponsePage);
	}

	private Pageable buildPageable(ReviewSearchRequest reviewSearchRequest) {
		String sortBy = reviewSearchRequest.getSortBy();

		String sortField = switch (sortBy) {
			case "title" -> "book.title";
			case "author" -> "book.author";
			case "createdAt" -> "createdAt";
			default -> "id";
		};

		Sort sort = Sort.by(Sort.Order.by(sortField)
				.with(Sort.Direction.fromString(reviewSearchRequest.getSortOrder())));

		return PageRequest.of(Math.max(reviewSearchRequest.getPage() - 1, 0), reviewSearchRequest.getSize(), sort);
	}
}
