package com.cakes.frameworks.util;

import java.text.MessageFormat;


public class SqlUtils {
	public static String getPageSql(String sql,String orderby,int pageSize,int pageNo) {
		if(orderby==null) orderby="";
//		if(DBContextHolder.DB_TYPE_MYSQL.equals(DBContextHolder.getDbType())){
			sql+=" "+orderby;
			String rtSql="{0} limit {1},{2}";
//			System.out.println((pageNo - 1) * pageSize);
//			System.out.println(pageSize);
			sql=MessageFormat.format(rtSql, sql, String.valueOf(((pageNo - 1) * pageSize)), String.valueOf(pageSize));
//			sql += " limit " + ((pageNo - 1) * pageSize) + "," + pageSize;
//		}else if(DBContextHolder.DB_TYPE_ORACLE.equals(DBContextHolder.getDbType())){
//			sql+=" "+orderby;
//			String rtSql = "SELECT * FROM ( SELECT A.*, rownum r FROM ( {0} ) A  ) B WHERE r > {1} AND r <={2} ";
//			sql = MessageFormat.format(rtSql, new Object[] {sql,  String.valueOf((pageNo - 1) * pageSize) ,  String.valueOf(pageNo * pageSize)});
//		}else if(DBContextHolder.DB_TYPE_MSSQLSERVER.equals(DBContextHolder.getDbType())){
////			String rtSql = "SELECT * FROM ( SELECT A.*, rownum r FROM ( {0} ) A  ) B WHERE r > {1} AND r <={2} ";
////			sql.toLowerCase().lastIndexOf("order");
//			String rtSql="select * from (select row_number() over("+orderby+")  as rownum,* from ({0}) as a) as t where rownum>={1}"+
//				" and rownum<{2}";
//			sql = MessageFormat.format(rtSql, new Object[] {sql,  ((pageNo - 1) * pageSize) ,  (pageNo * pageSize)});
//		}else{
//			throw new CustException("无法找到正确的数据源类型！");
//		}
//		System.out.println(sql);
		return sql;
	}

}
