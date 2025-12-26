package com.example.service;

import com.example.dto.request.RatingRequest;
import com.example.dto.response.RatingResponse;
import com.example.exception.RatingNotFoundException;
import com.example.exception.UserBookNotFoundException;
import com.example.model.Book;
import com.example.model.Rating;
import com.example.model.UserBook;
import com.example.repository.RatingRepository;
import com.example.repository.UserBookRepository;
import com.example.security.authentication.UserPrincipal;
import com.example.validation.ValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RatingService {

	private final ValidatorService validatorService;
	private final UserBookRepository userBookRepository;
	private final RatingRepository ratingRepository;

	@Transactional
	public RatingResponse addOrUpdateRating(UserPrincipal userPrincipal, Long bookId, RatingRequest ratingRequest) {
		validatorService.validatePositiveBookId(bookId);

		Optional<UserBook> optionalUserBook = userBookRepository.findByUserIdAndBookId(userPrincipal.getId(), bookId);
		if (optionalUserBook.isEmpty()) {
			throw new UserBookNotFoundException("User has not added this book to their collection");
		}

		UserBook userBook = optionalUserBook.get();
		Rating rating = userBook.getRating();
		if (rating != null) {
			rating.setValue(ratingRequest.getValue());
			ratingRepository.save(rating);
			log.info("Updated rating. bookId {} userId {}", bookId, userPrincipal.getId());
		} else {
			rating = new Rating();
			rating.setUser(userBook.getUser());
			rating.setBook(userBook.getBook());
			rating.setUserBook(userBook);
			rating.setValue(ratingRequest.getValue());

			ratingRepository.save(rating);
			log.info("Added rating for book ID {} by user ID {}", bookId, userPrincipal.getId());
		}

		Book book = userBook.getBook();
		return new RatingResponse(bookId, ratingRequest.getValue(), book.getAverageRating(), book.getRatingCount());
	}

	@Transactional
	public void deleteRating(UserPrincipal userPrincipal, Long bookId) {
		validatorService.validatePositiveBookId(bookId);
		Optional<UserBook> optionalUserBook = userBookRepository.findByUserIdAndBookId(userPrincipal.getId(), bookId);

		if (optionalUserBook.isEmpty()) {
			throw new UserBookNotFoundException("User has not added this book to their collection");
		}

		UserBook userBook = optionalUserBook.get();
		Rating rating = userBook.getRating();
		if (rating != null) {
			ratingRepository.delete(rating);
			log.info("Deleted rating. bookId {} userId {}", bookId, userPrincipal.getId());
		} else
			throw new RatingNotFoundException(String.format("Rating not found. bookId: %d", bookId));
	}
}
