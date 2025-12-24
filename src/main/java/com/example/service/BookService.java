package com.example.service;

import com.example.dto.request.BookSearchRequest;
import com.example.dto.response.BookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    @Transactional(readOnly = true)
    public BookResponse getBooksById(Long id) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> searchBooks(BookSearchRequest bookSearchRequest) {
        return List.of();
    }
}
