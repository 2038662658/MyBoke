package com.cakes.frameworks.util;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;


public class Db4Utils {

	StringBuffer handle = new StringBuffer();
	
	/**
	 * 生成like ?  的对应值
	 * @param values
	 * @return
	 */
	public static String queryStrLike(String values) {
		return "%" + values + "%";
	}
	
	/**
	 * /**
	 * 生成where字句的   = 操作对象集合    批量操作
	 * @param cols  需要操作的字段集合， 可以带表名    "a.fund_name,a.fund_code"
	 * @param rexpstr  去掉表前缀   .要加转义 \\	  "a\\.|p\\."
	 * @param ls  传入如的SQL参数集合    方法会自动追加新参数
	 * @param pageMap  页面参数传入集合

	 * @return
	 */
	public static String getEqulesColsSQL(List<Object> ls, Map pageMap, String cols, String rexpstr) {
		StringBuffer sql = new StringBuffer();
		String[] equlesCols = cols.split(","); //"a.fund_name,a.fund_code".split(",");
		String tmpVal = "";
		for ( String colName : equlesCols ) {//相等的字段集合
			colName = colName.trim();	// key
			tmpVal = CommFun.nvl(pageMap.get( repWhereSql(colName, rexpstr)) );//去掉表前缀   .要加转义 \\		
			if ( CommFun.isNotEmpty(tmpVal) ) {
				sql.append( " and ").append(colName).append("=?" );	 
				ls.add(tmpVal);
			}			
		} 
		
		return sql.toString();
	}
	
	/**
	 * /**
	 * 生成where字句的   like  操作对象集合    批量操作
	 * @param cols  需要操作的字段集合， 可以带表名   "a.fund_name,a.fund_code"
	 * @param rexpstr   去掉表前缀   .要加转义 \\	  "a\\.|p\\."
	 * @param ls  传入如的SQL参数集合，  方法会自动追加新参数
	 * @param pageMap  页面参数传入集合

	 * @return
	 */
	public static String getLikeColsSQL(List<Object> ls, Map pageMap, String cols, String rexpstr) {
		StringBuffer sql = new StringBuffer();
		String[] likeCols = cols.split(",");
		String tmpVal = "";	
		for ( String colName : likeCols ) {// 匹配like的字段集合
			colName = colName.trim();	// key
			tmpVal = CommFun.nvl(pageMap.get(  Db4Utils.repWhereSql(colName, rexpstr))  );//
			if ( CommFun.isNotEmpty(tmpVal) ) {
				sql.append( " and ").append(colName).append(" like ? " ); 
				ls.add(queryStrLike(tmpVal));
			}			
		} 
		
		return sql.toString();
	}

	/**
	 * 将输入的 str字符传转换成  '', '', ''格式
	 */
	public static String fmtStr4DbIn(String strs) {
		String ret = strs.replaceAll(",", "','");
		ret = "'" + ret +"'";
		return ret;
	}



	/**
	 * 获取指定操作拼装SQL
	 * @param ls
	 * @param colname
	 * @param colval
	 * @param op  =  <=  >=   等
	 * @return
	 */
	public static String getOpByColsSQL(List<Object> ls, String colname, String colval, String op) {
		String sql = "";
		if( CommFun.isNotEmpty(colval) ) {
			sql = " and "+colname+" "+op+" ?"; //  op:  =  <=  >= 
			ls.add(colval);
		}
		
		return sql;
	}
	
	
	/**
	 * 获取排序字段
	 * @param cols   a.colname 
	 * @param orderFlag   asc  desc 
	 * @return
	 */
	public static String getOrderByColsSQL( String col, String orderFlag) {
		
		orderFlag = CommFun.nvl(orderFlag, "");
		String sql = " order by "+col+" "+orderFlag+"  ";
		
		
		return sql;
	}
	
	
	
	/**
	 * sql where字句替换
	 * @param colName
	 * @param rexp
	 * @return
	 */
	public static String repWhereSql(String colName, String rexp) {
//		return colName.trim().toLowerCase().replaceAll("a\\.|p\\.", ""));
		String ret =  colName.trim().toLowerCase().replaceAll(rexp, "");
		return ret;
	}
	
	
	// 直接获取插入语句 
	public static String getMap2SqlByTabName(Map map, String tableName) {
		// 这里需要判断，判断本次拿到的值与数据库里的值进行判断 主键如果不存在 执行insert 存在 执行update
		/*
		 * if(map!=null) { Set key = map.keySet(); //获得key的集合 Iterator it =
		 * key.iterator(); while(it.hasNext()){ String colname =
		 * it.next().toString(); //获得key值 CommFun.nvl(map.get(colname));
		 * //获得vaule值
		 * System.out.println("+++++++"+map.get("tree_id"));//获得传过来的数据的tree_id
		 * 
		 * } } //获取数据库里tree_id的值 List list = new ArrayList(); String selectSQL =
		 * "select tree_id from " + tableName ; String
		 * rs=sqlCtrl.execute(selectSQL);
		 * 
		 * return null;
		 */

		StringBuffer getString = new StringBuffer(" insert into " + tableName);// 返回的sql
		StringBuffer col = new StringBuffer("(");// 拼接列名
		StringBuffer val = new StringBuffer("values(");// 拼接value值
		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			while (it.hasNext()) {
				String colname = it.next().toString(); // 获得key值
				col.append(colname + ",");
				String colvalue = CommFun.nvl(map.get(colname));
				if (StringUtils.isNumeric(colvalue) == false
						&& colvalue.indexOf(".NEXTVAL") < 0
						&& !"SYSDATE".equals(colvalue)) {
					colvalue = "'" + colvalue + "'";// 不是数字，需要加单引号
				}
				if (StringUtils.isEmpty(colvalue))
					val.append("null,");
				else
					val.append(CommFun.nvl(map.get(colname)) + ","); // 获得vaule值
			}
			String colsp = col.substring(0, col.lastIndexOf(","));// 去掉最后逗号
			String valsp = val.substring(0, val.lastIndexOf(","));// 去掉最后逗号
			getString.append(colsp + ") ");
			getString.append(valsp + ")");
		}
		// System.out.println("打印sql语句："+getString.toString());
		return getString.toString();
	}

	// 获取update更新语句
	public static String getUpdateSqlByMap(Map map, String tableName,
			String whereSQL) {
		StringBuffer getString = new StringBuffer(" update " + tableName
				+ " set ");// 返回的sql
		StringBuffer setSQL = new StringBuffer("");// 拼接列名

		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			while (it.hasNext()) {

				String colname = it.next().toString(); // 获得key值
				String colvalue = CommFun.nvl(map.get(colname));
				if (StringUtils.isNumeric(colvalue) == false
						&& colvalue.indexOf(".NEXTVAL") < 0
						&& !"SYSDATE".equals(colvalue) 
						&& !"now()".equals(colvalue)) {
					colvalue = "'" + colvalue + "'";// 不是数字，需要加单引号
				}
				if (StringUtils.isEmpty(colvalue))
					setSQL.append(colname).append("=").append("''").append(",");
				else
					setSQL.append(colname).append("=").append(colvalue).append(
							",");

			}
			String setSQLsp = setSQL.substring(0, setSQL.lastIndexOf(","));// 去掉最后逗号

			getString.append(setSQLsp).append(" where 1=1 ").append(whereSQL);

		}
		return getString.toString();
	}
	
	
	public static Object getNowDbTimeObj() {
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		return timeStamp;
	}

	/**
	 *  返回sql串  带？号的形式，参数传给 ls变量   
	 * @param List<Object> ls  SQL实际参数变量  ls.add()
	 * @param map SQL 字段 健值对
	 * @param String tableName  表名
	 * @param String whereSQL
	 * @return sql  生成  update tablename set col_1=?, col_2=?;
	 * @throws Exception
	 */
	public static String getUpdateSqlByMap(List<Object> ls, Map map, String tableName,
			String whereSQL) {
		ls.clear();
		StringBuffer getString = new StringBuffer(" update " + tableName
				+ " set ");// 返回的sql
		StringBuffer setSQL = new StringBuffer("");// 拼接列名

		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			String colname = "";
			String colvalue = "";
			Object colObj = null;
			while (it.hasNext()) {
				colname = it.next().toString(); // 获得key值
				colObj = map.get(colname);
				colvalue = CommFun.nvl(map.get(colname));
				if ( colvalue.indexOf(".NEXTVAL") < 0 && !"SYSDATE".equals(colvalue) && !"now()".equals(colvalue) ) {				
					setSQL.append(colname).append("=").append("?").append(",");
//					ls.add(EncodeUtils.htmlUnescape(colvalue.trim()));
					if ( colObj instanceof String ) {//考虑前台部分String 可能为空格, String 对象需要用nvl处理下
						ls.add(colvalue);
					} else {
						ls.add(colObj);
					}
				} else {
					// NEXTVAL  SYSDATE 
					setSQL.append(colname).append("=").append(colvalue).append(",");
				}				

			}
			String setSQLsp = setSQL.substring(0, setSQL.lastIndexOf(","));// 去掉最后逗号

			getString.append(setSQLsp).append(" where 1=1 ").append(whereSQL);

		}
		return getString.toString();
	}
	
	public static String getFmtInsSQL(String tablename, String insertCols ) {
		StringBuffer sqlbuf = new StringBuffer();
//		String insertCols = "PARAM_ID, SMALL_CELL_CODE, INST_NAME, INST_SEQ, INST_VAL, INST_TIME, INST_FLG, UPD_TIME, UPD_USER";	
//		sqlbuf.append("INSERT INTO SMALL_CELL_PARAM_INST ("+insertCols+") VALUES ("+insertCols.replaceAll("\\w+_\\w+|\\w+", "?")+")" );
		sqlbuf.append("INSERT INTO "+tablename+ "("+insertCols+") VALUES ("+insertCols.replaceAll("\\w+_\\w+|\\w+", "?")+")" );
		return sqlbuf.toString();
	}
	

	/**
	 *  返回sql串  带？号的形式，参数传给 ls变量    	
	 * @param List<Object> ls  SQL实际参数变量  ls.add()
	 * @param map SQL 字段 健值对
	 * @param String tableName  表名
	 * @param String whereSQL
	 * @return sql  生成  insert tablename (col_1, col_2) values (?,?)
	 * @throws Exception
	 */
	public static String getInsertSqlByMap(List<Object> ls, Map map, String tableName) {
//		ls = new ArrayList<Object>();
		ls.clear();
		StringBuffer getString = new StringBuffer(" INSERT INTO " + tableName);// 返回的sql
		StringBuffer col = new StringBuffer("(");// 拼接列名
		StringBuffer val = new StringBuffer("VALUES(");// 拼接value值
		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			String colname = "";
			String colvalue = "";
			Object colObj = null;
			while (it.hasNext()) {
				colname = it.next().toString(); // 获得key值
				colObj = map.get(colname);
				col.append(colname + ",");				
				colvalue = CommFun.nvl(colObj);
				if ( colvalue.indexOf(".NEXTVAL") < 0 && !"SYSDATE".equals(colvalue) && !"now()".equals(colvalue)  ) {								
					val.append("?,");
//					ls.add(EncodeUtils.htmlUnescape(colvalue.trim()));
					if ( colObj instanceof String ) {//考虑前台部分String 可能为空格, String 对象需要用nvl处理下
						ls.add(colvalue);
					} else {
						ls.add(colObj);
					}
					
				} else {
					val.append(colvalue + ","); // 获得vaule值
					
				}
				
			}
			String colsp = col.substring(0, col.lastIndexOf(","));// 去掉最后逗号
			String valsp = val.substring(0, val.lastIndexOf(","));// 去掉最后逗号
			getString.append(colsp + ") ");
			getString.append(valsp + ")");  
		}
		return getString.toString();
	}

	/*
	 * @return String 返回sql串      
	 */
	public static String getInsertSqlByMap(Map map, String tableName) {
		StringBuffer getString = new StringBuffer(" INSERT INTO " + tableName);// 返回的sql
		StringBuffer col = new StringBuffer("(");// 拼接列名
		StringBuffer val = new StringBuffer("VALUES(");// 拼接value值
		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			while (it.hasNext()) {
				String colname = it.next().toString(); // 获得key值
				col.append(colname + ",");
				String colvalue = CommFun.getObject2Str(map.get(colname));
				if (StringUtils.isNumeric(colvalue) == false
						&& colvalue.indexOf(".NEXTVAL") < 0
						&& !"SYSDATE".equals(colvalue)
						&& !"now()".equals(colvalue) ) {
					colvalue = "'" + colvalue + "'";// 不是数字，需要加单引号
				}
				if (StringUtils.isEmpty(colvalue))
					val.append("null,");
				else
					val.append(colvalue + ","); // 获得vaule值
			}
			String colsp = col.substring(0, col.lastIndexOf(","));// 去掉最后逗号
			String valsp = val.substring(0, val.lastIndexOf(","));// 去掉最后逗号
			getString.append(colsp + ") ");
			getString.append(valsp + ")");
		}
		return getString.toString();
	}
	
	
	
	/**
	 * 打印Sql语句
	 * @param sql
	 * @param args
	 */
	public static String printSql(String sql, Object[] args){
		String[] sqlArray = sql.split("\\?");
		StringBuffer new_sql = new StringBuffer();
		for(int i=0; i<sqlArray.length; i++){
			new_sql.append(sqlArray[i]);
			if(i < args.length){
				new_sql.append("'").append(CommFun.nvl(args[i])).append("'");
			}
		}
		System.out.println(new_sql.toString());
		return new_sql.toString();
	}
	
	
	

	// 获得Map的key,得到形式如：a,b,c,d
	public String obtainKey(Map map) {
		Set a = map.keySet();
		Iterator s = a.iterator(); 
		String b = "";
		while (s.hasNext()) {
			if (b != "") {
				b = b + "," + s.next();
			} else {
				b = s.next() + "";
			}
		}
		return b;
	}

	// 获得Map的values,得到形式如：'a','b','c'
	public String obtainValues(Map map) {
		Collection collection = map.values();
		Iterator itertor = collection.iterator();

		String k = "";
		while (itertor.hasNext()) {
			if (k != "") {
				k = k + ",'" + itertor.next() + "'";
			} else {
				k = "'" + itertor.next() + "'";
			}
		}
		return k;
	}
}
