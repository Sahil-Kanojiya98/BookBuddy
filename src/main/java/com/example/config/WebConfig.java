package com.example.config;

import com.example.interceptor.RateLimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.constant.RateLimitConstant.LOGIN_API_PATH;
import static com.example.constant.RateLimitConstant.PUBLIC_API_PATH_REGX;
import static com.example.constant.RateLimitConstant.SEARCH_API_PATH;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final RateLimitInterceptor rateLimitInterceptor;

	public WebConfig(RateLimitInterceptor rateLimitInterceptor) {
		this.rateLimitInterceptor = rateLimitInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(rateLimitInterceptor)
				.addPathPatterns(LOGIN_API_PATH, SEARCH_API_PATH)
				.excludePathPatterns(PUBLIC_API_PATH_REGX);
	}
}
