package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponse<T> {

	private final List<T> content;
	private final int pageNumber;
	private final int pageSize;
	private final long totalElements;
	private final int totalPages;
	private final boolean last;

	public PaginatedResponse(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages,
			boolean last) {
		this.content = content;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.last = last;
	}

	public static <T> PaginatedResponse<T> build(List<T> content, int pageNumber, int pageSize, long totalElements,
			int totalPages, boolean last) {
		return new PaginatedResponse<>(content, pageNumber, pageSize, totalElements, totalPages, last);
	}

	public static <T> PaginatedResponse<T> build(Page<T> page) {
		return new PaginatedResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
				page.getTotalPages(), page.isLast());
	}
}
