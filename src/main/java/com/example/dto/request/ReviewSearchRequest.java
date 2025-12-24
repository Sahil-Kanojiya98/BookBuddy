package com.example.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewSearchRequest {

    @Positive
    private Long bookId;

    private String title;
    private String author;
    private String keyword;

    @Pattern(regexp = "title|author|createdAt")
    private String sortBy = "title";

    @Pattern(regexp = "asc|desc")
    private String sortOrder = "asc";

    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 10;
}
