package com.example.controller;

import com.example.config.SwaggerConfig;
import com.example.dto.request.BookSearchRequest;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.BookResponse;
import com.example.dto.response.PaginatedResponse;
import com.example.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
public class BookController {

	private final BookService bookService;

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BookResponse>> getBookById(
			@PathVariable("id") Long id) {
		log.info("Get book by id request received. bookId: {}", id);
		BookResponse bookResponse = bookService.getBooksById(id);
		return ApiResponse.build(HttpStatus.OK, bookResponse);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<PaginatedResponse<BookResponse>>> searchBooks(
			@Valid @ModelAttribute BookSearchRequest bookSearchRequest) {
		log.info("Get books request received. bookSearchRequest: {}", bookSearchRequest);
		PaginatedResponse<BookResponse> paginatedBookResponse = bookService.searchBooks(bookSearchRequest);
		return ApiResponse.build(HttpStatus.OK, paginatedBookResponse);
	}
}
