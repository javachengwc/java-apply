package com.solr.model;

import java.util.ArrayList;

/**
 * 左侧分类VO
 */
public class LeftCatsVo {
	
	private CatInfo parentCat;
	private ArrayList<CatInfo> subCatList;
	public LeftCatsVo(){
		parentCat = new CatInfo();
		subCatList = new ArrayList<CatInfo>();
	}

	public CatInfo getParentCat() {
		return parentCat;
	}

	public void setParentCat(CatInfo parentCat) {
		this.parentCat = parentCat;
	}

	public ArrayList<CatInfo> getSubCatList() {
		return subCatList;
	}

	public void setSubCatList(ArrayList<CatInfo> subCatList) {
		this.subCatList = subCatList;
	}
	@Override
	public String toString() {
		return "LeftCatsVo [subCatList=" + subCatList + "]";
	}
}