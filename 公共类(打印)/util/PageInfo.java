package com.cakes.frameworks.util;

public class PageInfo {
	private long totalRow;// 总行数
	private long totalPage;// 总页数
	private int currPage;// 当前页
	private int pageRow;// 页行数
	
	private int startRow;//开始行数 用mysql的limit时减1
	
	public PageInfo(){
		currPage=1;
	}
	
	public PageInfo(long totalRow, int pageRow, int currPage) {
		this.totalRow=totalRow;
		this.pageRow=pageRow;
		this.currPage=currPage;
		calc();
	}
	
	public void calc(){
		//总页数
		if(totalRow==0){
			this.totalPage=0;
//			this.startRow=1;
			this.startRow=0;
		}else{
			this.totalPage=(totalRow-1)/pageRow+1;
			if(this.totalPage<this.currPage)
				this.currPage=new Long(totalPage).intValue();
//			this.startRow=(currPage-1)*pageRow+1;//从1开始
			this.startRow=(currPage-1)*pageRow;//从1开始
		}
	}
	
	public boolean isLastPage(){
		return currPage==totalPage;
	}

	public int getStartRow() {
		return startRow;
	}

	public long getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(long totalRow) {
		this.totalRow = totalRow;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getPageRow() {
		return pageRow;
	}

	public void setPageRow(int pageRow) {
		this.pageRow = pageRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}


}
