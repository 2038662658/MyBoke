package com.cakes.frameworks.util;

public class AjaxParamInfo {
	private boolean isArray;
	private String str;
	private String[] arr;
	
	public AjaxParamInfo(){
		
	}
	
	public AjaxParamInfo(String str){
		this.isArray=false;
		this.str=str;
	}
	
	public AjaxParamInfo(String[] arr){
		this.isArray=true;
		this.arr=arr;
	}
	
	public boolean isArray() {
		return isArray;
	}
	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String[] getArr() {
		return arr;
	}
	public void setArr(String[] arr) {
		this.arr = arr;
	}
	
	
	
}
