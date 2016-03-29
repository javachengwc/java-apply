package com.util.page;

import java.util.ArrayList;
import java.util.List;

public class CollectionPage<T> extends Page<T> {

	private static final long serialVersionUID = -7697183711484931545L;

	public CollectionPage(List<T> list, int pageNo, int pageSize) {
		this.totalCount = (list==null)?0:list.size();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		innerAdjust();
		List<T> resultList = new ArrayList<T>();
		for (int i = this.getFirstResult(); i < getNextPageFirstResult(); i++) {
			if(totalCount>i)
			{
			    resultList.add(list.get(i));
			}
		}
		this.setResult(resultList);
	}

	public void innerAdjust() {
		if (pageNo <= 0)
			pageNo = 1;
		int totalPageCount = getLastPageNo();
		if (pageNo > totalPageCount) {
			pageNo = totalPageCount;
		}
	}
}
