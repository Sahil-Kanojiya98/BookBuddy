package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

	private Long id;
	private String title;
	private String author;
	private String isbn;
	private String description;
	private Integer publishedYear;
	private Double averageRating;
	private Integer ratingCount;
}
