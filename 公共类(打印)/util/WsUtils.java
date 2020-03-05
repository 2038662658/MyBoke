package com.cakes.frameworks.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.owftc.rpt.action.utils.wx.MD5Util;
import com.cakes.frameworks.util.CommFun;
//import com.owftc.rpt.util.DateUtil;
//import com.owftc.rpt.util.MD5;
import com.cakes.frameworks.util.GlbCons;
//import com.owftc.rpt.util.cons.WsUtsCons;

public class WsUtils {

	/**
	 * 获取seqId
	 * 
	 * @return  "yyyyMMddHHmmssSSS"
	 */
	public static String getHtch_SEQID(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}
	
	
	/**
	 * JSON获取 键值,先判断是否有该值,如果没有,则返回参数定义的ifnull
	 */
	
	public static String getJsonVal(JSONObject jsonObj, String key, String ifnull) {
		String val = "";
		
		if ( jsonObjIsEmpty(jsonObj) || !jsonObj.containsKey(key)  )  return ifnull;
		try {
			val = jsonObj.getString(key);
			val = CommFun.nvl(val, ifnull);
		} catch (Exception e) {
			val = ifnull;
		}
		
		return val;
	}
	
	public static String getJsonVal(JSONObject jsonObj, String key, String ifnull, String ifNotKeyVal) {
		String val = "";
		if ( jsonObjIsEmpty(jsonObj) || !jsonObj.containsKey(key)  )  return ifNotKeyVal;	
		try {
			val = jsonObj.getString(key);
			val = CommFun.nvl(val, ifnull);
		} catch (Exception e) {
			val = ifNotKeyVal;
		}
		
		return val;
	}
	
	 /**
	   * 只解析单层非数组json对象
	   * @param jsonStr
	   * @return
	   */
	  public static Map<String, String> parseJSON2Map(String jsonStr){
	        Map<String, String> map = new HashMap<String, String>();
	        //最外层解析
	      try {
//		        JSONObject json = new JSONObject(jsonStr);
//		        Iterator it = json.keys(); 
		        JSONObject json = JSONObject.parseObject(jsonStr);
 
//		        Set<String> it = json.keySet();// json.keys(); 
		        Iterator it = json.keySet().iterator();
	          while (it.hasNext()) {  
	              String key = (String) it.next();  
	              String value = json.getString(key);  
	              map.put(key,value);
	          }   
	      } catch (Exception e) {
			e.printStackTrace();
		  }

	       
	        return map;
	    }
	
	
	/**
	 * 暖车时间、急加速时间弧度值
	 * ifMinVal 如果低于某值,用什么代替
	 */
	public static String getSrcTimeVal(String val, int minVal) {
		String srcVal = "0";
		try {
			if ( CommFun.isEmpty(val)) return "0";
			
			if ( val.indexOf(".") > -1 ) val = val.substring(0,val.indexOf("."));
			if ( CommFun.isNum(val) ) {
				int intVal = Integer.parseInt(val);
				if ( intVal == 0 ) return "0";
				
				int retVal =  intVal*15+30 ;//公式
				if ( retVal >= 360 ) {
					retVal = 310; //不能超过360
				}
				if ( retVal < minVal ) {//如果小于最小阈值
					return minVal+"";
				}
				srcVal = retVal + "";
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		
		return srcVal;
	}
	
	/**
	 *  油耗弧度值
	 * ifMinVal 如果低于某值,用什么代替
	 * baseVal  分母值
	 */
	public static String getSrcFuelVal(String baseVal, String val, int minVal, int baseSrc) {
		String srcVal = "0";
		try {
			if ( CommFun.isEmpty(val) ||  CommFun.isEmpty(baseVal) ) return "0";
		    
			if ( CommFun.isNum(val) ) {
				float floatVal = Float.parseFloat(val);
				float baseValFloat = 0;
				if ( CommFun.isNum(baseVal)) {
					baseValFloat = Float.parseFloat(baseVal);
				}
				//分母为空,怎返回0
				if ( baseValFloat == 0  || floatVal == 0) {
					return "0";
				}
				
				float retVal =  ( (floatVal) / baseValFloat )   * baseSrc  + minVal-10 ;//公式
				if ( retVal >= 360 ) {
					retVal = 310; //不能超过360
				}
				if ( retVal < minVal ) {//如果小于最小阈值
					return minVal+"";
				}
				srcVal = retVal + "";
				
				if ( srcVal.indexOf(".") > -1 ) srcVal = srcVal.substring(0,srcVal.indexOf("."));
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		
		return srcVal;
	}
	
	
	/**
	 * JSON获取 键值,先判断是否有该值,如果没有,则返回""
	 */
	public static String getJsonVal(JSONObject jsonObj, String key) {
		String val = "";
		if ( jsonObjIsEmpty(jsonObj) || !jsonObj.containsKey(key)  )  return "";
		try {
			val = jsonObj.getString(key);
		} catch (Exception e) {
			val = "";
		}
		return val;
	}
	/**
	 * JSON获取 键值,先判断是否有该值,如果没有,则返回""
	 */
	public static JSONObject getJsonObj(JSONObject jsonObj, String key) {
		JSONObject jo = new JSONObject();
		
		if ( jsonObjIsEmpty(jsonObj) || !jsonObj.containsKey(key)  )  return jo;
		try {
			jo = jsonObj.getJSONObject(key);
		} catch (Exception e) {
			e.printStackTrace();
			jo = new JSONObject();
		}
		return jo;
	}
	/**
	 * JSON获取 键值,先判断是否有该值,如果没有,则返回""
	 */
	public static JSONArray getJSONArray(JSONObject jsonObj, String key) {
		JSONArray jsonArray = null;
		if ( jsonObjIsEmpty(jsonObj) || !jsonObj.containsKey(key)  )  return null;
		try {
			if ( jsonObj.containsKey(key)) {
				jsonArray = jsonObj.getJSONArray(key);
			}
			
		} catch (Exception e) {
			jsonArray = null;
		}
		return jsonArray;
	}
	
	public static boolean jsonArrayIsEmpty(JSONArray jsonObj) {
		if (jsonObj == null || jsonObj.size() == 0)
			return true;	
		return false;
	}
	
	public static boolean jsonArrayIsNotEmpty(JSONArray jsonObj) {
		
		return !jsonArrayIsEmpty(jsonObj);
	}
	
	public static boolean jsonObjIsEmpty(JSONObject jsonObj) {
		if (jsonObj == null || jsonObj.size() == 0)
			return true;	
		return false;
	}
	
	public static boolean jsonObjIsNotEmpty(JSONObject jsonObj) {
		return !jsonObjIsEmpty(jsonObj);
	}
	
	
	
	/**
	 * 统一的错误返回
	 */
	public static Map<String, Object> getRetMap(String resultCode, String reason) {
		Map<String, Object> errRetMap = new HashMap<String, Object>();		
		errRetMap.put(GlbCons.RESULTCODE, resultCode);
		errRetMap.put(GlbCons.REASON, reason);	 
		return errRetMap;
	}
	
	/**
	 * 获取10位时间戳
	 * 
	 * @return  "yyyyMMddHHmmssSSS"
	 */
	public static int getTimeInt(){
		long times = System.currentTimeMillis()/1000;
		return Integer.parseInt(times+"");
	}
	
	/**
	 * 统一的错误返回
	 */
	public static Map<String, Object> getRetMap(String resultCode, String reason,Map<String,Object> log_map) {
		Map<String, Object> errRetMap = new HashMap<String, Object>();		
		errRetMap.put(GlbCons.RESULTCODE, resultCode);
		errRetMap.put(GlbCons.REASON, reason);	
		if(log_map!=null){
			log_map.put("ws_status", GlbCons.STATUS_NO);
			log_map.put("ws_bak", reason);
		}
		return errRetMap;
	}
	
	/**
	 * 统一的异常LOG错误返回
	 */
	public static void setRetExceptionLogMap(Map<String,Object> retMap, Map<String,Object> log_map, Exception e) { 
		log_map.put("ws_status", GlbCons.STATUS_NO);
		log_map.put("ws_bak", "异常:"+  CommFun.getStringByFixLen(e.getMessage(), 200));	 
		//retMap = new HashMap<String,Object>();
		retMap.put(GlbCons.RESULTCODE, GlbCons.CODE_REQ_ERR);
		retMap.put(GlbCons.REASON,  GlbCons.CODE_REQ_ERR_MSG);//CODE_REQ_ERR_MSG		
		retMap.put(GlbCons.RESULT,  "");//CODE_REQ_ERR_MSG		
	}
	
	/**
	 * 统一的异常LOG错误返回
	 */
	public static void setRetForbitLogMap(Map<String,Object> retMap, Map<String,Object> log_map, String e) { 
		log_map.put("ws_status", GlbCons.STATUS_NO);
		log_map.put("ws_bak", "Forbit:"+  e);	 
		//retMap = new HashMap<String,Object>();
		retMap.put(GlbCons.RESULTCODE, GlbCons.CODE_REQ_FORBIT);
		retMap.put(GlbCons.REASON,  GlbCons.CODE_REQ_FORBIT_MSG);//CODE_REQ_FORBIT_MSG
		retMap.put(GlbCons.RESULT,  "");//CODE_REQ_ERR_MSG		
	}
	
	/**
	 * 统一的LOG 初始状态
	 */
	public static void setRetDefaultOkLogMap(Map<String,Object> log_map, String ws_name, String ws_type) { 
		log_map.put("ws_name", ws_name); 		
		log_map.put("ws_status", GlbCons.STATUS_OK);
		log_map.put("ws_type", ws_type); 
	}
	/**
	 * 统一的错误LOG错误返回
	 */
	public static void setRetErrLogMap(Map<String,Object> log_map, String errMsg ) { 
		log_map.put("ws_status", GlbCons.STATUS_NO);
		log_map.put("ws_bak", "错误:"+  errMsg); 
	}
	 
	
	/**
	 * 
	 */
	public static void setH5CallHead(Map<String,Object> paramMap ) { 
		paramMap.put("udid", CommFun.nvl(paramMap.get("udid"), "0"));
		paramMap.put("appkey", CommFun.nvl(paramMap.get("appkey"), "h5_appkey"));
	}
	
	/**
	 * 获取课程详情页url 
	 * @param paramMap
	 */
	public static String getCourseDtlUrl(Map<String,Object> paramMap, Map<String,Object> infoMap) {
		String dtl_url = "";
		String course_type = CommFun.nvl(infoMap.get("course_type"));
		String course_id = CommFun.nvl(infoMap.get("course_id"));
//		if ( GlbCons.COURSE_TYPE_PLAN.equals(course_type)) { //计划
//			dtl_url = GlbCons.WEBPATH +  "/m/course/detail.do?course_id="+course_id;
//		} else if ( GlbCons.COURSE_TYPE_TRAIN.equals(course_type)) { //计划
//			dtl_url = GlbCons.WEBPATH +  "/m/train/detail.do?course_id="+course_id;
//		}
		
		
		return dtl_url;
		
	}
	
	
	/**
	 * 获取头像  不区分 大中小图
	 */
	public static String getAvatar(String rootUrl, String abth_path) {
		String fullpath = rootUrl +"/" +GlbCons.RES_PATH_AVATAR+ "/"+ abth_path;
		return fullpath;
	}
	
	
	/**
	 * 获取商品图片路径   不区分
	 * @param picsize  orig  mid  thumb   大中小图
	 * @param abth_path  相对路径
	 * @return
	 */
	public static String getGoodsPicUrl( String abth_path, String picsize) {
		if ( abth_path.startsWith("http:"))  return abth_path;
		String fullpath = GlbCons.WEBPATH_OUT +"/" +GlbCons.RES_PATH_GOODSPIC+ "/"+"/" +picsize+ "/"+ abth_path;
		return fullpath;
	}
	
	/**
	 * 获取商品图片路径   不区分
	 * @param picsize  orig  mid  thumb   大中小图
	 * @param abth_path  相对路径
	 * @return
	 */
	public static String getDtlPicUrl( String abth_path, String picsize) {
		String fullpath = GlbCons.WEBPATH_OUT + abth_path;
		return fullpath;
	}
	
	
	
	
	/**
	 * 获取隐私权限 接机服务 条款 等
	 * @param abth_path  根路径  http://192.168.1.101:8080/infserver/+请求路径
	 * @param abth_path  相对路径 
	 */
	public static String getPageUrl(String rootUrl, String abth_path) {
		String fullpath = rootUrl + "/"+ abth_path;
		return fullpath;
	}
	
	/**
	 * 获取apk路径
	 * @param abth_path  根路径  http://192.168.1.101:8080/infserver/
	 * @param abth_path  相对路径
	 * @return
	 */
	public static String getApkUrl(String rootUrl, String abth_path) {
		String fullpath = rootUrl + "/"+ GlbCons.RES_PATH_APK + "/"+ abth_path;
		return fullpath;
	}
	
	/**
	 * 获取使用手册     
	 * @param abth_path  根路径  http://192.168.1.101:8080/infserver/data 
	 * @return
	 */
	public static String getManualUrl(String rootUrl, String abth_path) {
		String fullpath = rootUrl +"/" +GlbCons.RES_PATH_MANUAL+ "/"+ abth_path;
		return fullpath;
	}
	/**
	 * 获取视频资料
	 */
	public static String getVedioUrl(String rootUrl, String abth_path) {
//		String fullpath = rootUrl + "/"+ GlbCons.RES_PATH_VEDIO +  "/"+ abth_path;
		// modify bu xuhm  2014-111-06和小梅确认,视频存 orig
		String fullpath = rootUrl + "/"+ GlbCons.RES_PATH_VEDIO +  "/orig/"+ abth_path;
		return fullpath;
	}
	
	/**
	 * 如果不是以webroot开头的，则加上系统的webrooturl
	 * @param webRootUrl
	 * @param url
	 * @return
	 */
	public static String getWebUrl( String url) {
		if ( CommFun.isEmpty(url)) return "";
		if ( url.startsWith("http:") || url.startsWith("https:"))  return url;
		String fullpath = ""; 
		if ( url.startsWith("/")) {
			fullpath = GlbCons.WEBPATH_OUT +  url;
		} else {
			fullpath = GlbCons.WEBPATH_OUT + "/"+ url;
		}
		 
		return fullpath;
	}
	
	/**
	 * 如果不是以webroot开头的，则加上系统的PORTAL_OUT
	 * @param webRootUrl
	 * @param url
	 * @return
	 */
	public static String getPortalUrl( String url) {
		if ( CommFun.isEmpty(url)) return "";
		if ( url.startsWith("http:"))  return url;
		String fullpath = "";
		if ( url.startsWith("/")) {
			fullpath = GlbCons.PORTAL_OUT +  url;
		} else {
			fullpath = GlbCons.PORTAL_OUT + "/"+ url;
		}
		
		
		return fullpath;
	}

	/**
	 * 获取不同尺寸图片路径  包含新闻活动 以及相册
	 * @param abth_path  根路径  http://192.168.1.101:8080/infserver/data
	 * @param pic_type   orig mid  thump    pic_type如果为空，则直接拼
	 * @param abth_path  相对路径
	 * @return
	 */
	public static String getPicUrl(String rootUrl, String pic_type, String abth_path) {
		if ( CommFun.isEmpty(abth_path)) return "";
		if ( abth_path.startsWith("http:"))  return abth_path;
		String fullpath = "";
		if ( CommFun.isNotEmpty(pic_type)) {
			fullpath = rootUrl + "/"+ GlbCons.RES_PATH_PIC +  "/"+ pic_type +  "/"+ abth_path;
		} else {
			fullpath = rootUrl + "/"+ abth_path;
		}
		
		return fullpath;
	}
	/**
	 * 
	 * @param rootUrl
	 * @param pic_type
	 * @param abth_path
	 * @return
	 */
	public static String getVideoUrl(String rootUrl, String pic_type, String abth_path) {
		if ( CommFun.isEmpty(abth_path)) return "";
		String fullpath = rootUrl + "/"+ GlbCons.RES_PATH_VEDIO +  "/"+ pic_type +  "/"+ abth_path;
		return fullpath;
	}
	
	
	public static List<Map<String, Object>> getRetMapDataList(Map<String,Object>  retMap) {
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		
		if ( CommFun.isNotEmpty(retMap)) {
			list = (List<Map<String, Object>>) retMap.get(GlbCons.RESULT);
		}
		return list;
	}
	
	public static Map<String, Object> getRetMapDataInfo(Map<String,Object>  retMap) {
		Map<String, Object>  infoMap = new HashMap<String, Object>();
		
		if ( CommFun.isNotEmpty(retMap)) {
			infoMap = (Map<String, Object>) retMap.get(GlbCons.RESULT);
		}
		return infoMap;
	}
	

	/**
	 * 获取驾驶时长，根据时间秒
	 * 
	 * @return  "00:00:00"
	 */
	
	public static String getDrvTimeCost4sec(String str_second, int type){  
		int second = 0;
		try {
			second = Integer.parseInt(str_second);
						
		} catch (Exception e) {
			String ret = CommFun.nvl(str_second, "0")+"s";	
			return ret;
		}
        int h = 0;  
        int d = 0;  
        int s = 0;  
        int temp = second%3600;  
             if(second>3600){  
               h= second/3600;  
                    if(temp!=0){  
               if(temp>60){  
               d = temp/60;  
            if(temp%60!=0){  
               s = temp%60;  
            }  
            }else{  
               s = temp;  
            }  
           }  
          }else{  
              d = second/60;  
           if(second%60!=0){  
              s = second%60;  
           }  
          }  
        
         if ( type == 1) {
        	 return h+"时"+d+"分"+s+"秒";  
         } else {
        	 return h+":"+d+":"+s;  
         } 
    }  
	
	/** 增长量或比率，(判断空，或NULL的情况)
	   flag=1: 增长率， flag=2: 比率  按分统计  3:折扣率  4: arpu	 2数相除  
	   maxVal 最大值限制，为-1 不限制。  
	  */

	  public static String formatPercent(String s1, String s2, int flag, Double maxVal) { //s1除数，  s2被除
	    String percent = "";
	    DecimalFormat df = new DecimalFormat("0.00");  
	   
	    if ( !CommFun.isNum(s1) || !CommFun.isNum(s2)) {
			return "";
		}
	    double f1 = Double.parseDouble(s1);
	    double f2 = Double.parseDouble(s2);
	    if ( f1 == 0 || f2 == 0 )
	      return percent;

	    //s1,s2不=null, 不为空，
//	    if (flag==0) /** @todo  */
	    switch (flag) {
	      case 1: //增长率   s1-s2/s2
	        percent = df.format( (f1 - f2) / f2 * 100) + "%";
	        break;
	      case 2: //比率 99.1%  s1/s2 %   百公里油耗
	        percent = df.format(f1 / f2 * 100) +"";
	        break;
	      case 3: //折扣率 s1-s2/s1
	        percent = df.format( (f1 - f2) / f1 * 100) + "%";
	        break;
	      case 4: //s1/s2 arpu
	        percent = df.format(f1 / f2);
	        break;
	    }
	    
	    if ( maxVal > -1 && Double.parseDouble(percent) >= maxVal ) {
	    	percent = maxVal + "";
	    }
	    return percent;
	}

	  
	  	/**
	  	 * 从request对象获取参数map对象 , 并验证是否合法
	  	 * @param request
	  	 * @param chk   是否验证  验证不过,返回空   验证的未处理
	  	 * @return
	  	 */
		@SuppressWarnings("unchecked")
		public static Map getReqMapAndChk(HttpServletRequest request, boolean chk) {

			// 参数Map
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e) { 
			}
			Map<String, String[]> properties = request.getParameterMap();

			// 返回值Map

			Map returnMap = new HashMap();

			Iterator entries = properties.entrySet().iterator();

			Map.Entry entry;

			String name = "";

			String value = "";

			while (entries.hasNext()) {

				entry = (Map.Entry) entries.next();

				name = (String) entry.getKey();

				Object valueObj = entry.getValue();
				value = "";

				if (null == valueObj) {

					value = "";

				} else if (valueObj instanceof String[]) {

					String[] values = (String[]) valueObj; 
					value = StringUtils.join(values, ',');

				} else {
					value = valueObj.toString();
				}
				returnMap.put(name, value);

			} 
			returnMap.put("request007", CommFun.nvl(request.getRequestURI()));
			//如果客户端传了src_ip，需要替换  remoteaddr007   2014-12-12 xuhm h5的服务器直接调用的server，所以需要增加该处理
			String src_ip = CommFun.nvl(returnMap.get("src_ip"));
			if ( CommFun.isNotEmpty(src_ip)) {
				returnMap.put("remoteaddr007",src_ip);
			} else {
//				returnMap.put("remoteaddr007",CommMethod.nvl(com.common.tools.utils.StringUtils.getIpAddr(request)));
			}
			
			if ( chk || true) { //检查
				String appkey = CommFun.nvl(returnMap.get("appkey"));
				String token=CommFun.nvl(returnMap.get("token"));
				String cust_code=CommFun.nvl(returnMap.get("cust_code"));
//				if ( CommFun.isEmpty(appkey) ) {
//					return null;
//				}
				if("3in4android8zjbh@2".equals(appkey)){
					if(token.length()==64 && token.equals(WsUtsCons.getCUSTKEYMAP().get(cust_code))){
						String now_time=DateUtil.dateTimeToString(new Date());
						String new_token=MD5Util.encrypt(cust_code+now_time, cust_code+"jnbh!68");
//						String new_token=MD5.md5JM(cust_code+now_time);
						WsUtsCons.getCUSTKEYMAP().put(cust_code, new_token);
						returnMap.put(GlbCons.NEWTOKEN, new_token);
					}else{
						return null;
					}
				}
			}else{
				returnMap.put("chkflg", "noneedchk");
			}
		
			if ( CommFun.isEmpty(returnMap) )  {//保证不为空
				returnMap = new HashMap();
				returnMap.put("chkflg", "chkok");
			}
			return returnMap;
	}
		
		
	
	
		
	public static void main(String[] s){
	
		WsUtils ws = new WsUtils();
//		System.out.println(ws.getDrvTimeCost4sec("2093", 2));
		String retval = ws.getSrcTimeVal("1", 20);
//		String retval = ws.getSrcFuelVal("10.21", "0.83", 30, 270);
		System.out.println("ret:"+retval);
	}
}
