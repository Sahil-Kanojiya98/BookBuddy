package com.example.validation;

import com.example.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidatorService {

    public void validatePositiveBookId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid Book Id. It must be a positive number greater than zero.");
        }
    }
}
