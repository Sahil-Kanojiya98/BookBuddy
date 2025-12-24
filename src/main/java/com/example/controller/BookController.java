package com.example.controller;

import com.example.dto.request.BookSearchRequest;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.BookResponse;
import com.example.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(
            @PathVariable("id") Long id
    ) {
        BookResponse bookResponse = bookService.getBooksById(id);
        return ApiResponse.build(HttpStatus.OK, bookResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooksBySearch(
            @ModelAttribute BookSearchRequest bookSearchRequest
    ) {
        List<BookResponse> bookResponseList = bookService.searchBooks(bookSearchRequest);
        return ApiResponse.build(HttpStatus.OK, bookResponseList);
    }
}
