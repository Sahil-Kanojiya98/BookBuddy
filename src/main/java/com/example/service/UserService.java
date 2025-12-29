package com.example.service;

import com.example.dto.request.AddBookRequest;
import com.example.dto.request.BookSearchRequest;
import com.example.dto.request.UpdateBookStatusRequest;
import com.example.dto.response.PaginatedResponse;
import com.example.dto.response.UserBookResponse;
import com.example.dto.response.UserResponse;
import com.example.exception.BookNotFoundException;
import com.example.exception.UserBookAlreadyExistsException;
import com.example.exception.UserBookNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.Book;
import com.example.model.User;
import com.example.model.UserBook;
import com.example.repository.BookRepository;
import com.example.repository.UserBookRepository;
import com.example.repository.UserRepository;
import com.example.security.authentication.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserBookRepository userBookRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;

	public UserResponse getMe(UserPrincipal userPrincipal) {
		return new UserResponse(
				userPrincipal.getId(),
				userPrincipal.getEmail(),
				userPrincipal.getUsername());
	}

	@Transactional(readOnly = true)
	public UserBookResponse getMyLibraryBookById(UserPrincipal userPrincipal, Long bookId) {
		Long userId = userPrincipal.getId();

		Optional<UserBookResponse> optionalUserBookResponse = userBookRepository
				.findUserBookResponseByUserIdAndBookId(userId, bookId);
		if (optionalUserBookResponse.isEmpty())
			throw new UserBookNotFoundException(
					String.format("UserBook not found with userId %d bookId %d", userId, bookId));

		return optionalUserBookResponse.get();
	}

	@Transactional(readOnly = true)
	public PaginatedResponse<UserBookResponse> searchMyLibraryBooks(UserPrincipal userPrincipal,
			BookSearchRequest bookSearchRequest) {
		Long userId = userPrincipal.getId();
		String title = bookSearchRequest.getTitle() != null ? bookSearchRequest.getTitle().trim().toLowerCase() : null;
		String author = bookSearchRequest.getAuthor() != null ? bookSearchRequest.getAuthor().trim().toLowerCase()
				: null;

		String sortBy = bookSearchRequest.getSortBy();

		String sortField = switch (sortBy) {
			case "title" -> "b.title";
			case "author" -> "b.author";
			case "rating" -> "b.averageRating";
			case "publishedYear" -> "b.publishedYear";
			default -> "id";
		};

		Sort sort = Sort.by(Sort.Order.by(sortField)
				.with(Sort.Direction.fromString(bookSearchRequest.getSortOrder())));
		PageRequest pageRequest = PageRequest.of(Math.max(bookSearchRequest.getPage() - 1, 0),
				bookSearchRequest.getSize(), sort);

		Page<UserBookResponse> userBookResponsePage = userBookRepository.searchMyLibraryBooks(
				userId, title, author, bookSearchRequest.getMinRating(), bookSearchRequest.getPublishedYearFrom(),
				pageRequest);
		return PaginatedResponse.build(userBookResponsePage);
	}

	@Transactional
	public void addBookToMyLibrary(UserPrincipal userPrincipal, AddBookRequest addBookRequest) {
		Long userId = userPrincipal.getId();
		Long bookId = addBookRequest.getBookId();

		if (userBookRepository.findByUserIdAndBookId(userId, bookId).isPresent())
			throw new UserBookAlreadyExistsException(
					String.format("Book with id %d is already in user's library", bookId));

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(String.format("User not found with id %d", userId)));

		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new BookNotFoundException(String.format("Book not found with id %d", bookId)));

		UserBook userBook = new UserBook();
		userBook.setUser(user);
		userBook.setBook(book);
		userBook.setStatus(addBookRequest.getStatus());
		userBookRepository.save(userBook);
	}

	@Transactional
	public void removeBookFromMyLibrary(UserPrincipal userPrincipal, Long bookId) {
		Long userId = userPrincipal.getId();

		Optional<UserBook> optionalUserBook = userBookRepository.findByUserIdAndBookId(userId, bookId);

		optionalUserBook.ifPresent(userBookRepository::delete);
	}

	@Transactional
	public void updateMyLibraryBookStatus(UserPrincipal userPrincipal, Long bookId,
			UpdateBookStatusRequest updateBookStatusRequest) {
		Long userId = userPrincipal.getId();

		Optional<UserBook> optionalUserBook = userBookRepository.findByUserIdAndBookId(userId, bookId);

		optionalUserBook.ifPresent(userBook -> {
			userBook.setStatus(updateBookStatusRequest.getStatus());
			userBookRepository.save(userBook);
		});
	}
}
