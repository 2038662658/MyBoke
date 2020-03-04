package com.cakes.frameworks.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cakes.cms.admin.bean.Staff;
import com.cakes.cms.wx.bean.Cust;
import com.cakes.cms.wx.bean.User;
//import com.owftc.rpt.action.utils.WsUtils;
import com.cakes.frameworks.util.AjaxMethods;
import com.cakes.frameworks.util.AjaxParamInfo;
import com.cakes.frameworks.util.CommFun;
import com.cakes.frameworks.util.CustException;
import com.cakes.frameworks.util.GlbCons;

public class AjaxMethods {
	
	//2017年6月26日 18:47:30
	/**
	 * 获取app user_id, 微信在登录的时候会直接写入session
	 * 微信关注后也会直接写入  t_reg_user 
	 * @return
	 */
	public static String getLoginAppUserId(){
		Object app_user = getHttpSession().getAttribute("app_user");
		Map<String,Object> rm = new HashMap<String,Object>();
		String user_id = "";
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
			user_id = CommFun.nvl(rm.get("id"));
		}
		return user_id;//部署放开
		//return "96eef4bbf6454bd4b785c8f23a4b8282";
	}
	
	
	public static String getLoginOpenId(){
		String pno_code = "1001";
		Object openid = (String) getHttpSession().getAttribute("openid"+pno_code);
		return CommFun.nvl(openid);
	}
	
	
	/**
	 * 获取app phone, 微信在登录的时候会直接写入session
	 * 周超
	 * 2017-6-19上午10:10:56
	 */
	public static String getLoginAppUserMobile(){
		Object app_user = getHttpSession().getAttribute("app_user");
		Map<String,Object> rm = new HashMap<String,Object>();
		String user_phone = "";
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
			user_phone = CommFun.nvl(rm.get("user_phone"));
		}
		//return "12345678909";//测试
		return user_phone;//部署放开
	}
	
	
	
	/**
	 * 获取 后台 登录 staff 账户 id
	 * 剑锋
	 * 2017年6月26日 19:20:16
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getLoginStaffId(){
		Object staff = getHttpSession().getAttribute("staff");
		Map<String,Object> rm = new HashMap<String,Object>();
		String staff_id = "";
		if ( staff != null ) {
			rm = ( Map<String,Object> ) staff;
			staff_id = CommFun.nvl(rm.get("id"));
		}
		return staff_id;
	}
	
	
	
	/**
	 * 设置 setAppUser2Session 
	 * @param userMap
	 */
	public static void setAppUser2Session(Map<String,Object>  userMap) {
		
		//String avatar = CommFun.nvl(userMap.get("avatar"));
		//avatar = WsUtils.getWebUrl(avatar);
		//userMap.put("avatar", avatar);
		//String filter = CommFun.nvl(userMap.get("filter"));
		//if ( CommFun.isNotEmpty(filter)) {
		//	System.out.println("--------------------setAppUser2Session----------------------filter:"+filter);
		//}
		userMap.put("last_upd_time", System.currentTimeMillis());//记录上次更新时间
		AjaxMethods.getHttpSession().setAttribute("app_user", userMap);
	}
	
	
	public static void  removeAppUser4Session() {
		AjaxMethods.getHttpSession().removeAttribute("app_user");
	}
	
	
	public static void  removeStaff4Session() {
		AjaxMethods.getHttpSession().removeAttribute("staff");
	}
	//2017年6月26日 18:47:37
	
	
	
	
	/**
	 * @param userMap
	 */
	public static void setAppAccount2Session(Map<String,Object>  userMap) {
		
		//String avatar = CommFun.nvl(userMap.get("avatar"));
		//avatar = WsUtils.getWebUrl(avatar);
		//userMap.put("avatar", avatar);
		//String filter = CommFun.nvl(userMap.get("filter"));
		//if ( CommFun.isNotEmpty(filter)) {
		//	System.out.println("--------------------setAppUser2Session----------------------filter:"+filter);
		//}
		userMap.put("last_upd_time", System.currentTimeMillis());//记录上次更新时间
		AjaxMethods.getHttpSession().setAttribute("app_account", userMap);
	}
	
	
	public static void  removeAppAccount4Session() {
		AjaxMethods.getHttpSession().removeAttribute("app_account");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void sendMsg(HttpServletResponse response, String content)  {
//        HttpServletResponse response = AjaxMethods.getResponse();
        response.setCharacterEncoding("utf8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=null;
//        System.out.println(content);
		try {
			out = response.getWriter();
			out.print(content);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null)
				out.close();
		}
    }
	
	
	/**
	 * 获取加密的user_id
	 * 前台user_id :   user_id+时间戳或者其他参数 加密传进来，待定
	 * @param user_id
	 * @return
	 */
	public static String getParamUserId(String user_id ) {
		String re_user_id = user_id;
		/**
		 * 解密逻辑，未处理		
		 */
				
		return re_user_id;
		
	}
	
	public static String ajax(String name){
		if(name==null)
			return "";
		try {
			return java.net.URLDecoder.decode(name,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return name;
		}
	}
	
	public static String encode(String name){
		if(name==null)
			return "";
		try {
			return java.net.URLEncoder.encode(name,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return name;
		}
	}
	
	public static Map<String,String> getParamMapStr(){
		HttpServletRequest req= AjaxMethods.getRequest();//ServletActionContext.getRequest();
		try {
			req.setCharacterEncoding("utf-8");//注意编码
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String,String[]> map=req.getParameterMap();
		Map<String,String> re=new HashMap<String, String>();
		for (String str : map.keySet()) {
//			AjaxParamInfo info=new AjaxParamInfo();
			String[] valarr=(String[])map.get(str);
//			System.out.println(str);
			if(str.endsWith("[]")){//数组
//				for(String val:valarr){
//					System.out.println(val);
//				}
//				info.setArray(true);
//				info.setArr(valarr);
//				re.put(str.substring(0,str.length()-2), info);
				re.put(str, valarr[0]);
			}else{
//				System.out.println(valarr[0]);
//				info.setArray(false);
//				info.setStr(valarr[0]);
				re.put(str, valarr[0]);
			}
//			System.out.println(str.toString()+":"+ajax(req.getParameter(str.toString())));
//			valMap.put(str.toString(), ajax(req.getParameter(str.toString())));
		}
		return re;
	}
	
	public static Map<String,AjaxParamInfo> getParamMap(){
		HttpServletRequest req= AjaxMethods.getRequest();//ServletActionContext.getRequest();
		try {
			req.setCharacterEncoding("utf-8");//注意编码
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String,String[]> map=req.getParameterMap();
		Map<String,AjaxParamInfo> re=new HashMap<String, AjaxParamInfo>();
		for (String str : map.keySet()) {
			AjaxParamInfo info=new AjaxParamInfo();
			String[] valarr=(String[])map.get(str);
			System.out.println(str);
			if(str.endsWith("[]")){//数组
//				for(String val:valarr){
//					System.out.println(val);
//				}
				info.setArray(true);
				info.setArr(valarr);
				re.put(str.substring(0,str.length()-2), info);
			}else{
//				System.out.println(valarr[0]);
				info.setArray(false);
				info.setStr(valarr[0]);
				re.put(str, info);
			}
//			System.out.println(str.toString()+":"+ajax(req.getParameter(str.toString())));
//			valMap.put(str.toString(), ajax(req.getParameter(str.toString())));
		}
		return re;
	}
	
	public static String getIpAddr() {
//		HttpServletRequest request= ServletActionContext.getRequest();
		HttpServletRequest request= AjaxMethods.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	
	public static boolean is_wechat () {
		boolean is_wechat = false;
		String ua = getRequest().getHeader("user-agent").toLowerCase();   
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
			is_wechat = true;
		}
		is_wechat = true;
		System.out.println(" is_wechat()  "+is_wechat);
		return is_wechat;
	}
	
	
	
	
//	public static String jsonVal(String val){
//		if(val==null) return "\"\"";
////		System.out.println(val);
////		String re=val.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\r", "\\\\r").replaceAll("\\n", "\\\\n").replaceAll("\"", "\\\\\"");
//		String re=(new JSONStringer()).value(val);
////		String re=JSONValue.toJSONString(val);
////		System.out.println(re);
//		return re;
//	}
	
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

//	public static HttpServletResponse getResponse() {
//		return RequestResponseContext
//
//	}

	public static HttpSession getHttpSession() {
		return getRequest().getSession();
	}

	
	public static User getLoginUserInfo(){
		return (User)getHttpSession().getAttribute(GlbCons.user);
	}
	public static Cust getLoginCustInfo(){
		return (Cust)getHttpSession().getAttribute(GlbCons.CUST);
	}
	
	
	public static boolean is_superadm () {
		boolean is_super = false;
		Set<String> roleSet=AjaxMethods.getLoginUserInfo().getRoleSet();
		if ( roleSet.contains("super")) {
			is_super = true; 
		}
		return is_super;
	}
	
	
	/**
	 * 获取登录用户session
	 * @return
	 */
	public static Map<String,Object> getCustUserInfo(){
		Object obj = getHttpSession().getAttribute(GlbCons.CUST);
		Map<String,Object> rmap = new HashMap<String,Object>();
		if ( obj != null ) {
			rmap = (Map<String,Object>)getHttpSession().getAttribute(GlbCons.CUST);
		}
		
		return rmap;
	}
	/**
	 * 获取登录用户id
	 * @return
	 */
	public static String getCustUserID() {
		
		Cust cust=null;
		Object obj = getHttpSession().getAttribute(GlbCons.CUST);
		String custId=null;
		if ( obj != null ) {
			cust = (Cust)obj;
			custId=CommFun.nvl(cust.getCust_code(), "");
		}
    	return custId;
    }
	
	/**
	 * 获取app登录用户session
	 * @return
	 */
	public static String getAppUserWeid() {
		
		Map<String,Object> rmap = new HashMap<String,Object>();
		Object obj = getHttpSession().getAttribute("app_user");
		if ( obj != null ) {
			rmap = (Map<String,Object>)obj;
			//System.out.println("chace app_user====="+rmap.toString());
		}
		String weid=CommFun.nvl(rmap.get("weid"), "");
		//System.out.println("user_id======"+user_id);
    	return weid;
    }
	
	/**
	 * 判定是否登录
	 * @return
	 */
	public static boolean appHasLoad() {
		
		String user_id = getLoginAppUserId();
		System.out.println(" appHasLoad  user_id " + user_id);
		
		if ( CommFun.isEmpty(user_id) || user_id.startsWith("-9999")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 获取登录页面
	 * @param user_type
	 * @return
	 */
	public static String getIfLoginFwd(String user_type) {
		String fwd = "";
		if ( appHasLoad() ) return "";
		//如果没登录
		if ( "1".equals(user_type)) {
			//fwd = "forward:/m/login/goLogin.do";
			// 未成为会员之前 应跳转到成为会员页面-如果已经是会员填充session
			fwd = "forward:/m/mbc/MbcAction/goMemberSingIn.do";
		} else if ( "2".equals(user_type)) {
			//代理商登陆     让庞同学改
			fwd = "forward:/m/coach/login.do";
		}
		return fwd;
	}
	
	
	
	
	/**
	 *  getAppUser2Session 
	 * @param userMap
	 */
	public static Map<String,Object>  getAppUser2Session() {
		Map<String,Object>  userMap = (Map<String,Object>)AjaxMethods.getHttpSession().getAttribute("app_user");
		
		return userMap;
	}
	
	
	
	/**
	 *  根据键值从session里面取值 
	 * @param userMap
	 */
	public static String  getAppUser4SessionByKey(String key) {
		Map<String,Object>  userMap = (Map<String,Object>)AjaxMethods.getHttpSession().getAttribute("app_user");
		String val = "";
		if ( CommFun.isNotEmpty(userMap)) {
			val = CommFun.nvl(userMap.get(key));
		}
		return val;
	}
	
	/**
	 * 获取app user
	 * @return
	 */
	public static Map<String,Object> getLoginAppUser(){
		Object app_user = getHttpSession().getAttribute("app_user");
		Map<String,Object> rm = new HashMap<String,Object>();
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
		}
		return rm;
	}
	
	
	
	
	
	
	
	/***  商户登录 的session 操作  start **********/
	/**
	 * 设置  dealer 的用户  setAppUser2Session 
	 * @param userMap
	 */
	public static void setDlrUser2Session(Map<String,Object>  userMap) {
		
		String avatar = CommFun.nvl(userMap.get("avatar"));
		avatar = WsUtils.getWebUrl(avatar);
		userMap.put("avatar", avatar);
		AjaxMethods.getHttpSession().setAttribute("dlr_user", userMap);
	}
	
	
	/**
	 * 获取Dlr user
	 * @return
	 */
	public static Map<String,Object> getLoginDlrUser(){
		Object app_user = getHttpSession().getAttribute("dlr_user");
		Map<String,Object> rm = new HashMap<String,Object>();
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
		}
		return rm;
	}
	
	/**
	 * 获取app user_id, 微信在登录的时候会直接写入session
	 * 微信关注后也会直接写入  t_reg_user 
	 * @return
	 */
	public static String getLoginDlrUserId(){
		Object app_user = getHttpSession().getAttribute("dlr_user");
		Map<String,Object> rm = new HashMap<String,Object>();
		String user_id = "";
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
			user_id = CommFun.nvl(rm.get("dlr_user_id"));
		}
		return user_id;
	}
	
	/***  商户登录 的session 操作  end **********/
	
	
	public static Map<String,Object> getCourseDetail(){
		Object obj = getHttpSession().getAttribute("courseDetail");
		Map<String,Object> rmap = new HashMap<String,Object>();
		if ( obj != null ) {
			rmap = (Map<String,Object>)getHttpSession().getAttribute("courseDetail");
		}
		
		return rmap;
	}
//	public static User getLoginStoreUser(){
//		return (User)getHttpSession().getAttribute("store_user");
//	}
	
	public static Set<String> getLoginUserRoleSet() throws CustException{
		User u = getLoginUserInfo();
		if(u==null) {
			throw new CustException("内网登录超时，请重新登录！");
		} else {
		
		}
		Set<String> roleSet= u.getRoleSet();
		return roleSet;
	}
	
	public static String getLoginUser() throws CustException{
		User u = getLoginUserInfo();
		if(u==null)
			throw new CustException("内网登录超时，请重新登录！");
		return u.getUser();
	}
	public static String getLoginCust() throws CustException{
		Cust u=getLoginCustInfo();
		if(u==null)
			throw new CustException("外网登录超时，请重新登录！");
		return u.getCust_code();
	}
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}
	
	
	public static String getLoginAppAccountType(){
		Object app_user = getHttpSession().getAttribute("app_account");
		Map<String,Object> rm = new HashMap<String,Object>();
		String user_id = "";
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
			user_id = CommFun.nvl(rm.get("login_type"));
		}
		return user_id;//部署放开
		//return "96eef4bbf6454bd4b785c8f23a4b8282";
	}
	
	public static Map<String,Object> getLoginAppAccountMap(){
		Object app_user = getHttpSession().getAttribute("app_account");
		Map<String,Object> rm = new HashMap<String,Object>();
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
		}
		return rm;
	}
	
	
	public static String getLoginAppUserType(){
		Object app_user = getHttpSession().getAttribute("app_user");
		Map<String,Object> rm = new HashMap<String,Object>();
		String user_id = "";
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
			user_id = CommFun.nvl(rm.get("login_type"));
		}
		return user_id;//部署放开
		//return "96eef4bbf6454bd4b785c8f23a4b8282";
	}
	
	public static Map<String,Object> getLoginAppUserMap(){
		Object app_user = getHttpSession().getAttribute("app_user");
		Map<String,Object> rm = new HashMap<String,Object>();
		if ( app_user != null ) {
			rm = ( Map<String,Object> ) app_user;
		}
		return rm;
	}
	
	
}
