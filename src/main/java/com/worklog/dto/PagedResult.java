package com.worklog.dto;

import java.util.List;

public class PagedResult<T> {

	private final List<T> data;
	private final int totalCount;

	public PagedResult(List<T> data, int totalCount) {
		this.data = data;
		this.totalCount = totalCount;
	}

	public List<T> getData() {
		return data;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalPages(int size) {
		if (size <= 0)
			return 1;
		int pages = (int) Math.ceil((double) totalCount / size);
		return Math.max(1, pages);
	}

}
