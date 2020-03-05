package com.cakes.frameworks.util;

import java.util.List;
import java.util.Map;

/**
 * 分页bean类
 * @author 剑锋
 * 2016年6月4日 11:45:07
 * @param <T>
 */
public class PageJsonBean<T> {
	private long total;// 总数
	private long pages;// 总页数    暂时没实现
	private long curr_page;// 当前页数  暂时没实现
	private List<T> rows;// 数据集合
	
	/*构造函数*/
	public PageJsonBean(long total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}
	public PageJsonBean(long total, List<T> rows, List<String> categories, List<Map<String,Object>> series) {
		this.total = total;
		this.rows = rows;
	}
	public PageJsonBean(long total, List<T> rows, long pages) {
		this.total = total;
		this.rows = rows;
		this.pages = pages;
	}
	

	
	/*getter setter*/
	public long getTotal() {return total;}
	public void setTotal(long total) {
		this.total = total;
	}
	
	public long getPages() {return pages;}
	public void setPages(long pages) {
		this.pages = pages;
	}
	
	public long getCurr_page() {return curr_page;}
	public void setCurr_page(long curr_page) {
		this.curr_page = curr_page;
	}
	
	public List<T> getRows() {return rows;}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
