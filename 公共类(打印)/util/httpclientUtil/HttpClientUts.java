package com.cakes.frameworks.util.httpclientUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cakes.frameworks.util.AjaxMethods;
import com.cakes.frameworks.util.CommFun;
import com.cakes.frameworks.util.DateUtil;
import com.cakes.frameworks.util.GlbCons;
import com.cakes.frameworks.util.JsonStringUtils;
import com.cakes.frameworks.util.WsUtils;


public class HttpClientUts {
	
	

	public static String post2Server( Map<String, String> pp, String posturl ){
	    	System.out.println("===post2Server=="+DateUtil.DateToString(new Date(),"yyyy-MM-dd"));
	    //	if ( true) return;
	    	String retJson = "";
	    	HttpRequest request=new  HttpRequest(HttpResultType.BYTES);	    	
	    	try {
				request.setUrl(posturl);
				request.setParameters(HttpRequest.generatNameValuePair(pp));
				request.setCharset("UTF-8");
			
				HttpResponse response = HttpClientUtil.getInstance().execute(request, "", "");
//				response.setInput_charset("UTF-8");
				retJson = response.getStringResult();
				
				System.out.println("--------------- post url:"+posturl);								
				System.out.println("--------------- input :"+JsonStringUtils.objectToJsonString(pp));				
//				System.out.println("--------------- ret json is :"+retJson);
				printret(retJson);
				

			} catch (Exception e) { 
				e.printStackTrace();
				retJson = e.toString();
			} 
	    	
	    	return retJson;
	}
	

	public static String postH5Server( Map<String, String> pp, String posturl ){
    	System.out.println("===post2Server=="+DateUtil.DateToString(new Date(),"yyyy-MM-dd"));
    	System.out.println("--------------- post url:"+posturl);	
    	String custId=AjaxMethods.getCustUserID();
    	if(CommFun.isNotEmpty(custId)){
    		pp.put(GlbCons.CUSTCODE, custId);
    	}
    //	if ( true) return;
    	String retJson = "";
    	HttpRequest request=new  HttpRequest(HttpResultType.BYTES);
    	try {
    		
    		String json_param = JsonStringUtils.objectToJsonString(pp);
    		
    		pp.put("operAtt", "operApplication");
    		pp.put("param", json_param);
    		
			request.setUrl(posturl);
			request.setParameters(HttpRequest.generatNameValuePair(pp));
			request.setCharset("UTF-8");
		
			HttpResponse response = HttpClientUtil.getInstance().execute(request, "", "");
//			response.setInput_charset("UTF-8");
			if(CommFun.isNotEmpty(response.getStringResult())){
				retJson = response.getStringResult();
			}else{
				retJson="";
			}
			
								
			System.out.println("--------------- input :"+JsonStringUtils.objectToJsonString(pp));
			printret(retJson);
			

		} catch (Exception e) { 
			throw new RuntimeException("-----------------报错了");
		} 
    	
    	return retJson;
}
	
	private static void printret(String retJson) {
		if ( CommFun.isNotEmpty(retJson)) {
			
				System.out.println("--------------- ret json is :"+retJson);
			
		} else {
			System.out.println("--------------- ret json is : null");
		}
	}
	
	
	public static String postH5Server(HttpServletRequest servletrequest ,String posturl ){
		Map<String, String> pp = WsUtils.getReqMapAndChk(servletrequest, true);
//		String user_id=AjaxMethods.getAppUserID();
//		pp.put("user_id", user_id);
		
    	System.out.println("===post2Server=="+DateUtil.DateToString(new Date(),"yyyy-MM-dd"));
    //	if ( true) return;
    	String retJson = "";
    	HttpRequest request=new  HttpRequest(HttpResultType.BYTES);	    	
    	try {
    		pp.put("appkey", "h5appkey");
			request.setUrl(posturl);
			request.setParameters(HttpRequest.generatNameValuePair(pp));
			request.setCharset("UTF-8");
		
			HttpResponse response = HttpClientUtil.getInstance().execute(request, "", "");
//			response.setInput_charset("UTF-8");
			retJson = response.getStringResult();
			
			System.out.println("--------------- post url:"+posturl);								
			System.out.println("--------------- input :"+JsonStringUtils.objectToJsonString(pp));				
			printret(retJson);

		} catch (Exception e) { 
			e.printStackTrace();
			retJson = e.toString();
		} 
    	
    	return retJson;
}
	
	public static void main(String[] s){
		
		
		
	}
}
