package com.example.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookSearchRequest {

    private String title;
    private String author;
    private Integer minRating;
    private Integer publishedYearFrom;
    private Integer publishedYearTo;
    private String sortBy = "title";
    private String sortOrder = "asc";
    private Integer page = 0;
    private Integer size = 10;
}

