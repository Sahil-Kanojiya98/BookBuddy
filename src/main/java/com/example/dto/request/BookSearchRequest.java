package com.example.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BookSearchRequest {

	private String title;

	private String author;

	@Min(value = 1, message = "Minimum rating should be at least 1")
	private Float minRating;

	@Min(value = 1000, message = "Published year from should be at least 1000")
	private Integer publishedYearFrom;

	@Pattern(regexp = "id|title|author|rating|publishedYear", message = "Invalid sort field. Choose from: title, author, rating, publishedYear")
	private String sortBy = "id";

	@Pattern(regexp = "asc|desc", message = "Sort order must be either 'asc' or 'desc'")
	private String sortOrder = "asc";

	@Min(value = 0, message = "Page must be 0 or greater")
	private Integer page = 0;

	@Min(value = 1, message = "Size must be at least 1")
	private Integer size = 10;
}
