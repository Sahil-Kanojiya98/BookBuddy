package com.example.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @Min(value = 1, message = "Minimum rating should be at least 1")
    private Integer minRating;

    @Min(value = 1000, message = "Published year from should be at least 1000")
    private Integer publishedYearFrom;

    @Pattern(regexp = "title|author|rating|publishedYear", message = "Invalid sort field. Choose from: title, author, rating, publishedYear")
    private String sortBy = "title";

    @Pattern(regexp = "asc|desc", message = "Sort order must be either 'asc' or 'desc'")
    private String sortOrder = "asc";

    @Min(value = 0, message = "Page must be 0 or greater")
    private Integer page = 0;

    @Min(value = 1, message = "Size must be at least 1")
    private Integer size = 10;
}
