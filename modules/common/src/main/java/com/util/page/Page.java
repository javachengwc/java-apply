package com.util.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Page<T> implements Serializable, Iterable<T> {
	
	private static final long serialVersionUID = 1722272400736470203L;

	private static int DEFAULT_PAGESIZE = 10;

	protected List<T> result;

	protected int pageSize;

	protected int pageNo;

	protected int totalCount = 0;

	protected String order;

	protected String orderBy;

	public Page() {
		this(0, DEFAULT_PAGESIZE, 0);
	}

	public Page(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0, new ArrayList<T>(0));
	}

	public Page(int pageNo, int pageSize, int totalCount) {
		this(pageNo, pageSize, totalCount, new ArrayList<T>(0));
	}

	public Page(int pageNumber, int pageSize, int totalCount, List<T> result) {
		this.pageNo = pageNumber;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		setResult(result);
	}

	/**
	 * 页内容列表数量
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 第pageNo页
	 */
	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 分页里面的具体列表，因为是用到泛型，此列表的具体类型见Controller接口,已注明
	 */
	public List<T> getResult() {
		return this.result;
	}

	public void setResult(List<T> elements) {
		if (elements == null)
			return;
		this.result = elements;
	}

	/**
	 * 总数量
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isFirstPage() {
		return getPageNo() == 1;
	}

	public boolean isLastPage() {
		return getPageNo() >= getLastPageNo();
	}

	public boolean isHasNextPage() {
		return getLastPageNo() > getPageNo();
	}

	public boolean isHasPreviousPage() {
		return getPageNo() > 1;
	}

	public int getLastPageNo() {
		int result = totalCount % pageSize == 0 ? totalCount / pageSize
				: totalCount / pageSize + 1;
		if (result <= 1)
			result = 1;
		return result;
	}

	public int getPageFirstElementNo() {
		return (getPageNo() - 1) * getPageSize() + 1;
	}

	public int getPageLastElementNo() {
		int fullPage = getPageFirstElementNo() + getPageSize() - 1;
		return getTotalCount() < fullPage ? getTotalCount() : fullPage;
	}

	public int getNextPageNo() {
		return getPageNo() + 1;
	}

	public int getPreviousPageNo() {
		return getPageNo() - 1;
	}

	public int getFirstResult() {
		if (pageSize <= 0)
			return 0;
		int result = (pageNo - 1) * pageSize;
		return result > 0 ? result : 0;
	}
	public int getNextPageFirstResult()
	{
		return getFirstResult()+pageSize;
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return this.result == null ? Collections.EMPTY_LIST.iterator()
				: this.result.iterator();
	}
	
}
