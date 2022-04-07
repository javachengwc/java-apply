package com.manage.util;


import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	
	public static void main(String[] args) {
		List<String> lStrings =  new ArrayList<String>();
		for (int i = 0; i < 11; i++) {
			lStrings.add(i+"");
			System.out.print(lStrings.get(i)+"\t");
		}
		System.out.println();
		Page<String> page = new Page<String>();
		List aList = page.getPage(lStrings, 6, -5);
		for (int i = 0; i <aList.size(); i++) {
			System.out.println(aList.get(i));
		}
	}

	public Page(){
		
	}
	public Page(List<T> list,int currentPage,int numOfPage){
		sumCount = list.size();
		if(numOfPage<=0){
			numOfPage = 2 ;
		}
		sumPage = sumCount % numOfPage == 0 ? sumCount / numOfPage : sumCount / numOfPage +1 ;
		if(currentPage <= 1){
			currentPage = 1;
		}
		if(currentPage >= sumPage){
			currentPage = sumPage;
		}
	}
	/**
	 * 
	 * @Description: 
	 * @param @param list	需分页的列表
	 * @param @param currentPage	当前页
	 * @param @param numOfPage     每页多少条
	 * @throws 
	 * @author  channing
	 * Create time 2011-11-7
	 */
	public List<T> getPage(List<T> list,int currentPage,int numOfPage){
		List<T> resultList = new ArrayList<T>();
		sumCount = list.size();
		if(numOfPage<=0){
			numOfPage = 2 ;
		}
		sumPage = sumCount % numOfPage == 0 ? sumCount / numOfPage : sumCount / numOfPage +1 ;
		if(currentPage <= 1){
			currentPage = 1;
		}
		if(currentPage >= sumPage){
			currentPage = sumPage;
		}
		if(currentPage > 0){
			for (int i = (currentPage-1) * numOfPage ;i<(currentPage * numOfPage > sumCount ? sumCount : currentPage * numOfPage );i++) {
				resultList.add(list.get(i));
			}
		}
		
		return resultList;
	}
	
	/**
	 * 分页
	 */
	//每页显示条数
	int numOfPage = 2;
	//共多少条
	int sumCount = 0;
	
	public int getSumCount() {
		return sumCount;
	}
	 
	public int getSumPage() {
		return sumPage;
	}
	//共多少页
	int sumPage = 0;
	//当前第几页
	int currentPage = 1;
	
	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}
	public int getCurrentPage(){
		if(currentPage <= 1){
			currentPage = 1;
		}
		if(currentPage >= sumPage){
			currentPage = sumPage;
		}
		return currentPage;
	}
	public int getNumOfPage() {
		if(numOfPage<=0){
			numOfPage = 2 ;
		}
		return numOfPage;
	}

	public void setNumOfPage(int numOfPage) {
		this.numOfPage = numOfPage;
	}

	public void setSumCount(int sumCount) {
		this.sumCount = sumCount;
	}

	public void setSumPage(int sumPage) {
		this.sumPage = sumPage;
	}

}
