package com.example.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewSearchRequest {

    private Long bookId;
    private String title;
    private String author;
    private String keyword;
    private String sortBy = "title";
    private String sortOrder = "asc";
    private Integer page = 0;
    private Integer size = 10;
}
