package com.example.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RatingRequest {

	@NotNull(message = "Rating value cannot be null")
	@Min(value = 1, message = "Rating value must be at least 1")
	@Max(value = 5, message = "Rating value must be at most 5")
	private Integer value;
}
