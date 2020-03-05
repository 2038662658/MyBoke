package com.cakes.frameworks.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.util.IOUtils;
import com.cakes.cms.wechat.user.WeUserService;
import com.cakes.cms.wx.action.WechatService;
import com.cakes.frameworks.weixin.fastweixin.api.OauthAPI;
import com.cakes.frameworks.weixin.fastweixin.api.response.OauthGetTokenResponse;
import com.cakes.frameworks.weixin.wx.WxUtils;
import com.cakes.frameworks.cache.CacheData;
import com.cakes.frameworks.util.AjaxMethods;
import com.cakes.frameworks.util.CommFun;
import com.cakes.frameworks.util.Methods;


/**
 * 过滤器
 */
public class LoginAnnotationInterceptor extends HandlerInterceptorAdapter {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
	private WechatService wechatService;
    @Autowired
	private WeUserService weUserService;
    
    /**
     * 剑锋
     * 2017年4月17日 01:55:43
     * 免登录页面
     */
    private final static Set<String> noLoginUrl=new HashSet<String>();
    static{
    	noLoginUrl.add("wechat/forminfo/goLoginPage.go");
    	noLoginUrl.add("wechat/forminfo/goLoginAccountPage.go");
    }
    
    
    private final static Set<String> noDealUrl=new HashSet<String>();
    static{
    	noDealUrl.add("wechat/forminfo/goWarnPage.go");
    }
    
    /**
     * 剑锋
     * 2017年4月17日 01:55:33
     * 需要 微信 进行 处理的 业务逻辑。
     */
    private final static Set<String> wxAccountDealUrl=new HashSet<String>();
    static{
    	wxAccountDealUrl.add("wechat/forminfo/goLoginAccountPage.go");//业务员 登录页面
    }
    
    private final static Set<String> wxUserDealUrl=new HashSet<String>();
    static{
    	wxUserDealUrl.add("wechat/forminfo/goLoginPage.go");//用户 登录页面
    }
    
    
    
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
//    public String getParameter(String name) {
//        String[] results = (String[]) parameterMap.get(name);
//        if (results == null || results.length <= 0)
//            return null;
//        else {
//            //System.out.println("modify before：" + results[0]);
//            return modify(results[0]);
//        }
//    }
    
    private String modify(String string) {
        return string;
    }
    
    //private Map<String, String[]> parameterMap;
    
    @SuppressWarnings("unchecked")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//获取 访问的 URL 信息
    	String uri = request.getRequestURI();
		String prefix = request.getContextPath();
		StringBuffer url = request.getRequestURL();//
		
		if (prefix != null && uri.startsWith(prefix)) {
			uri = uri.substring(prefix.length() + 1);
		} else if (uri.startsWith("/")) {
			uri = uri.substring(1);
		}
		
		//parameterMap = request.getParameterMap();
		//Vector<String> vector = new Vector<>(parameterMap.keySet());
		
		//Map<String,Object> ret_map = new HashMap<String, Object>();
		
		//for(int i=0 ; i<vector.size() ; i++){
		//	String key = CommFun.nvl(vector.get(i));
		//	ret_map.put(key, CommFun.nvl(this.getParameter(key)));
		//}
		
		//@SuppressWarnings({ "static-access", "unused" })
		//String sr = this.sendPost("http://132.232.3.30:8099/getToken", "url='"+url.toString()+"'&wholeStr='"+JsonStringUtils.objectToJsonString(ret_map)+"'");
		
		//判断是否是微信浏览器  start
		boolean is_wechat = AjaxMethods.is_wechat();
		
		System.out.println(!noDealUrl.contains(uri));
		
		if(!is_wechat && !noDealUrl.contains(uri)){
			//提示 只能 在微信端 进行 显示操作！
			response.sendRedirect(prefix+"/wechat/forminfo/goWarnPage.go");
			return true;
		}else if(!is_wechat && noDealUrl.contains(uri)){
			//不在 微信浏览器 ，访问的是提示 页面。返回 true。
			return true;
		}
		
		
		//在 微信 客户端的 处理方式。
		
		//判断 是否 有 用户登录 状态，有 登录状态 直接 跳转 中心页面。无需多次登录。
		Map<String,Object> login_map_user = AjaxMethods.getLoginAppUserMap();
		Map<String,Object> login_map_account = AjaxMethods.getLoginAppAccountMap();
		
		//String login_type = CommFun.nvl(login_map_user.get("login_type"));
		//String login_code = CommFun.nvl(login_map_user.get("login_code"));
		//String login_name = CommFun.nvl(login_map_user.get("login_name"));
		
		if(wxAccountDealUrl.contains(uri)){
			if(CommFun.isNotEmpty(login_map_account)){
				response.sendRedirect(prefix+"/wechat/forminfo/goLoginAccountCenterPage.go");
				return true;
			}
		}else if(wxUserDealUrl.contains(uri)){
			if(CommFun.isNotEmpty(login_map_user)){
				response.sendRedirect(prefix+"/wechat/forminfo/goLoginUserCenterPage.go");
				return true;
			}
		}
		
		
		//微信业务需要处理的部分 
		if (is_wechat) {
			//获取微信 连接信息中的 pno_code
			//String pno_code = CommFun.nvl(request.getParameter("pno_code"));//微信Url 需要把url参数带上Pno_code
			//if ( CommFun.isEmpty(pno_code)) { //如果参数没传，则从session获取
			//	pno_code = CommFun.nvl(AjaxMethods.getHttpSession().getAttribute("pno_code"));
			//}
			
			String oid = CommFun.nvl(request.getParameter("oid"));//微信Url 需要把url参数带上Pno_code
			//System.out.println("oid  "+ oid);
			String pno_code = "1001";
			
			//存入 session　中
			AjaxMethods.getHttpSession().setAttribute("pno_code", pno_code);
			
			//可以在这里 
			String openid = "";
			
			System.out.println(CommFun.nvl(login_map_account).toString());
			System.out.println(CommFun.nvl(login_map_user).toString());
			
			if (noLoginUrl.contains(uri)) {
				return true;
			}else{
				
			}
				
				//如果 openid传入，判断没登录则把Session写入
				CacheData  cacheService  = (CacheData)SpringContextUtil.getBean("cacheData");
				
				request.getSession().setAttribute("openid"+pno_code, openid);
				AjaxMethods.getHttpSession().setAttribute("openid"+pno_code, openid);
				
				Map<String,Object> param_map = new HashMap<String, Object>();
				param_map.put("openid", openid);
				param_map.put("oid", oid);
				
				
				/*if ( AjaxMethods.appHasLoad()) { //如果已经登录了  判断session时间
					String time_out = CommFun.nvl( cacheService.getSetting("SESSION_TIME_OUT_WECHAT"), "1800" );//取不到则半小时
					
					System.out.println(" time_out   "+time_out);
					
					String last_upd_time = AjaxMethods.getAppUser4SessionByKey("last_upd_time");
					
					System.out.println(" last_upd_time   "+last_upd_time);
					
					boolean ifreloadData  = cacheService.getIfReloadCache(time_out, last_upd_time);
					
					System.out.println(" ifreloadData    "+ifreloadData);
					
					if ( ifreloadData ) {
						AjaxMethods.removeAppUser4Session();//清楚缓存
					}
				}*/
				
//				if ( !AjaxMethods.appHasLoad()) {//未登录，未获取到session
//					Map<String,Object> userMap = cacheService.getAppUserByOpenId(openid);//这个 方法 需要 修改
//					if ( CommFun.isNotEmpty(userMap)) {
//						userMap.put("filter", "yes");
//						userMap.put("last_upd_time", System.currentTimeMillis());//记录上次更新时间
//						AjaxMethods.setAppUser2Session(userMap);
//					} else {
//						AjaxMethods.removeAppUser4Session();//清楚缓存
//					}
//				}
				
				return true;
			}else{
				//CacheData  cacheService  = (CacheData)SpringContextUtil.getBean("cacheData");
				
				response.sendRedirect(prefix+"/wechat/forminfo/goWarnPage.go");
				return true;
			}
			
    }
    
    
    /**
     * 获取openid
     * @param pno_code
     * @param request
     * @param response
     * @return
     */
    private String getOpenidByCode(String pno_code,HttpServletRequest request, HttpServletResponse response){
    	String code = request.getParameter("code");
    	if(Methods.isNull(code))
    		return null;
    	System.out.println("code:"+code);
    	
    	//System.out.println("code new :"+code.sub+new Date().getTime());
    	
		OauthAPI oauthAPI = new OauthAPI(WxUtils.getApiConfig(pno_code));
		OauthGetTokenResponse res = oauthAPI.getToken(code);
		if (Methods.isNull(res.getOpenid())) {
			return null;
		}
		
		//
		String openid = res.getOpenid();
		String token = CommFun.nvl(res.getAccessToken());
		wechatService.saveOauthUser(pno_code, code, openid, token);
		
		
		Cookie cookies[] = request.getCookies();           
		Cookie c = null;
		boolean isExists=false;
		if(cookies!=null)
		for (int i = 0; i < cookies.length; i++) {
			c = cookies[i];
			if (c.getName().equals("openid"+pno_code)) {
				c.setValue(openid);
				c.setMaxAge(60 * 60 * 24 * 365);
    			c.setDomain("*");
    			c.setPath("/");
    			c.setSecure(false);
				response.addCookie(c);
				isExists=true;
			}
		}
		if(!isExists){
			c = new Cookie("openid"+pno_code,openid);
			c.setMaxAge(60 * 60 * 24 * 365);
			c.setDomain("*");
			c.setPath("/");
			c.setSecure(false);
			response.addCookie(c);
		}
		return openid;
    }
    
	protected String getOpenidByCookie(String pno_code,HttpServletRequest request) {
		Cookie cookies[] = request.getCookies();
		Cookie c = null;
		if (cookies != null)
			for (int i = 0; i < cookies.length; i++) {
				c = cookies[i];
				if (c.getName().equals("openid"+pno_code)) {
					return c.getValue();
				}
			}
		return null;
	}
}