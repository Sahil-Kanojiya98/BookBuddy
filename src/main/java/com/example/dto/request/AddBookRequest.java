package com.example.dto.request;

import com.example.model.constant.ReadingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddBookRequest {

    @NotNull
    @Positive
    private Long bookId;

    @NotNull
    private ReadingStatus status;
}