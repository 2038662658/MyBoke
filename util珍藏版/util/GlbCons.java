package com.cakes.frameworks.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public class GlbCons {
	/* 是否 */
	public static final String YES = "Y";//YES  
	public static final String NO = "N";//NO
	
	public static final String CHKOK = "chkok";//
	public static final String CHKFLG_KEY = "chkflg";//
	
	public static final String WS_TYPE = "ws_type";//
	public static final String WS_DATA = "ws_data";//
	
	
	
	public static final String USER_TYPE_CUST = "1";//1  
	public static final String USER_TYPE_COACH = "2";//2
	
	public static final String PRE_CHK_TRACE = "TRACE";
	public static final String PRE_COMM_FILE = "FILE";
	
	public static final String DATE_FMT_YYYYMMDD= "yyyy-MM-dd HH:mm:ss";	
	public static final String DATE_FMT_YYYY_MM_DD= "yyyy-MM-dd";
	
	
	public static final String DB_ROWNUM_EQ_1 = " LIMIT 1";
	public static final String DB_SYS_DATE_FINAL = "date_format(now(),'%Y-%m-%d %T:%f')";
	
	public static final String SYS_EXCEPTION_ERR_MSG = "系统错误,请联系管理员!";
	
	//接口状态 提供数据的来源 1缓存，2数据库，3外部接口
		public static final String ws_type_cache="1"; 
		public static final String ws_type_db="2"; 
		public static final String ws_type_ws="3"; 
		
		//接口中分页的默认分页
		public static final String pagesize = "10";
		
		//项目中默认状态 0是， 1否
		public static final String STATUS_OK="0";
		public static final String STATUS_NO="1";  
		public static final String STATUS_QT="2";  
		
		
		
		//返回值错误码
		public static final String CODE_PARAM_ERR="101"; 
		public static final String CODE_PARAM_ERR_MSG="参数错误"; 
		public static final String CODE_BT_ERR="102";
		public static final String CODE_OK="200"; 
		public static final String CODE_OK_MSG="ok"; 
		public static final String CODE_USER_ERR="301"; 
		public static final String CODE_REQ_ERR="500"; 
		public static final String CODE_REQ_ERR_MSG="系统错误"; //非法请求
		
		public static final String CODE_REQ_FORBIT="501"; //非法请求
		public static final String CODE_REQ_FORBIT_MSG="禁止访问"; //非法请求
		
		//返回值KEY
		public static final String RESULT="data"; 
		public static final String PAGE_RESULT="page_info"; 
		public static final String RESULTCODE="status"; 
		public static final String REASON="msg";
		public static final String NEWTOKEN="new_token"; 
		
		
	
		public static final String APP_KEY  ="APP_KEY";// 

		
		//session存放的某些key
		public static final String user  ="user";//内网登录用户
		public static final String CUST  ="cust";//外网登录用户
		public static final String CUSTCODE  ="cust_code";//外网登录用户ID
		public static final String PC_LOGIN_IP  ="PC_LOGIN_IP";//登录用户ip地址
		public static final String CUR_CHECK_MENU  ="CUR_CHECK_MENU";  //管理人员操作的当前菜单
		
		
		//时间格式
		public static final String WS_MON_FMT = "yyyy-MM"; //精确到月
		public static final String WS_DAY_FMT = "yyyy-MM-dd"; //精确到日
		public static final String WS_DAY_HMS_FMT = "yyyy-MM-dd HH:mm:ss";//时分秒
		public static final String WS_DAY_HM_FMT = "yyyy-MM-dd HH:mm";//时分 
		
		
		//图片路径  跟目录DATA   
		public static final String RES_PATH_AVATAR  ="data/avatar";//用户添加头像时候路径 
		public static final String RES_PATH_MANUAL  ="data/manual";//手册路径
		public static final String RES_PATH_VEDIO  ="data/video";//视频路径 
		public static final String RES_PATH_PIC  ="data/pic";//图片保存路径
		public static final String RES_PATH_GOODSPIC  ="data/goods";//商品图片路径
		public static final String RES_PATH_APK  ="data/apk";//apk 路径
		public static final String MAIL_PATH  ="data/mail/send";//邮件生成Excel路径
		
		//图片类的具体子路径  
		public static final String PIC_PATH_ORIG  ="orig";//原始目录	
		public static final String PIC_PATH_THUMB  ="thumb";//缩略图目录 
		public static final String PIC_PATH_MID  ="mid";//中图
		//视频默认图片
		public static final String PIC_PATH_VIDEO  ="data/pic/default.png";
		//背景图片
		public static final String PIC_PATH_BG  ="data/pic/bg.png";
		
		public static final int PIC_THUMB_IMG_HEIGHT1  =195;
		public static final int PIC_THUMB_IMG_WIDTH1  =110;
		
		//缩略图尺寸
		
		public static final int PIC_THUMB_IMG_WIDTH  = 210;//缩略图 w
		public static final int PIC_THUMB_IMG_HEIGHT  = 210;//缩略图h
		public static final int PIC_MID_IMG_WIDTH  = 350;//中图w
		public static final int PIC_MID_IMG_HEIGHT  = -1;//中图w   -1表示自适应
		
		
		//系统默认开始路径  spring初始化
		public static  String SYSTEMPATH="";
		public static  String UPLOAD_PATH=""; //附件保存路径，默认tomcat，随系统启动加载，也可能会改在tomcat外面
		
		//系统默认开始路径  读取数据库setting配置表
		public static  String WEBPATH="";//内网网
		public static  String STATIC_RESPATH="";//静态资源路径  CSS、图片、JS/等
		public static  String WEBPATH_OUT="";//内网
		public static  String SELF_WEBPATH = "";
		
		public static  String PORTAL_OUT = "";
		
		
		
		//缓存名称 settingParam系统参数缓存  outdate数据缓存
		public static String CACHE_PARAM = "cbb_infosettings"; //setting表  dict表使用  
		public static String CACHE_APP_BUSI = "cbb_infoappbusi"; 	//APP业务数据缓存
		
		//订单编号生成规则
		public static final String ORDER_BUILD_RULE = "OBR";
		
		//课程类型   1-计划类课程  2-训练营课程
		public static final String COURSE_TYPE_PLAN = "plan"; 
		public static final String COURSE_TYPE_TRAIN = "train"; 
		//商品类型    coach-教练  plan-课程计划   train-训练营
		public static final String ITEM_BELONG_PLAN = "plan"; 
		public static final String ITEM_BELONG_TRAIN = "train";
		public static final String ITEM_BELONG_COACH = "coach";
		
		
		// 'send-已发送  wait-待发送  fail-发送失败',
		public static final String SEND_MSG_STATUS_SEND = "send"; 
		public static final String SEND_MSG_STATUS_WAIT = "wait";
		public static final String SEND_MSG_STATUS_FAIL = "fail";
		
		// 消息表归属类型
		public static final String MSG_TYPE_ord_ok_msg2coach = "ord_ok_msg2coach"; //订单成功 -通知教练
		public static final String MSG_TYPE_ord_outtime_msg2user = "ord_outtime_msg2user"; //订单超时提醒

		// getPnoCodeByUtype
		public static  String PNO_TYPE_USER = "1001";//用户端微信公号类型码
		public static  String PNO_TYPE_COACH = "1002";//用户端微信公号类型码
		
		
		public static final String REMARK_WX_MSG = "如需帮助，客服18618415398随时听候您的吩咐";
		
		//cbb 基础库  数据库实例名
		public static final String DB_NAME_CBBBASE = "cbbbase";
		
		
		public static final String RESULTSTATE="state";
		public static final String RESULTINFO="info";
		
		public static final String RESULTSTATE_OK="success";
		public static final String RESULTINFO_OK="OK";
		public static final String RESULTSTATE_ERR="error";
		public static final String RESULTINFO_ERR="系统错误";
		
		
		
		
		//剑锋 2017年6月1日 13:44:37
		public static String WEBURL = "http://m.panhuikeji.com/xunlongsys1/";
		public static String WEBURL_LOCAL = "http://127.0.0.1:8080/xunlongsys1/";//http://127.0.0.1:8080/xunlongsys1
		
		public static String MD5_STR = "caishui20176291429";
		
		public static String CACHE_API_URL = "";
}
