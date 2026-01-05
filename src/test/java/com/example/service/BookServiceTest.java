package com.example.service;

import com.example.dto.response.BookResponse;
import com.example.repository.BookRepository;
import com.example.validation.ValidatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceTest {

    @MockitoBean
    ValidatorService validatorService;

    @MockitoBean
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Test
    void getBooksById_validId_returnsBook() {
        Long bookId = 1L;

        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(bookId);
        bookResponse.setTitle("Clean Code");

        when(bookRepository.findBookResponseById(bookId)).thenReturn(Optional.of(bookResponse));

        BookResponse response = bookService.getBooksById(bookId);

        assertNotNull(response);
        assertEquals(bookId, response.getId());
        assertEquals("Clean Code", response.getTitle());
    }
}