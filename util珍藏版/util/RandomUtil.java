package com.cakes.frameworks.util;

import java.util.List;
import java.util.Random;

public class RandomUtil {
	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static final String allChar_lower = "0123456789abcdefghijklmnopqrstuvwxyz";
    
    public static final String letterUpperChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letterLowerChar = "abcdefghijklmnopqrstuvwxyz";
    
    public static final String letterChar = letterLowerChar + letterUpperChar;
    public static final String numberChar = "0123456789";
    //除去I和O的小写字母
    public static final String lowerChar = "abcdefghjklmnpqrstuvwxyz";
    
    public static  Random random = new Random();   
  
    
    public static int getRangeNum(int min, int max) {
       
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String genString(int length) {
        StringBuffer sb = new StringBuffer();
       
        int randow_idx = 0;
        int last_type = 1; // 1数字   2字母
        for (int i = 0; i < length; i++) {
        	randow_idx = random.nextInt(allChar_lower.length());
        	if ( randow_idx > 10 && last_type == 2 ) {//取到字母
        		randow_idx = random.nextInt(10);//取数字
        		last_type = 1;
        	} else if ( randow_idx <= 10 && last_type == 1 ) {//取到数字
        		randow_idx = getRangeNum(11,26);//取字母
        		last_type = 2;
        	}
        	
        	if ( randow_idx > 10 ) last_type = 2;
        	else last_type = 1;
            sb.append( allChar_lower.charAt(randow_idx) );
        }
        return sb.toString();
    }
    
    
    /**
     * 返回一个定长的随机字符串(只包数字)
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String genNum(int length) {
        StringBuffer sb = new StringBuffer();
        
        
        for (int i = 0; i < length; i++) {
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return sb.toString();
    }
    
    
    /**
     * 返回一个时间毫秒数+定长的随机字符串(返回+随机数字)
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String genTimeNum(int length) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return sb.toString();
    }


    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String genMixString(int length) {
        StringBuffer sb = new StringBuffer();
        
        for (int i = 0; i < length; i++) {
            sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String genLowerString(int length) {
        return genMixString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String genUpperString(int length) {
        return genMixString(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     * 
     * @param length
     *            字符串长度
     * @return 纯0字符串
     */
    public static String genZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     * 
     * @param num
     *            数字
     * @param fixdlenth
     *            字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(long num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(genZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
                    + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     * 
     * @param num
     *            数字
     * @param fixdlenth
     *            字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(int num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(genZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
                    + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }
    
    
  //重排序    打乱
    public static void changePosition(String[] arr) {    
    	int size = arr.length;
        for(int index=size-1; index>=0; index--) {    
            //从0到index处之间随机取一个值，跟index处的元素交换    
            exchange(arr, random.nextInt(index+1), index);    
        }    
        
    }    
        
    //交换位置    
    private static void exchange(String[] arr, int p1, int p2) {    
    	String temp = arr[p1];   
    
    	arr[p1] = arr[p2];    
    	arr[p2] = temp;  //更好位置  
    }   
    
    
    
    public static void main(String[] args) {
       for ( int i = 0; i < 100 ; i ++ ) {
    	   String s = RandomUtil.genString(6);
    			   System.out.println(s);;
       }
       
    }
        
 
}
