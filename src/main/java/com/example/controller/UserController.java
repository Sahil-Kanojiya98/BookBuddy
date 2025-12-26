package com.example.controller;

import com.example.dto.request.AddBookRequest;
import com.example.dto.request.BookSearchRequest;
import com.example.dto.request.UpdateBookStatusRequest;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.PaginatedResponse;
import com.example.dto.response.UserBookResponse;
import com.example.dto.response.UserResponse;
import com.example.security.authentication.UserPrincipal;
import com.example.service.UserService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserResponse>> getMe(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		log.info("Get me request received. userId: {}", userPrincipal.getId());
		UserResponse userResponse = userService.getMe(userPrincipal);
		return ApiResponse.build(HttpStatus.OK, userResponse);
	}

	@GetMapping("/me/books/{id}")
	public ResponseEntity<ApiResponse<UserBookResponse>> getBookById(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PathVariable("id") Long bookId) {
		log.info("Get book by id request received. bookId: {}", bookId);
		UserBookResponse userBookResponse = userService.getMyLibraryBookById(userPrincipal, bookId);
		return ApiResponse.build(HttpStatus.OK, userBookResponse);
	}

	@GetMapping("/me/books/search")
	public ResponseEntity<ApiResponse<PaginatedResponse<UserBookResponse>>> searchMyBooks(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@Valid @ModelAttribute BookSearchRequest bookSearchRequest) {
		log.info("Search books request received. bookSearchRequest: {}", bookSearchRequest);
		PaginatedResponse<UserBookResponse> paginatedUserBookResponse = userService.searchMyLibraryBooks(userPrincipal,
				bookSearchRequest);
		return ApiResponse.build(HttpStatus.OK, paginatedUserBookResponse);
	}

	@PostMapping("/me/books")
	public ResponseEntity<ApiResponse<Void>> addBookToLibrary(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@RequestBody AddBookRequest addBookRequest) {
		log.info("Add book to library request received. addBookRequest: {}", addBookRequest);
		userService.addBookToMyLibrary(userPrincipal, addBookRequest);
		return ApiResponse.build(HttpStatus.CREATED, "Book added to library");
	}

	@PutMapping("/me/books/{userBookId}/status")
	public ResponseEntity<ApiResponse<Void>> updateBookStatus(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PathVariable("userBookId") Long userBookId,
			@RequestBody UpdateBookStatusRequest updateBookStatusRequest) {
		log.info("Update book status request received. userBookId: {} updateBookStatusRequest: {}", userBookId,
				updateBookStatusRequest);
		userService.updateMyLibraryBookStatus(userPrincipal, userBookId, updateBookStatusRequest);
		return ApiResponse.build(HttpStatus.OK, "Book status updated");
	}

	@DeleteMapping("/me/books/{userBookId}")
	public ResponseEntity<ApiResponse<Void>> removeBookFromLibrary(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PathVariable("userBookId") Long userBookId) {
		log.info("Remove book from library request received. userBookId: {}", userBookId);
		userService.removeBookFromMyLibrary(userPrincipal, userBookId);
		return ApiResponse.build(HttpStatus.NO_CONTENT, "Book removed from library");
	}
}
