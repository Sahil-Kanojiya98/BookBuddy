package com.example.service;

import com.example.dto.request.BookSearchRequest;
import com.example.dto.response.BookResponse;
import com.example.dto.response.PaginatedResponse;
import com.example.exception.BookNotFoundException;
import com.example.repository.BookRepository;
import com.example.validation.ValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

	private final ValidatorService validatorService;
	private final BookRepository bookRepository;

	@Transactional(readOnly = true)
	@Cacheable(value = "books", key = "#id")
	public BookResponse getBooksById(Long id) {
		validatorService.validatePositiveBookId(id);
		Optional<BookResponse> optionalBookResponse = bookRepository.findBookResponseById(id);
		if (optionalBookResponse.isEmpty()) {
			throw new BookNotFoundException(String.format("Book not found. id: %d", id));
		}
		return optionalBookResponse.get();
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "booksSearch", key = "T(java.util.Objects).hash(#bookSearchRequest)")
	public PaginatedResponse<BookResponse> searchBooks(BookSearchRequest bookSearchRequest) {
		String title = bookSearchRequest.getTitle() != null ? bookSearchRequest.getTitle().trim().toLowerCase() : null;
		String author = bookSearchRequest.getAuthor() != null ? bookSearchRequest.getAuthor().trim().toLowerCase()
				: null;

		String sortField = switch (bookSearchRequest.getSortBy()) {
			case "title" -> "title";
			case "author" -> "author";
			case "rating" -> "averageRating";
			case "publishedYear" -> "publishedYear";
			default -> "id";
		};

		Sort sort = Sort.by(Sort.Order.by(sortField)
				.with(Sort.Direction.fromString(bookSearchRequest.getSortOrder())));
		PageRequest pageRequest = PageRequest.of(Math.max(bookSearchRequest.getPage() - 1, 0),
				bookSearchRequest.getSize(), sort);

		Page<BookResponse> bookResponsePage = bookRepository.searchBooks(
				title, author, bookSearchRequest.getMinRating(), bookSearchRequest.getPublishedYearFrom(), pageRequest);
		return PaginatedResponse.build(bookResponsePage);
	}
}
