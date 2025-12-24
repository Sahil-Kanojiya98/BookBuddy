package com.example.service;

import com.example.dto.request.AddBookRequest;
import com.example.dto.request.BookSearchRequest;
import com.example.dto.request.UpdateBookStatusRequest;
import com.example.dto.response.UserBookResponse;
import com.example.dto.response.UserResponse;
import com.example.repository.UserRepository;
import com.example.security.authentication.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getMe(UserPrincipal userPrincipal) {
        return new UserResponse(
                userPrincipal.getId(),
                userPrincipal.getEmail(),
                userPrincipal.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public UserBookResponse getMyLibraryBookById(UserPrincipal userPrincipal, Long bookId) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<UserBookResponse> searchMyLibraryBooks(UserPrincipal userPrincipal, BookSearchRequest bookSearchRequest) {
        return null;
    }

    @Transactional
    public void addBookToMyLibrary(UserPrincipal userPrincipal, AddBookRequest addBookRequest) {
    }

    @Transactional
    public void removeBookFromMyLibrary(UserPrincipal userPrincipal, Long userBookId) {
    }

    @Transactional
    public void updateMyLibraryBookStatus(UserPrincipal userPrincipal, Long userBookId, UpdateBookStatusRequest updateBookStatusRequest) {
    }
}
