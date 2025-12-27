package com.example.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewSearchRequest {

	@Positive(message = "Book ID must be a positive number")
	private Long bookId;

	private String title;

	private String author;

	private String keyword;

	@Pattern(regexp = "id|title|author|createdAt", message = "Invalid sort field. Choose from: title, author, createdAt")
	private String sortBy = "id";

	@Pattern(regexp = "asc|desc", message = "Sort order must be either 'asc' or 'desc'")
	private String sortOrder = "asc";

	@Min(value = 0, message = "Page must be 0 or greater")
	private Integer page = 0;

	@Min(value = 1, message = "Size must be at least 1")
	private Integer size = 10;
}
