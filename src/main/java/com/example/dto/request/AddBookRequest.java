package com.example.dto.request;

import com.example.model.constant.ReadingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddBookRequest {

    private Long bookId;
    private ReadingStatus status;
}