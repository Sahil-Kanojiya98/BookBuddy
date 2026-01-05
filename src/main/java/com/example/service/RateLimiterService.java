package com.example.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.example.constant.RateLimitConstant.LOGIN_RATE_LIMIT;
import static com.example.constant.RateLimitConstant.SEARCH_RATE_LIMIT;

@Service
public class RateLimiterService {

	private final Cache bucketCache;

	public RateLimiterService(CacheManager cacheManager) {
		this.bucketCache = cacheManager.getCache("rateLimitCache");
	}

	public boolean isLoginAllowed(String ip) {
		return isAllowed(getOrCreateBucket("login:" + ip, LOGIN_RATE_LIMIT));
	}

	public boolean isSearchAllowed(String userId) {
		return isAllowed(getOrCreateBucket("search:" + userId, SEARCH_RATE_LIMIT));
	}

	private Bucket getOrCreateBucket(String key, int limit) {
		Bucket bucket = bucketCache.get(key, Bucket.class);

		if (bucket == null) {
			bucket = createBucket(limit);
			bucketCache.put(key, bucket);
		}

		return bucket;
	}

	private Bucket createBucket(int limit) {
		// @formatter:off
		Bandwidth bandwidth = Bandwidth.builder()
                .capacity(limit)
                .refillGreedy(limit, Duration.ofSeconds(1))
                .build();

		return Bucket.builder()
                .addLimit(bandwidth)
                .build();
        // @formatter:on
	}

	private boolean isAllowed(Bucket bucket) {
		ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
		return probe.isConsumed();
	}
}
