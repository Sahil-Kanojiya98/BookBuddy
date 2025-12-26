package com.example.interceptor;

import com.example.exception.RateLimitExceededException;
import com.example.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import static com.example.constant.RateLimitConstant.LOGIN_API_PATH;
import static com.example.constant.RateLimitConstant.LOGIN_RATE_LIMIT_EXCEEDED_MSG;
import static com.example.constant.RateLimitConstant.SEARCH_API_PATH;
import static com.example.constant.RateLimitConstant.SEARCH_RATE_LIMIT_EXCEEDED_MSG;
import static com.example.constant.RateLimitConstant.USER_ID_ATTRIBUTE;
import static com.example.constant.RateLimitConstant.USER_ID_INVALID_MSG;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

	private final RateLimiterService rateLimiterService;

	public RateLimitInterceptor(RateLimiterService rateLimiterService) {
		this.rateLimiterService = rateLimiterService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getRequestURI();

		if (uri.startsWith(LOGIN_API_PATH)) {
			String ipAddress = request.getRemoteAddr();

			if (!rateLimiterService.isLoginAllowed(ipAddress)) {
				throw new RateLimitExceededException(LOGIN_RATE_LIMIT_EXCEEDED_MSG);
			}
		} else if (uri.startsWith(SEARCH_API_PATH)) {
			Long userId = (Long) request.getAttribute(USER_ID_ATTRIBUTE);

			if (userId == null) {
				throw new RateLimitExceededException(USER_ID_INVALID_MSG);
			}

			if (!rateLimiterService.isSearchAllowed(String.valueOf(userId))) {
				throw new RateLimitExceededException(SEARCH_RATE_LIMIT_EXCEEDED_MSG);
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
