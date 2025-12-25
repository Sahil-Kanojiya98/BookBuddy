package com.example.validation;

import com.example.dto.request.LoginUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExactlyOneLoginIdentifierValidator implements ConstraintValidator<ExactlyOneLoginIdentifier, LoginUserRequest> {

    @Override
    public boolean isValid(LoginUserRequest request,
                           ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        boolean hasEmail = request.getEmail() != null && !request.getEmail().trim().isEmpty();
        boolean hasUsername = request.getUsername() != null && !request.getUsername().trim().isEmpty();

        return hasEmail ^ hasUsername;
    }
}
