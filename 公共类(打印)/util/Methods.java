package com.cakes.frameworks.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;


public class Methods {
	
	public final static String[] weekStrArr={"周一","周二","周三","周四","周五","周六","周日"};
	
	
	public static Date toWxDate(Long time){
		if(time==null)
			return null;
		long t=time*1000L;
		return new Date(t);
	}
	
	public static String getDayLabelOnWeek(int week){
		return weekStrArr[week-1];
	}
	
	public static int getDayOnWeek(Date d){
		Calendar ca=Calendar.getInstance();
		ca.setTime(d);
		int week=ca.get(Calendar.DAY_OF_WEEK);
		return week==1?7:week-1;
	}
	
	public static String join(List<String> strList,String ge){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<strList.size();i++){
			if(i!=0)
				sb.append(ge);
			sb.append("'");
			sb.append(strList.get(i));
			sb.append("'");
		}
		return sb.toString();
	}
	
	public static String urlEncode(String url) {
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	public static String urlDecode(String url) {
		try {
			return URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		while(true){
//			System.out.println(createUniSeq());
//		}
		String s=URLEncoder.encode("http://www.drugqq.cn/zhangsheng/m/cust/wxTest.do","utf-8");
		System.out.println(s);
		
		String input = "是否+-*/";
		System.out.println(Methods.getQuanjiaoStr(input));
	}
	
	public static String getQuanjiaoStr(String str){
		if(str==null)
			return null;
		char[] arr={'＋','－','×','％'};
		char[] strArr=str.toCharArray();
		for(char c:strArr){
			for(char tmp:arr){
				if(c==tmp)
					return String.valueOf(c);
			}
		}
		return null;
	}
	
	public static String arrToStr(String[] arr){
		StringBuffer sb=new StringBuffer();
		for(String s:arr){
			sb.append(s).append(",");
		}
		return sb.toString();
	}
	
	public static String createUniSeq(){
		Random r=new Random();
		return String.valueOf(System.currentTimeMillis()*100000+r.nextInt(100000));
	}
	
	public static String createUniCode(String prefix){
		Random r=new Random();
		return prefix+sdf.format(new Date())+r.nextInt(100000);
	}
	
	public static String parseUni(String str){
		if(Methods.isNotNull(str)){
			int n=str.length();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<n;i++){
				int c=str.codePointAt(i);
				if (c<0x0000||c>0xffff) {
				    continue;
				}
				sb.append((char)c);
			}
			return sb.toString();
		}
		return "";
	}

	public static String formatSqlCol(String val) {
		if (val == null)
			return "";
		return val.replaceAll("'", "''");
	}

	public static Date dayAdd(Date date1, int day) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date1);
		long t = ca.getTimeInMillis() + day * 1000 * 3600 * 24;
		ca.setTimeInMillis(t);
		return ca.getTime();
	}

	/**
	 * 时间差天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date1);
		long t1 = ca.getTimeInMillis();
		ca.setTime(date2);
		long t2 = ca.getTimeInMillis();
		long re = (t2 - t1) / (1000 * 3600 * 24);
		return (int) re;
	}

	public static int[] lastMonth(int year, int month) {
		int[] re = new int[2];
		if (month == 1) {
			re[0] = year - 1;
			re[1] = 12;
		} else {
			re[0] = year;
			re[1] = month - 1;
		}
		return re;
	}

	public static boolean isNull(Object obj) {
		if (obj == null)
			return true;
		return "".equals(obj.toString());
	}

	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	public static String nvl(Object obj) {
		if (obj == null)
			return "";
		return obj.toString();
	}

	public static String getArticleStatus(String pass_is) {
		if ("1".equals(pass_is)) {
			pass_is = "<font color='darkblue'>草稿</font>";
		} else if ("2".equals(pass_is)) {
			pass_is = "<font color='red'>待审核</font>";
		} else if ("3".equals(pass_is)) {
			pass_is = "审核通过";
		} else if ("4".equals(pass_is)) {
			pass_is = "<font color='sienna'>审核不通过</font>";
		} else {
			pass_is = "";
		}
		return pass_is;
	}

	public static String nvl(Object obj, String val) {
		if (obj == null || "".equals(obj))
			return val;
		return obj.toString();
	}

	public static boolean isNumeric(String str) {
		if (isNull(str))
			return false;
		return str.matches("^\\d*([.]\\d{0,20})?$");
	}

	public static boolean isInteger(String str) {
		if (isNull(str))
			return false;
		return str.matches("\\d*");
	}

	public static String toUtf8String(String s) {
		try {
			s = new String(s.getBytes("gb2312"), "ISO8859-1");
			return s;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String formatObject(Object obj,String format){
		if(isNull(format)){
			return nvl(obj);
		}
		if(obj==null){
			return "";
		}
//		System.out.println("obj:"+obj);
		DecimalFormat df = new DecimalFormat(format);
		if(obj instanceof BigDecimal){
			return df.format(((BigDecimal)obj).doubleValue());
		}
		if(obj instanceof Long){
			return df.format((Long)obj);
		}
		if(obj instanceof Double){
			return df.format((Double)obj);
		}
		String str=nvl(obj);
		if(isNumeric(str)){
			return df.format(obj);
		}
		return str;
	}
	
	public static String formatDecimal(Object obj, String formatType) {
		if (obj == null)
			return "0";
		DecimalFormat df2 = new DecimalFormat(formatType); // "###0.00"
		return df2.format(((BigDecimal) obj).doubleValue());
	}
	
	public static String formatDecimal(double obj) {
		String formatType = "###0.00";
		DecimalFormat df2 = new DecimalFormat(formatType);
		return df2.format(obj);
	}

	public static String formatDecimal(Object obj) {
		if (obj == null)
			return "0";
		String formatType = "###0.00";
		return formatDecimal(obj, formatType);
	}

	// public static String formatDate(Date date){
	// java.text.SimpleDateFormat sdf = new
	// java.text.SimpleDateFormat("yy-MM-dd");
	// return sdf.format(date);
	// }
	//	
	// public static String formatDate2(Date date){
	// if(date==null) return null;
	// java.text.SimpleDateFormat sdf = new
	// java.text.SimpleDateFormat("yyyy-MM-dd");
	// return sdf.format(date);
	// }

	public static String formatTime(Date date) {
		if (date == null)
			return null;
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public final static String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	public final static String yyyyMMdd = "yyyy-MM-dd";
	public final static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyMMddHHmmsss");

	public static String formatDate(Date date, String format) {
		if (date == null)
			return null;
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static Date parseDate(String str, String format) {
		if (isNull(str))
			return null;
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDate_yyyy_MM_dd(String str) {
		if (isNull(str))
			return null;
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static double toDouble(String str){
		if(str==null)
			return 0;
		if(isNumeric(str))
			return new Double(str);
		return 0;
	}

	public static Integer toInteger(Object obj) {
		if (obj == null || "".equals(obj))
			return null;
		return Integer.parseInt(obj.toString());
	}

	public static Long toLong(Object str) {
		if (str == null || "".equals(str))
			return null;
		return Long.parseLong(str.toString());
	}

	public static String textareaToHtml(String textarea) {
		if (textarea == null)
			return "";
		return textarea.replaceAll("\r\n", "<BR>");
	}

	public static String htmlToTextarea(String html) {
		if (html == null)
			return "";
		return html.replaceAll("<BR>", "\r\n");
	}
	
	
	/*
	 * 对数据进行大小排序、大-->小 mapKey 按照哪个字段排序 keyType排序字段的类型 sortType :ASC还是desc
	 */
	public static List<Map<String, Object>> sortList(
			List<Map<String, Object>> list, final String mapKey, final String keyType,
			final String sortType) {
//		System.out.println("对数据进行排序");
		if (list != null && list.size() > 0) {
			String key = mapKey;
			Comparator<Map<String, Object>> mapComprator = new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> o1,
						Map<String, Object> o2) {
					// do compare.
					int yesVal = 1;
					int noVal = -1;
					if ("desc".equals(StringUtils.lowerCase(sortType))) {
						yesVal = 1;
						noVal = -1;
					} else {
						yesVal = -1;
						noVal = 1;
					}
					int retVal = noVal;
					String o1_key = String.valueOf(o1.get(mapKey));
					String o2_key = String.valueOf(o2.get(mapKey));
					if ("int".equals(keyType)) {
						o1_key = nvl(o1_key, "0");
						o2_key = nvl(o2_key, "0");
						if (Integer.valueOf(o1_key) < Integer.valueOf(o2_key)) {
							retVal = yesVal;
						}
					} else if ("String".equals(keyType)) {
						o1_key = nvl(o1_key, "");
						o2_key = nvl(o2_key, "");
						if (o1_key.compareTo(o2_key) < 0){
							retVal = yesVal;	    		
				    	}
					} else if ("float".equals(keyType)) {
						o1_key = nvl(o1_key, "0");
						o2_key = nvl(o2_key, "0");
						if (Float.valueOf(o1_key) < Float.valueOf(o2_key)) {
							retVal = yesVal;
						}
					} else if ("long".equals(keyType)) {
						o1_key = nvl(o1_key, "0");
						o2_key = nvl(o2_key, "0");
						if (Long.valueOf(o1_key) < Long.valueOf(o2_key)) {
							retVal = yesVal;
						}
					}
					return retVal;
				}
			};
			Collections.sort(list, mapComprator);
		} else {
			new Exception("排序没有取到数据");
		}
		return list;
	}
	
	public static String concatList(List<String> list,String str){
		StringBuffer sb=new StringBuffer();
		for(String s:list){
			sb.append(s).append(str);
		}
		String re=sb.toString();
		if(re.endsWith(str)){
			return re.substring(0,re.length()-str.length());
		}
		return re;
	}

}
