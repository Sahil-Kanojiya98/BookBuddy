package com.example.dto.request;

import com.example.model.constant.ReadingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UpdateBookStatusRequest {

	@NotNull
	private ReadingStatus status;
}
