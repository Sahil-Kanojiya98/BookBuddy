package com.example.constant;

public class RateLimitConstant {

	// Define API paths
	public static final String LOGIN_API_PATH = "/api/v1/auth/login";
	public static final String SEARCH_API_PATH = "/api/v1/books/search";
	public static final String PUBLIC_API_PATH_REGX = "/api/v1/public/**"; // Exclude public paths

	// Rate limit thresholds
	public static final int LOGIN_RATE_LIMIT = 5; // 5 requests per second for login
	public static final int SEARCH_RATE_LIMIT = 50; // 50 requests per second for search

	// User ID request attribute key
	public static final String USER_ID_ATTRIBUTE = "userId";

	// Rate limit error messages
	public static final String LOGIN_RATE_LIMIT_EXCEEDED_MSG = "Rate limit exceeded for login. Try again later.";
	public static final String SEARCH_RATE_LIMIT_EXCEEDED_MSG = "Rate limit exceeded for book search. Try again later.";
	public static final String USER_ID_INVALID_MSG = "Invalid user rate limit exceeded.";
}
