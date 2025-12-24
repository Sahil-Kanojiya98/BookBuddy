package com.example.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookSearchRequest {

    private String title;
    private String author;

    @Min(1)
    private Integer minRating;

    @Min(1000)
    private Integer publishedYearFrom;

    @Pattern(regexp = "title|author|rating|publishedYear")
    private Integer publishedYearTo;

    private String sortBy = "title";

    @Pattern(regexp = "asc|desc")
    private String sortOrder = "asc";

    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 10;
}

