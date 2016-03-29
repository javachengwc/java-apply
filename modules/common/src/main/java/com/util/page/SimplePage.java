package com.util.page;

import java.util.List;

public class SimplePage<T> extends Page<T> {

	private static final long serialVersionUID = -6421220395754922201L;

	public SimplePage(Page<?> page) {
		if (page.getOrder() != null)
			this.setOrder(page.getOrder());
		if (page.getOrderBy() != null)
			this.setOrderBy(page.getOrderBy());
		this.setPageNo(page.getPageNo());
		this.setPageSize(page.getPageSize());
		this.setTotalCount(page.getTotalCount());

	}

	public SimplePage(final List<T> es, Page<?> page) {
		this.setResult(es);
		if (page.getOrder() != null)
			this.setOrder(page.getOrder());
		if (page.getOrderBy() != null)
			this.setOrderBy(page.getOrderBy());
		this.setPageNo(page.getPageNo());
		this.setPageSize(page.getPageSize());
		this.setTotalCount(page.getTotalCount());

	}

}
