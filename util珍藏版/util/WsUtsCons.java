package com.cakes.frameworks.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cakes.frameworks.service.CommonService;


@Controller
public class WsUtsCons {
	
		
	@Autowired
	CommonService provider;
	
	public static List<Map<String, Object>> MENULIST=new ArrayList<Map<String,Object>>();
	public static Map<String,String> CUSTKEYMAP=new HashMap<String, String>();
	
	public static List<Map<String, Object>> getMENULIST() {
		return MENULIST;
	}
	public static void setMENULIST(List<Map<String, Object>> mENULIST) {
		MENULIST = mENULIST;
	}
	
	public static Map<String, String> getCUSTKEYMAP() {
		return CUSTKEYMAP;
	}
	public static void setCUSTKEYMAP(Map<String, String> cUSTKEYMAP) {
		CUSTKEYMAP = cUSTKEYMAP;
	}
	public void initialize(){
		List<Map<String, Object>> menusObj;
		MENULIST.clear();
		try {
			menusObj=provider.queryBySql("SELECT * FROM s_product_type;", new String[]{});
			if(menusObj!=null && menusObj.size()>0){
				MENULIST=getParentChildren1(menusObj,MENULIST,"0");
				System.out.println("产品类型加载完成......");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
  }
	
	public List<Map<String, Object>> getParentChildren1(List<Map<String, Object>> fromList,
			List<Map<String, Object>> returnList,String parentId){
		for(Map<String, Object> menu:fromList){
			if(CommFun.nvl(menu.get("parent_menu_id")).equals(parentId)){
				menu.put("children", getParentChildren1(fromList,new ArrayList<Map<String,Object>>(),CommFun.nvl(menu.get("menu_id"))));
				returnList.add(menu);
			}
		}
		return returnList;
	}
}
