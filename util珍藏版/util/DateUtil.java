package com.cakes.frameworks.util;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
  
/**
 * 时间工具类
 * 
 * @ClassName: DateUtil
 * @author wgw
 * @date Nov 26, 2012 4:04:25 PM
 * @version V1.0
 * 
 */
public class DateUtil { 
	public static Date addDate(Date date,int renewalsdata){ 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, renewalsdata);
		return calendar.getTime(); 
	}
	public static Date str2Date (String datestr, String datefmt) {		
		try {
			if ( CommFun.isEmpty(datefmt)) datefmt = "yyyy-MM-dd HH:mm:ss" ;
			SimpleDateFormat df = new SimpleDateFormat(datefmt);
			return df.parse(datestr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 *  时间转换成字符串
	 *  date 转换的时间，datefmt 转换成的格式 默认yyyy-MM-dd HH:mm:ss
	 */
	public static String DateToString(Date date,String datefmt) { 
		if(null==date)return "";
		if ( CommFun.isEmpty(datefmt)) datefmt = "yyyy-MM-dd HH:mm:ss" ;
		SimpleDateFormat f = new SimpleDateFormat(datefmt);
		return  f.format(date); 
	}
	
	
	/**
	 *  返回当前时间
	 *  datefmt 返回时间的格式  默认yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateTimeToString(String datefmt) {
		Calendar c = Calendar.getInstance();
		if ( CommFun.isEmpty(datefmt)) datefmt = "yyyy-MM-dd HH:mm:ss" ;
		SimpleDateFormat f = new SimpleDateFormat(datefmt);
		return  f.format(c.getTime()); 
	}
	
	/**
	 * 比较两个日期的大小 如果date1>date2,返回大于 0 的值 如果date1<date2,返回小于 0 的值
	 * 如果date1=date2,返回等于 0 的值	 *  
	 */
	public static int compareDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return -1;
		}
		//时间抽象类Calendar
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.compareTo(cal2);
	}
	/**
	 * 获得与某日期相隔几天的日期
	 * 
	 * @param date 指定的日期
	 * @param dayNumber 相隔的天数
	 * @return Date 日期
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static Date addDay(Date date, int dayNumber) {
		if (date == null || dayNumber == 0) {
			return date;
		} else {
			Calendar vcal = Calendar.getInstance();
			vcal.setTime(date);
			vcal.add(5, dayNumber);
			return vcal.getTime();
		}
	}

	/**
	 * 获得与某日期相隔几天的日期（返回string）
	 * 
	 * @param dateString 指定的日期
	 * @param format 返回日期的格式
	 * @param dayNumber 相隔的天数
	 * @return String 日期
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String addDayToString(String dateString, String format, int dayNumber) {
		Date vdate = stringToDate(dateString, format);
		vdate = addDay(vdate, dayNumber);
		String vdates = dateToString(vdate, format);
		return vdates;
	}

	/**
	 * 获得与某日期相隔几月的日期
	 * 
	 * @param date 指定的日期
	 * @param monthNumber 相隔的月数
	 * @return Date 返回的日期
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static Date addMonth(Date date, int monthNumber) {
		if (date == null || monthNumber == 0) {
			return date;
		} else {
			Calendar vcal = Calendar.getInstance();
			vcal.setTime(date);
			vcal.add(2, monthNumber);
			return vcal.getTime();
		}
	}

	/**
	 * 获得与某日期相隔几月的日期（返回string）
	 * 
	 * @param date 指定的日期
	 * @param format 时间格式
	 * @param monthNumber 相隔的月数
	 * @return Date 返回的日期
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String addMonthToString(String dateString, String format, int monthNumber) {
		Date vdate = stringToDate(dateString, format);
		vdate = addMonth(vdate, monthNumber);
		String vdates = dateToString(vdate, format);
		return vdates;
	}

	/**
	 * 转换日期为字符串，格式"yyyyMMdd"
	 * 
	 * @param date 时间
	 * @return String 转换后时间
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyyMMdd");
	}

	/**
	 * 转换日期为字符串，格式"yyyyMMddHHmmss"
	 * 
	 * @param date 时间
	 * @return String 转换后时间
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String dateTimeToString(Date date) {
		return dateToString(date, "yyyyMMddHHmmss");
	}

	/**
	 * 转换时间为指定格式
	 * 
	 * @param date 指定的时间
	 * @param format 指定的格式(yyyy/yy MM/mm dd hh24/hh/HH mi/mm ss SSS 年月日)
	 * @return String 返回的时间
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	@SuppressWarnings("unchecked")
	public static String dateToString(Date date, String format) {
		if (date == null) {
			return null;
		}
		// 传入参数中的[时间格式]为空
		if (format == null || format.equalsIgnoreCase("")) {
			return null;
		}
		Hashtable h = new Hashtable();
		String javaFormat = new String();
		if (format.indexOf("yyyy") != -1) {
			h.put(new Integer(format.indexOf("yyyy")), "yyyy");
		} else if (format.indexOf("yy") != -1) {
			h.put(new Integer(format.indexOf("yy")), "yy");
		}
		if (format.indexOf("MM") != -1) {
			h.put(new Integer(format.indexOf("MM")), "MM");
		} else if (format.indexOf("mm") != -1) {
			h.put(new Integer(format.indexOf("mm")), "MM");
		}
		if (format.indexOf("dd") != -1) {
			h.put(new Integer(format.indexOf("dd")), "dd");
		}
		if (format.indexOf("hh24") != -1) {
			h.put(new Integer(format.indexOf("hh24")), "HH");
		} else if (format.indexOf("hh") != -1) {
			h.put(new Integer(format.indexOf("hh")), "HH");
		} else if (format.indexOf("HH") != -1) {
			h.put(new Integer(format.indexOf("HH")), "HH");
		}
		if (format.indexOf("mi") != -1) {
			h.put(new Integer(format.indexOf("mi")), "mm");
		} else if (format.indexOf("mm") != -1 && h.containsValue("HH")) {
			h.put(new Integer(format.lastIndexOf("mm")), "mm");
		}
		if (format.indexOf("ss") != -1) {
			h.put(new Integer(format.indexOf("ss")), "ss");
		}
		if (format.indexOf("SSS") != -1) {
			h.put(new Integer(format.indexOf("SSS")), "SSS");
		}

		int intStart = 0;
		for (intStart = 0; format.indexOf("-", intStart) != -1; intStart++) {
			intStart = format.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}

		for (intStart = 0; format.indexOf(".", intStart) != -1; intStart++) {
			intStart = format.indexOf(".", intStart);
			h.put(new Integer(intStart), ".");
		}

		for (intStart = 0; format.indexOf("/", intStart) != -1; intStart++) {
			intStart = format.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}

		for (intStart = 0; format.indexOf(" ", intStart) != -1; intStart++) {
			intStart = format.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}

		for (intStart = 0; format.indexOf(":", intStart) != -1; intStart++) {
			intStart = format.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}

		if (format.indexOf("年") != -1) {
			h.put(new Integer(format.indexOf("年")), "年");
		}
		if (format.indexOf("月") != -1) {
			h.put(new Integer(format.indexOf("月")), "月");
		}
		if (format.indexOf("日") != -1) {
			h.put(new Integer(format.indexOf("日")), "日");
		}
		if (format.indexOf("时") != -1) {
			h.put(new Integer(format.indexOf("时")), "时");
		}
		if (format.indexOf("分") != -1) {
			h.put(new Integer(format.indexOf("分")), "分");
		}
		if (format.indexOf("秒") != -1) {
			h.put(new Integer(format.indexOf("秒")), "秒");
		}
		int i = 0;
		while (h.size() != 0) {
			Enumeration e = h.keys();
			int n = 0;
			do {
				if (!e.hasMoreElements()) {
					break;
				}
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n) {
					n = i;
				}
			} while (true);
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = (new StringBuilder()).append(temp).append(javaFormat).toString();
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat, new DateFormatSymbols());
		return df.format(date);
	}

	/**
	 * 计算某日期之前几个月的日期
	 * 
	 * @param dateString 指定的日期yyyyMM
	 * @param delMonth 相隔月数
	 * @return String 返回的日期
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String descreaseYearMonth(String dateString, int delMonth) {
		if (dateString == null) {
			return null;
		}
		// [时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!
		if (dateString.length() != 6) {
			return null;
		}
		int year = (new Integer(dateString.substring(0, 4))).intValue();
		int month = (new Integer(dateString.substring(4, 6))).intValue();
		if (delMonth < 0) {
			return increaseYearMonth(dateString, -1 * delMonth);
		}
		month -= delMonth;
		if (month >= 10) {
			return (new StringBuilder()).append(dateString.substring(0, 4)).append((new Integer(month)).toString())
					.toString();
		}
		if (month > 0 && month < 10) {
			return (new StringBuilder()).append(dateString.substring(0, 4)).append("0").append(
					(new Integer(month)).toString()).toString();
		}
		int yearDec = (-1 * month) / 12 + 1;
		int month2 = 12 - (-1 * month) % 12;
		if (month2 >= 10) {
			return (new StringBuilder()).append((new Integer(year - yearDec)).toString()).append(
					(new Integer(month2)).toString()).toString();
		} else {
			return (new StringBuilder()).append((new Integer(year - yearDec)).toString()).append("0").append(
					(new Integer(month2)).toString()).toString();
		}
	}

	/**
	 * 获取中文日期
	 * 
	 * @param date 时间
	 * @return String 中文日期
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getChineseDate(Date date) {
		if (date == null) {
			return null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", new DateFormatSymbols());
			String dtrDate = df.format(date);
			return (new StringBuilder()).append(dtrDate.substring(0, 4)).append("年").append(
					Integer.parseInt(dtrDate.substring(4, 6))).append("月").append(
					Integer.parseInt(dtrDate.substring(6, 8))).append("日").toString();
		}
	}

	/**
	 * 获取中文年月
	 * 
	 * @param
	 * @param
	 * @return String
	 * @throws
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getChineseYearAndMonth(String dateString) {
		if (dateString == null) {
			return null;
		}
		// [时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!
		if (dateString.length() != 6) {
			return null;
		}
		String year = dateString.substring(0, 4);
		String month = dateString.substring(4, 6);
		return (new StringBuilder()).append(year).append("年").append(month).append("月").toString();
	}

	/**
	 * 获取当前时间
	 * 
	 * @return Date
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	/**
	 * 获取当前时间  timeStamp
	 * 
	 * @return Date
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static Timestamp getCurrentTimeStamp() {
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		return timeStamp;
	}

	/**
	 * 获取当前时间（yyyyMMddHHmiss格式）
	 * 
	 * @return String
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getCurrentDateToString() {
		return getCurrentDateToString("yyyyMMddHHmiss");
	}
	
	/**
	 * 获取当前时间（yyyy-MM-dd格式）
	 * 周超
	 * 2018-12-20下午6:38:57
	 */
	public static String getCurrentDateToString2() {
		return getCurrentDateToString("yyyy-MM-dd");
	}
	
	/**
	 * 获取当前时间（yyyyMMddHHmiss格式）  写入数据库的 datetime
	 * 
	 * @return String
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getCurrentDate4Db() {
		return getCurrentDateToString("yyyyMMddHHmiss");
	}

	/**
	 * 获取当前时间（自定义格式）
	 * 
	 * @param strFormat 时间格式（yyyyMMddHHmiss等格式）
	 * @return String
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getCurrentDateToString(String strFormat) {
		return dateToString(getCurrentDate(), strFormat);
	}

	/**
	 * 获取当前年份
	 * 
	 * @return int
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(1);
	}

	/**
	 * 获取当前年月（返回string）
	 * 
	 * @return String
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getCurrentYearMonthToString() {
		Calendar cal = Calendar.getInstance();
		String currentYear = (new Integer(cal.get(1))).toString();
		String currentMonth = null;
		if (cal.get(2) < 9) {
			currentMonth = (new StringBuilder()).append("0").append((new Integer(cal.get(2) + 1)).toString())
					.toString();
		} else {
			currentMonth = (new Integer(cal.get(2) + 1)).toString();
		}
		return (new StringBuilder()).append(currentYear).append(currentMonth).toString();
	}
	/**
	 * 获取上一个年月
	 * @return
	 */
	public static String getLastYearMonthToString() {
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        //sdf.format(cal.getTime());
        return sdf.format(cal.getTime());
	}
	
	
	

	/**
	 * 转换某时间为某种格式
	 * 
	 * @param date 指定的时间
	 * @param format 指定的格式(yyyy/yy MM/mm dd hh24/hh/HH mi/mm ss SSS 年月日)
	 * @return String 返回的时间
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	@SuppressWarnings("unchecked")
	public static String getDate(Date date, String format) {
		if (date == null) {
			return null;
		}
		// 传入参数中的[时间格式]为空
		if (format == null || format.equalsIgnoreCase("")) {
			return null;
		}
		Hashtable h = new Hashtable();
		String javaFormat = new String();
		if (format.indexOf("yyyy") != -1) {
			h.put(new Integer(format.indexOf("yyyy")), "yyyy");
		} else if (format.indexOf("yy") != -1) {
			h.put(new Integer(format.indexOf("yy")), "yy");
		}
		if (format.indexOf("MM") != -1) {
			h.put(new Integer(format.indexOf("MM")), "MM");
		} else if (format.indexOf("mm") != -1) {
			h.put(new Integer(format.indexOf("mm")), "MM");
		}
		if (format.indexOf("dd") != -1) {
			h.put(new Integer(format.indexOf("dd")), "dd");
		}
		if (format.indexOf("hh24") != -1) {
			h.put(new Integer(format.indexOf("hh24")), "HH");
		} else if (format.indexOf("hh") != -1) {
			h.put(new Integer(format.indexOf("hh")), "HH");
		} else if (format.indexOf("HH") != -1) {
			h.put(new Integer(format.indexOf("HH")), "HH");
		}
		if (format.indexOf("mi") != -1) {
			h.put(new Integer(format.indexOf("mi")), "mm");
		} else if (format.indexOf("mm") != -1) {
			h.put(new Integer(format.indexOf("mm")), "MM");
		}
		if (format.indexOf("ss") != -1) {
			h.put(new Integer(format.indexOf("ss")), "ss");
		}
		if (format.indexOf("SSS") != -1) {
			h.put(new Integer(format.indexOf("SSS")), "SSS");
		}

		int intStart = 0;
		for (intStart = 0; format.indexOf("-", intStart) != -1; intStart++) {
			intStart = format.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}

		for (intStart = 0; format.indexOf(".", intStart) != -1; intStart++) {
			intStart = format.indexOf(".", intStart);
			h.put(new Integer(intStart), ".");
		}

		for (intStart = 0; format.indexOf("/", intStart) != -1; intStart++) {
			intStart = format.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}

		for (intStart = 0; format.indexOf(" ", intStart) != -1; intStart++) {
			intStart = format.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}

		for (intStart = 0; format.indexOf(":", intStart) != -1; intStart++) {
			intStart = format.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}

		if (format.indexOf("年") != -1) {
			h.put(new Integer(format.indexOf("年")), "年");
		}
		if (format.indexOf("月") != -1) {
			h.put(new Integer(format.indexOf("月")), "月");
		}
		if (format.indexOf("日") != -1) {
			h.put(new Integer(format.indexOf("日")), "日");
		}
		if (format.indexOf("时") != -1) {
			h.put(new Integer(format.indexOf("时")), "时");
		}
		if (format.indexOf("分") != -1) {
			h.put(new Integer(format.indexOf("分")), "分");
		}
		if (format.indexOf("秒") != -1) {
			h.put(new Integer(format.indexOf("秒")), "秒");
		}
		int i = 0;
		while (h.size() != 0) {
			Enumeration e = h.keys();
			int n = 0;
			do {
				if (!e.hasMoreElements()) {
					break;
				}
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n) {
					n = i;
				}
			} while (true);
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = (new StringBuilder()).append(temp).append(javaFormat).toString();
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat, new DateFormatSymbols());
		return df.format(date);
	}

	/**
	 * 获取两个时间之间的天数
	 * 
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return long 天数
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static long getDayDifferenceBetweenTwoDate(Date beginDate, Date endDate) {
		// 传入参数[开始时间]为空
		if (beginDate == null) {
			return 0;
		}
		// 传入参数[结束时间]为空
		if (endDate == null) {
			return 0;
		}
		long ld1 = beginDate.getTime();
		long ld2 = endDate.getTime();
		long days = (ld2 - ld1) / 86400000L;
		return days;
	}

	  
	/**
	 * 获取下月第一天
	 * 
	 * @return String
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getFirstDayOfNextMonth() {
		String strToday = getCurrentDateToString();
		return (new StringBuilder()).append(increaseYearMonth(strToday.substring(0, 6), 1)).append("01").toString();
	}

	/**
	 * 获取某月最后一天
	 * 
	 * @param dateString 指定月份yyyyMM
	 * @return String 几号
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getLastDayOfMonth(String dateString) {
		if (dateString == null) {
			return null;
		}
		// [时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!
		if (dateString.length() != 6) {
			return null;
		}
		int vnf = Integer.valueOf(dateString.substring(0, 4));
		int vyf = Integer.valueOf(dateString.substring(4, 6));
		if (vyf == 2) {
			if (vnf % 4 == 0 && vnf % 100 != 0 || vnf % 400 == 0) {
				return "29";
			} else {
				return "28";
			}
		}
		switch (vyf) {
		case 1: // '\001'
		case 3: // '\003'
		case 5: // '\005'
		case 7: // '\007'
		case 8: // '\b'
		case 10: // '\n'
		case 12: // '\f'
			return "31";

		case 4: // '\004'
		case 6: // '\006'
		case 9: // '\t'
		case 11: // '\013'
			return "30";

		case 2: // '\002'
		default:
			return null;
		}
	}

	/**
	 * 获取某月第一天
	 * 
	 * @param pdate 指定日期
	 * @return int
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static int getMonthFirstDay(Date pdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pdate);
		return calendar.getActualMinimum(5);
	}

	/**
	 * 获取某月最后一天
	 * 
	 * @param pdate 指定日期
	 * @return int
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static int getMonthLastDay(Date pdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pdate);
		return calendar.getActualMaximum(5);
	}

	/**
	 * 获取两个日期直接的月数
	 * 
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return int 月数
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static int getMonthDifferenceBetweenTwoDate(Date beginDate, Date endDate) {
		// 传入参数[开始时间]为空
		if (beginDate == null) {
			return 0;
		}
		// 传入参数[结束时间]为空
		if (endDate == null) {
			return 0;
		}
		int year1 = Integer.valueOf(dateToString(beginDate, "yyyy"));
		int year2 = Integer.valueOf(dateToString(endDate, "yyyy"));
		int month1 = Integer.valueOf(dateToString(beginDate, "MM"));
		int month2 = Integer.valueOf(dateToString(endDate, "MM"));
		int day1 = Integer.valueOf(dateToString(beginDate, "dd"));
		int day2 = Integer.valueOf(dateToString(endDate, "dd"));
		double months = ((year2 - year1) * 12 + month2) - month1;
		if (day1 != day2 && (day1 != getLastDayOfMonth(beginDate) || day2 != getLastDayOfMonth(endDate))) {
			months += (double) (day2 - day1) / 31D;
		}

		return (int) months;
	}

	/**
	 * 获取两个字符串日期之间的月数
	 * 
	 * @param beginDate 开始时间yyyyMM/yyyyMMdd
	 * @param endDate 结束时间yyyyMM/yyyyMMdd
	 * @return int 月数
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static int getMonthDifferenceBetweenTwoStringDate(String beginDate, String endDate) throws Exception {
		// 起始时间输入格式错误,请输入形如\"yyyyMMdd\"或者\"yyyyMM\"的日期格式!
		if (beginDate == null || beginDate.length() != 6 && beginDate.length() != 8) {
			return 0;
		}
		// 终止时间输入格式错误,请输入形如\"yyyyMMdd\"或者\"yyyyMM\"的日期格式!
		if (endDate == null || endDate.length() != 6 && endDate.length() != 8) {
			return 0;
		}
		return getMonthDifferenceBetweenTwoDate(stringToDate(beginDate), stringToDate(endDate));
	}

	/**
	 * 获取某日期之后几个月的日期
	 * 
	 * @param dateString 指定的日期
	 * @param addMonth 月数
	 * @return String 返回的日期yyyymm
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String increaseYearMonth(String dateString, int addMonth) {
		if (dateString == null) {
			return null;
		}
		// [时间串]输入格式错误,请输入形如\"yyyymm\"的日期格式!
		if (dateString.length() != 6) {
			return null;
		}
		int year = (new Integer(dateString.substring(0, 4))).intValue();
		int month = (new Integer(dateString.substring(4, 6))).intValue();
		if (addMonth < 0) {
			return descreaseYearMonth(dateString, -1 * addMonth);
		}
		month += addMonth;
		if(month != 12){
			year += month / 12;
			month %= 12;
		}
		if (month <= 12 && month >= 10) {
			return (new StringBuilder()).append(year).append((new Integer(month)).toString()).toString();
		} else {
			return (new StringBuilder()).append(year).append("0").append((new Integer(month)).toString()).toString();
		}
	}

	/**
	 * 判断日期格式是否合法
	 * 
	 * @param dateString 指定的时间
	 * @return boolean
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static boolean isDate(String dateString) {
		String s = null;
		if (dateString == null) {
			return false;
		}
		if (dateString.length() != 10 && dateString.length() != 8) {
			return false;
		}
		if (dateString.length() == 10) {
			s = (new StringBuilder()).append(dateString.substring(0, 4)).append(dateString.substring(5, 7)).append(
					dateString.substring(8, 10)).toString();
		} else {
			s = dateString;
		}
		try {
			stringToDate(s, "yyyyMMdd");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 字符串转日期
	 * 
	 * @param dateString String型日期
	 * @return Date 转换后日期
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static Date stringToDate(String dateString) {
		Date vdate = null;
		String vformat = null;
		if (dateString == null) {
			return null;
		}
		// [时间串]输入格式错误,请输入合法的日期格式!
		if (dateString.length() != 4 && dateString.length() != 6 && dateString.length() != 7
				&& dateString.length() != 8 && dateString.length() != 10 && dateString.length() != 14
				&& dateString.length() != 19) {
			return null;
		}
		if (dateString.length() == 4) {
			vformat = "yyyy";
		} else if (dateString.length() == 6) {
			vformat = "yyyyMM";
		} else if (dateString.length() == 7) {
			dateString = (new StringBuilder()).append(dateString.substring(0, 4)).append(dateString.substring(5, 7))
					.toString();
			vformat = "yyyyMM";
		} else if (dateString.length() == 8) {
			vformat = "yyyyMMdd";
		} else if (dateString.length() == 10) {
			dateString = (new StringBuilder()).append(dateString.substring(0, 4)).append(dateString.substring(5, 7))
					.append(dateString.substring(8, 10)).toString();
			vformat = "yyyyMMdd";
		} else if (dateString.length() == 14) {
			vformat = "yyyyMMddHHmmss";
		} else if (dateString.length() == 19) {
			vformat = "yyyy-MM-dd HH:mm:ss";
		}
		vdate = stringToDate(dateString, vformat);
		return vdate;
	}

	/**
	 * 字符串转时间（指定时间格式）
	 * 
	 * @param dateString
	 * @param format 时间格式
	 * @return Date
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	@SuppressWarnings("unchecked")
	public static Date stringToDate(String dateString, String format) {
		if (dateString == null) {
			return null;

		}
		// 传入参数中的[时间串]为空
		if (dateString.equalsIgnoreCase("")) {
			return null;
		}
		// 传入参数中的[时间格式]为空
		if (format == null || format.equalsIgnoreCase("")) {
			return null;
		}
		Hashtable h = new Hashtable();
		String javaFormat = new String();
		if (format.indexOf("yyyy") != -1) {
			h.put(new Integer(format.indexOf("yyyy")), "yyyy");
		} else if (format.indexOf("yy") != -1) {
			h.put(new Integer(format.indexOf("yy")), "yy");
		}
		if (format.indexOf("MM") != -1) {
			h.put(new Integer(format.indexOf("MM")), "MM");
		} else if (format.indexOf("mm") != -1) {
			h.put(new Integer(format.indexOf("mm")), "MM");
		}
		if (format.indexOf("dd") != -1) {
			h.put(new Integer(format.indexOf("dd")), "dd");
		}
		if (format.indexOf("hh24") != -1) {
			h.put(new Integer(format.indexOf("hh24")), "HH");
		} else if (format.indexOf("hh") != -1) {
			h.put(new Integer(format.indexOf("hh")), "HH");
		} else if (format.indexOf("HH") != -1) {
			h.put(new Integer(format.indexOf("HH")), "HH");
		}
		if (format.indexOf("mi") != -1) {
			h.put(new Integer(format.indexOf("mi")), "mm");
		} else if (format.indexOf("mm") != -1 && h.containsValue("HH")) {
			h.put(new Integer(format.lastIndexOf("mm")), "mm");
		}
		if (format.indexOf("ss") != -1) {
			h.put(new Integer(format.indexOf("ss")), "ss");
		}
		if (format.indexOf("SSS") != -1) {
			h.put(new Integer(format.indexOf("SSS")), "SSS");
		}

		int intStart = 0;
		for (intStart = 0; format.indexOf("-", intStart) != -1; intStart++) {
			intStart = format.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}

		for (intStart = 0; format.indexOf(".", intStart) != -1; intStart++) {
			intStart = format.indexOf(".", intStart);
			h.put(new Integer(intStart), ".");
		}

		for (intStart = 0; format.indexOf("/", intStart) != -1; intStart++) {
			intStart = format.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}

		for (intStart = 0; format.indexOf(" ", intStart) != -1; intStart++) {
			intStart = format.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}

		for (intStart = 0; format.indexOf(":", intStart) != -1; intStart++) {
			intStart = format.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}

		if (format.indexOf("年") != -1) {
			h.put(new Integer(format.indexOf("年")), "年");
		}
		if (format.indexOf("月") != -1) {
			h.put(new Integer(format.indexOf("月")), "月");
		}
		if (format.indexOf("日") != -1) {
			h.put(new Integer(format.indexOf("日")), "日");
		}
		if (format.indexOf("时") != -1) {
			h.put(new Integer(format.indexOf("时")), "时");
		}
		if (format.indexOf("分") != -1) {
			h.put(new Integer(format.indexOf("分")), "分");
		}
		if (format.indexOf("秒") != -1) {
			h.put(new Integer(format.indexOf("秒")), "秒");
		}
		int i = 0;
		while (h.size() != 0) {
			Enumeration e = h.keys();
			int n = 0;
			do {
				if (!e.hasMoreElements()) {
					break;
				}
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n) {
					n = i;
				}
			} while (true);
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = (new StringBuilder()).append(temp).append(javaFormat).toString();
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat);
		df.setLenient(false);
		Date myDate = new Date();
		try {
			myDate = df.parse(dateString);
		} catch (ParseException e) {
			// 日期格式转换错误!将dateString转换成时间时出错
			e.printStackTrace();
		}
		return myDate;
	}

	/**
	 * 比较两个日期大小，是否endDate>=beginDate
	 * 
	 * @param beginDate 起始时间
	 * @param endDate 终止时间
	 * @return boolean
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static boolean yearMonthGreatEqual(String beginDate, String endDate) {
		// 起始时间输入格式错误,请输入形如\"yyyyMM\"的日期格式!
		if (beginDate == null || beginDate.length() != 6) {
			return false;
		}
		// 终止时间输入格式错误,请输入形如\"yyyyMM\"的日期格式!
		if (endDate == null || endDate.length() != 6) {
			return false;
		}
		String temp1 = beginDate.substring(0, 4);
		String temp2 = endDate.substring(0, 4);
		String temp3 = beginDate.substring(4, 6);
		String temp4 = endDate.substring(4, 6);
		if (Integer.parseInt(temp1) > Integer.parseInt(temp2)) {
			return true;
		}
		if (Integer.parseInt(temp1) == Integer.parseInt(temp2)) {
			return Integer.parseInt(temp3) >= Integer.parseInt(temp4);
		} else {
			return false;
		}
	}

	/**
	 * 月数转换为N年M个月
	 * 
	 * @param month 月数
	 * @return String N年M个月
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String monthToYearMonth(String month) {
		if (month == null) {
			return null;
		}
		// 传入参数中的[月数]为空
		if (month.equalsIgnoreCase("")) {
			return null;
		}
		String yearMonth = "";
		int smonth = 0;
		int year = 0;
		int rmonth = 0;
		if ("0".equals(month)) {
			return "0月";
		}
		smonth = Integer.parseInt(month);
		year = smonth / 12;
		rmonth = smonth % 12;
		if (year > 0) {
			yearMonth = (new StringBuilder()).append(year).append("年").toString();
		}
		if (rmonth > 0) {
			yearMonth = (new StringBuilder()).append(yearMonth).append(rmonth).append("个月").toString();
		}
		return yearMonth;
	}

	/**
	 * 获取某个月最后一天
	 * 
	 * @param date 指定的日期
	 * @return int 最后一天（N号）
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static int getLastDayOfMonth(Date date) {
		// 传入参数中的[时间]为空
		if (date == null) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(5);
	}

	/**
	 * 根据出生日期和某日期计算年龄
	 * 
	 * @param pcsrq 出生日期
	 * @param pny 指定的日期(date)
	 * @return double 年龄
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static double getAgeByBirthDay(Date pcsrq, Date pny) {
		return getAgeByBirthDay(pcsrq, dateToString(pny, "yyyyMMDD"));
	}

	/**
	 * 根据出生日期和某日期计算年龄
	 * 
	 * @param pcsrq 出生日期
	 * @param pny 指定的日期(String)
	 * @return double 年龄
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static double getAgeByBirthDay(Date pcsrq, String pny) {
		double vnl = 0.0D;
		// 计算年龄时传入的出生日期为空!
		if (pcsrq == null) {
			return 0;
		}
		// 计算年龄时传入的年月为空!
		if (pny == null || "".equals(pny)) {
			return 0;
		}
		int vlen = pny.length();
		if (vlen == 6) {
			vnl = (double) getMonthDifferenceBetweenTwoDate(stringToDate(dateToString(pcsrq, "yyyyMM"), "yyyyMM"),
					stringToDate(pny, "yyyyMM")) / 12D;
		} else if (vlen == 8) {
			vnl = (double) getMonthDifferenceBetweenTwoDate(stringToDate(dateToString(pcsrq, "yyyyMMDD"), "yyyyMMDD"),
					stringToDate(pny, "yyyyMMDD")) / 12D;
		}
		return vnl;
	}

	/**
	 * 获取某个日期为第几周
	 * 
	 * @param date 指定的日期
	 * @return int 周数
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static int getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(7);
	}
	
	
	/**
	 * 获取接下来一周时间
	 * @return
	 */
	public static List<Map<String, Object>> getWeekDays(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i=0;i<7;i++){
			Map<String,Object> remap=new HashMap<String,Object>();
			Date date=new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(cal.DATE,i);//把日期往后增加
			date=cal.getTime();  
			String dateday = sf.format(date);
			String weekday=getChineseWeek(date).substring(2);
			if(i==0){weekday="今";};if(i==1){weekday="明";};
			remap.put("dateday", dateday);
			remap.put("weekday", weekday);
			list.add(remap);
		}
		return list;
	}
	
	/**
	 * 获取某个日期为第几周（中文）
	 * 
	 * @param date 指定的日期
	 * @return String
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getChineseWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int i = cal.get(7);
		if (i == 1) {
			return "星期日";
		}
		if (i == 2) {
			return "星期一";
		}
		if (i == 3) {
			return "星期二";
		}
		if (i == 4) {
			return "星期三";
		}
		if (i == 5) {
			return "星期四";
		}
		if (i == 6) {
			return "星期五";
		}
		if (i == 7) {
			return "星期六";
		} else {
			return "";
		}
	}
	
	//返回周几
	public static String getIntWeek(String date, String fmt) {
		Date d = stringToDate(date, fmt);
		
		return getEnWeek(d);
	}
	
	/**
	 * 获取某个日期为第几周（中文）
	 * 
	 * @param date 指定的日期
	 * @return String
	 * @author wgw
	 * @date Nov 26, 2012
	 */
	public static String getEnWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int i = cal.get(7);
		if (i == 1) {
			return "7";
		}
		if (i == 2) {
			return "1";
		}
		if (i == 3) {
			return "2";
		}
		if (i == 4) {
			return "3";
		}
		if (i == 5) {
			return "4";
		}
		if (i == 6) {
			return "5";
		}
		if (i == 7) {
			return "6";
		} else {
			return "";
		}
	}
	
	
	public static List<String> getTwoDaysList(String date1, String date2, String input_fmt,  String output_fmt, int maxret){ 
    	 List<String>  dayList = new ArrayList<String>();
    	 
    	 SimpleDateFormat input_fmter = new SimpleDateFormat(input_fmt); 
    	 SimpleDateFormat output_fmter = new SimpleDateFormat(output_fmt); 
        if(date1.equals(date2)){ 
//            System.out.println("两个日期相等!");   
            return dayList; 
        } 
         
        String tmp; 
        if(date1.compareTo(date2) > 0){  //确保 date1的日期不晚于date2 
            tmp = date1; 
            date1 = date2;  
            date2 = tmp; 
        } 
         
        tmp = input_fmter.format(str2Date(date1, input_fmt).getTime() + 3600*24*1000); 
         
        int num = 0;  
        while(tmp.compareTo(date2) <0){   
        	maxret--;
        //    System.out.println(tmp);    
          
            num++; 
            tmp = input_fmter.format(str2Date( tmp, input_fmt).getTime() + 3600*24*1000); 
            dayList.add(CommFun.strDate2fmt(tmp, input_fmt, output_fmt));
            if ( maxret == 0 )  break;
            
        } 
         
        if(num == 0) {
        	System.out.println("两个日期相邻!"); 
        }	            
        
        return dayList;
    } 
	
	
	
	/** 
     * 时间戳转换成日期格式字符串 
     * @param seconds 精确到秒的字符串 
     * @param formatStr 
     * @return 
     */  
    public static String timeStamp2Date(String seconds,String format) {  
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
            return "";  
        }  
        if(format == null || format.isEmpty()) {
        	
        	format = "yyyy-MM-dd HH:mm:ss";  
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);  
        if ( seconds.indexOf(".") > -1 || seconds.indexOf(":") > -1 ) {
        	Date d = str2Date(seconds,format );
        	
        	return sdf.format(d);
        }
        
       
        return sdf.format(new Date(Long.valueOf(seconds+"000")));  
    }  
    
    
    /** 
    * 生成随机时间 
    * @param beginDate 
    * @param endDate 
    * @return 
    */ 
    public static Date randomDate(String beginDate,String  endDate ){  

    try {  

	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	    Date start = format.parse(beginDate);//构造开始日期  
	
	    Date end = format.parse(endDate);//构造结束日期  
	
	    //getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。  
	
	    if(start.getTime() >= end.getTime()){  
	
	    return null;  
	
	    }  
	
	    long date = random(start.getTime(),end.getTime());  
	
	    return new Date(date);  
	
	    } catch (Exception e) {  
	
	    e.printStackTrace();  
	
	    }  
	
	    return null;  
	
	    }  
	
	    private static long random(long begin,long end){  
	
	    long rtn = begin + (long)(Math.random() * (end - begin));  
	
	    //如果返回的是开始时间和结束时间，则递归调用本函数查找随机值  
	
	    if(rtn == begin || rtn == end){  
	
	    return random(begin,end);  
	
	    }  
	
	    return rtn;  

    }
	
	public static void main(String[] s){
//		System.out.println(DateUtil.DateToString(new Date(),"yyyyMM"));
		
//		String lastMonLastDay1 = CommFun.lastDayOfMonth("2014-10-12", -1, InterConstants.WS_DAY_FMT, "yyyyMMdd");
		
		String lastMonLastDay1 = CommFun.strDate2fmt("9/10/15", "dd/MM/yy", "yyyy-MM-dd");
		
		lastMonLastDay1 = DateUtil.timeStamp2Date("1448260860", "yyyy-MM-dd HH:mm");
//		
//		System.out.println("lastMonLastDay1:"+lastMonLastDay1);
		
//		String lastMonLastDay1 = WsUtils.formatPercent("11.221", "22", 4);
		//String lastMonLastDay1 = CommFun.isInteger("1") +"";
				
		//System.out.println(Long.parseLong(CommFun.nvl("", "0")) +"lastMonLastDay1:"+lastMonLastDay1);
		System.out.println(lastMonLastDay1);
//		System.out.println(DateUtil.descreaseYearMonth("201503", 3));
		
		Date randomDate=randomDate("2015-12-20 11:11:11","2016-3-28 12:12:12"); 
		
		
		System.out.println(DateUtil.dateToString(randomDate, "yyyy-MM-dd HH:mm:ss"));
		
		
	}
	
}
