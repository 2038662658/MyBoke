package com.cakes.frameworks.util;

import java.util.Set;

import com.cakes.cms.wx.bean.User;

public class WxComm {
	/**
	 * 获取微信数据权限SQL
	 * @param maintable_pre  表前缀
	 * @return
	 */
	public static String getWxDataLimitSql(String maintable_relcol, User user) {
		String whereWxDataLimit  = "";
		Set<String> roleSet = user.getRoleSet();
		//微信管理员和super可以看所有
		if ( roleSet.contains("wxadm") || roleSet.contains("super")) {//可以看所有
			return "";
		} 
		//查看单个微信公众号

//		whereWxDataLimit = " and exists ( select 1 from sys_staff_role r where concat('wx_', "a.pno_code" )=r.role_id and r.staff_id=? ) ";
		whereWxDataLimit = " and exists ( select 1 from sys_staff_role r where concat('wx_', "+maintable_relcol+")=r.role_id  and r.staff_id='"+user.getUser()+"'  ) ";
		
		return whereWxDataLimit;
	}

}
