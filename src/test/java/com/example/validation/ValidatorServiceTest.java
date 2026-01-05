package com.example.validation;

import com.example.exception.ValidationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorServiceTest {

	static ValidatorService validatorService;

	@BeforeAll
	static void setUp() {
		validatorService = new ValidatorService();
	}

	@Test
	void validatePositiveBookIdWithValidData() {
		assertDoesNotThrow(() -> validatorService.validatePositiveBookId(123L));
	}

	@Test
	void validatePositiveBookIdWithInvalidData() {
		assertThrows(ValidationException.class, () -> validatorService.validatePositiveBookId(-123L));
	}
}