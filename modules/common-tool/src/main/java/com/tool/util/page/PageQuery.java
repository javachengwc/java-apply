package com.tool.util.page;

public class PageQuery<T> {

	private static int DEFAULT_PAGESIZE = 10;

	private int pageNo;

	private int pageSize;

	private int start;

	private T entity;
	
	private String table;
	
	private String order;

	private String orderBy;

	public PageQuery() {
		this(0);
	}

	public PageQuery(int pageNo) {
		this(pageNo, DEFAULT_PAGESIZE);
	}

	public PageQuery(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;

		if (pageSize <= 0)
			start = 0;
		else {
			int result = (pageNo - 1) * pageSize;
			start = result > 0 ? result : 0;
		}
	}

	public PageQuery(int pageNo, int pageSize, T entity) {
		this(pageNo, pageSize);
		this.entity = entity;
	}
	
	public PageQuery(int pageNo, int pageSize, T entity,String table) {
		this(pageNo, pageSize,entity);
		this.table=table;
	}
	

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int genStart() {
		
		if (pageSize <= 0)
			start = 0;
		else {
			int result = (pageNo - 1) * pageSize;
			start = result > 0 ? result : 0;
		}
		
		return start;
	}

	public int getStart() {
		return start;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
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
	
	public String popTable(Extractor<T> extractor)
	{
		return extractor.pop(entity);
	}
	
	public interface Extractor<T>{
		public String pop(T entity);
	}
}
