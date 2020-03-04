package com.cakes.frameworks.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


/** 

*/
public class PoiXlsx {
	private static int type = 1;
	private List<String> rowlist = new ArrayList<String>();
	private List<Map<String,Object>> dbMaplist = new ArrayList<Map<String,Object>>();
	private String colsname;

	// The type of the data value is indicated by an attribute on the cell.
	// The value is usually in a "v" element within the cell.
	enum xssfDataType {
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
	}

	int countrows = 0;

	// Derived from http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
	// <p>
	// Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
	// http://www.ecma-international.org/publications/standards/Ecma-376.htm
	// </p><p>
	// A web-friendly version is http://openiso.org/Ecma/376/Part4
	class MyXSSFSheetHandler extends DefaultHandler {

		// Table with styles
		private StylesTable stylesTable;

		// Table with unique strings
		private ReadOnlySharedStringsTable sharedStringsTable;

		// Destination for data
		private final PrintStream output;

		// Number of columns to read starting with leftmost
		private final int minColumnCount;

		// Set when V start element is seen
		private boolean vIsOpen;

		// Set when cell start element is seen;
		// used when cell close element is seen.
		private xssfDataType nextDataType;

		// Used to format numeric cell values.
		private short formatIndex;
		private String formatString;
		private final DataFormatter formatter;

		private int thisColumn = -1;
		// The last column printed to the output stream
		private int lastColumnNumber = -1;

		// Gathers characters as they are seen.
		private StringBuffer value;

		// Accepts objects needed while parsing.
		// @param styles Table of styles
		// @param strings Table of shared strings
		// @param cols Minimum number of columns to show
		// @param target Sink for output
		public MyXSSFSheetHandler(StylesTable styles,
				ReadOnlySharedStringsTable strings, int cols, PrintStream target) {
			this.stylesTable = styles;
			this.sharedStringsTable = strings;
			this.minColumnCount = cols;
			this.output = target;
			this.value = new StringBuffer();
			this.nextDataType = xssfDataType.NUMBER;
			this.formatter = new DataFormatter();
		}

		// @see
		// org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
		// java.lang.String, java.lang.String, org.xml.sax.Attributes)
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {

			if ("inlineStr".equals(name) || "v".equals(name)) {
				vIsOpen = true;
				// Clear contents cache
				value.setLength(0);
			}
			// c => cell
			else if ("c".equals(name)) {
				// Get the cell reference
				String r = attributes.getValue("r");
				int firstDigit = -1;
				for (int c = 0; c < r.length(); ++c) {
					if (Character.isDigit(r.charAt(c))) {
						firstDigit = c;
						break;
					}
				}
				thisColumn = nameToColumn(r.substring(0, firstDigit));

				// Set up defaults.
				this.nextDataType = xssfDataType.NUMBER;
				this.formatIndex = -1;
				this.formatString = null;
				String cellType = attributes.getValue("t");
				String cellStyleStr = attributes.getValue("s");
				if ("b".equals(cellType))
					nextDataType = xssfDataType.BOOL;
				else if ("e".equals(cellType))
					nextDataType = xssfDataType.ERROR;
				else if ("inlineStr".equals(cellType))
					nextDataType = xssfDataType.INLINESTR;
				else if ("s".equals(cellType))
					nextDataType = xssfDataType.SSTINDEX;
				else if ("str".equals(cellType))
					nextDataType = xssfDataType.FORMULA;
				else if (cellStyleStr != null) {
					// It's a number, but almost certainly one
					// with a special style or format
					int styleIndex = Integer.parseInt(cellStyleStr);
					XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
					this.formatIndex = style.getDataFormat();
					this.formatString = style.getDataFormatString();
					if (this.formatString == null)
						this.formatString = BuiltinFormats
								.getBuiltinFormat(this.formatIndex);
				}
			}

		}

		// @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
		// java.lang.String, java.lang.String)
		public void endElement(String uri, String localName, String name)
				throws SAXException {

			String thisStr = null;

			// v => contents of a cell
			if ("v".equals(name)) {
				// Process the value contents as required.
				// Do now, as characters() may be called more than once
				switch (nextDataType) {

				case BOOL:
					char first = value.charAt(0);
					thisStr = first == '0' ? "FALSE" : "TRUE";
					break;

				case ERROR:
					thisStr = "\"ERROR:" + value.toString() + '"';
					break;

				case FORMULA:
					// A formula could result in a string value,
					// so always add double-quote characters.
					thisStr = '"' + value.toString() + '"';
					break;

				case INLINESTR:
					// TODO: have seen an example of this, so it's untested.
					XSSFRichTextString rtsi = new XSSFRichTextString(
							value.toString());
					thisStr = '"' + rtsi.toString() + '"' ;
					break;

				case SSTINDEX:
					String sstIndex = value.toString();
					try {
						int idx = Integer.parseInt(sstIndex);
						XSSFRichTextString rtss = new XSSFRichTextString(
								sharedStringsTable.getEntryAt(idx));
						thisStr = '"' + rtss.toString() + '"';
					} catch (NumberFormatException ex) {
						output.println("Failed to parse SST index '" + sstIndex
								+ "': " + ex.toString());
					}
					break;

				case NUMBER:
					String n = value.toString();
					if (this.formatString != null)
						thisStr = formatter.formatRawCellContents(
								Double.parseDouble(n), this.formatIndex,
								this.formatString);
					else
						thisStr = n;
					break;

				default:
					thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
					break;
				}

				// Output after we've seen the string contents
				// Emit commas for any fields that were missing on this row
				if (lastColumnNumber == -1) {
					lastColumnNumber = 0;
				}
				
//				for (int i = lastColumnNumber; i < thisColumn; ++i) {
//					output.print(',');					
//				}
				
				//补充空值
				for (int i = lastColumnNumber; i < thisColumn-1; ++i) {				
					rowlist.add(null);
				}
				

				// Might be the empty string.
				if ( CommFun.isEmpty(thisStr)) {//测试空值
					System.out.println("----test Might be the empty string value---");					
				}
				
				rowlist.add(thisStr.replaceAll("\"", ""));
//				output.print(thisStr);
			
				// Update column
				if (thisColumn > -1) {
					lastColumnNumber = thisColumn;
				}

			} else if ("row".equals(name)) {

				// Print out any missing commas if needed
				if (minColumns > 0) {
					// Columns are 0 based
					if (lastColumnNumber == -1) {
						lastColumnNumber = 0;
					}
					for (int i = lastColumnNumber; i < (this.minColumnCount); i++) {
						output.print(',');
					}
				}

				// We're onto a new row

//				output.println();
				output.println("  row："+countrows);
				
				if ( countrows == 0 ) {
					optKeyRows ( countrows,rowlist);
				} else {
					optRows( countrows,rowlist);
				}
				
				countrows++;//
				rowlist.clear();
				lastColumnNumber = -1;
			}

		}
		
		//获取1行数据
		private String create_user = "cbook_1";

		
		private String db_old_keys = "FIRST_NAME,LAST_NAME,DEPART,POSITION,EMAIL,TEL";
		private String db_new_keys = "FIRST_NAME,LAST_NAME,DEPART,POSITION,EMAIL,TEL";
		

		private Map<String,String> db_key_map = new HashMap<String,String>();   //map.put(i, "old_key");
		private Map<String,String> db_key_comp_map = new HashMap<String,String>();//标题映射   map.put("old_key", "new_key");

		private String tmpVal = "";
		private String tmpKey = "";
		
		
		/**
		 * 生成标题key
		 * @param curRow
		 * @param rowlist
		 */
		public void optKeyRows(int curRow, List<String> rowlist) {
			 try {		
				
				    String[]  db_old_key_arr = db_old_keys.split(",");
				    String[]  db_new_key_arr = db_new_keys.split(","); 
				    int k = 0;
				    System.out.println(" ------chk---------"+ (db_new_key_arr.length-db_old_key_arr.length));
				    for ( String old_key  : db_old_key_arr) {
				    	db_key_comp_map.put(old_key, db_new_key_arr[k]);
				    	k++;
				    }
				    
				  
				    //加载数据字典
				    loadData();
				    
				 
				    List<String>  keyList = CommFun.str2List(db_old_keys.toUpperCase(), ",");
					for (int i = 0; i < rowlist.size(); i++) {
						tmpVal = CommFun.nvl(rowlist.get(i));						
						tmpVal = tmpVal.toUpperCase();
						if ( keyList.contains(tmpVal)) {
							db_key_map.put(i+"", tmpVal);
						}
					}
//					 output.println("  row："+curRow);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		
		}
		
		/**
		 * 加载数据
		 */
		public  void loadData() {
//			  initBaseInfo();

		}
		

		
		/**
		 * 生成SQL语句
		 * @param curRow
		 * @param rowlist
		 */
		public void optRows(int curRow, List<String> rowlist) {
			 try {	
				 
				 Map<String, Object> tmpMap = new HashMap<String,Object>();
				    String[] cols = colsname.split(",");
					for (int i = 0; i < rowlist.size(); i++) {
						
//						if ( db_key_map.containsKey(i+"")) {
							tmpVal = CommFun.nvl(rowlist.get(i));
							tmpKey = cols[i];
//							tmpKey = db_key_map.get(i+"");//excel 里的key
//							tmpKey = db_key_comp_map.get(tmpKey);//获取新的key
							
							
//							if ( "OWNER_TYPE".equals(tmpKey)) {
//								tmpVal = CommFun.decode(tmpVal, "PERSON", "0", "1");
//							} else if ( "OLD_MODEL_NAME".equals(tmpKey)) {//
//								
//								
//							} else if ( "DEALER_NAME".equals(tmpKey)) {//经销商ID
//								
//							} 					
							tmpMap.put(tmpKey, tmpVal);
//						}
						
//						System.out.print("'" + rowlist.get(i) + "',");
					}
					
					List<Object>  ls = new ArrayList<Object>();				
					tmpMap.put("upd_user", create_user);
					
					
					dbMaplist.add(tmpMap);
					
//					String runSQL = SqlUtils.getInsertSqlByMap(ls, tmpMap, "t_meet_user");
//					String insertSql = printSql(runSQL, ls.toArray())+";";
//					insertSql += "\r\n";
//					FileUtils.writeStringToFile(new File("D:\\ftp.sql"), insertSql, true);
//					 output.println("  row："+curRow);
			 } catch (Exception e) {
				 e.printStackTrace();
			 }
		
		}
		
		
		public  String printSql(String sql, Object[] args){
			if(args==null){
				System.out.println(sql.toString());
				return "";
			}
			String[] sqlArray = sql.split("\\?");
			StringBuffer new_sql = new StringBuffer();
			for(int i=0; i<sqlArray.length; i++){
				new_sql.append(sqlArray[i]);
				if(i < args.length){
					new_sql.append("'").append(args[i]).append("'");
				}
			}
//			System.out.println(new_sql.toString()+";");
			return new_sql.toString();
		}
	

		// Captures characters only if a suitable element is open.
		// Originally was just "v"; extended for inlineStr also.
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (vIsOpen)
				value.append(ch, start, length);
		}

		// Converts an Excel column name like "C" to a zero-based index.
		// @param name
		// @return Index corresponding to the specified name
		private int nameToColumn(String name) {
			int column = -1;
			for (int i = 0; i < name.length(); ++i) {
				int c = name.charAt(i);
				column = (column + 1) * 26 + c - 'A';
			}
			return column;
		}

	}

	// /////////////////////////////////////

	private OPCPackage xlsxPackage;
	private int minColumns;
	private PrintStream output;

	// Creates a new XLSX -> CSV converter
	// @param pkg The XLSX package to process
	// @param output The PrintStream to output the CSV to
	// @param minColumns The minimum number of columns to output, or -1 for no
	// minimum
	public PoiXlsx(OPCPackage pkg, PrintStream output, int minColumns, String colsname) {
		this.xlsxPackage = pkg;
		this.output = output;
		this.minColumns = minColumns;
		this.colsname = colsname;
	}

	// Parses and shows the content of one sheet
	// using the specified styles and shared-strings tables.
	// @param styles
	// @param strings
	// @param sheetInputStream
	public void processSheet(StylesTable styles,
			ReadOnlySharedStringsTable strings, InputStream sheetInputStream)
			throws IOException, ParserConfigurationException, SAXException {

		InputSource sheetSource = new InputSource(sheetInputStream);
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader sheetParser = saxParser.getXMLReader();
		ContentHandler handler = new MyXSSFSheetHandler(styles, strings,
				this.minColumns, this.output);
		sheetParser.setContentHandler(handler);
		sheetParser.parse(sheetSource);
	}

	// Initiates the processing of the XLS workbook file to CSV.
	// @throws IOException
	// @throws OpenXML4JException
	// @throws ParserConfigurationException
	// @throws SAXException

	public List<Map<String,Object>> process() throws IOException, OpenXML4JException,
			ParserConfigurationException, SAXException {

		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(
				this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);

		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader
				.getSheetsData();
		int index = 0;
		while (iter.hasNext()) {
			InputStream stream = iter.next();
			String sheetName = iter.getSheetName();
//			this.output.println();
			this.output.println(sheetName + " [index=" + index + "]:");
			processSheet(styles, strings, stream);
			stream.close();
			++index;
			break; //就解析第一个sheet xuhm
		}
		
		
		List<Map<String,Object>> list = this.dbMaplist;
		return list;
	}
	
	
	public static void main(String[] args) throws Exception {
		// File xlsxFile = new File(args[0]);
//		File xlsxFile = new File("d:\\20150213.xlsx");//D:\work\英非尼迪\2015\FTP接口
		File xlsxFile = new File("D:\\work\\电信国际公司-CTG\\视频会议2\\phonebook.xlsx");//
		type = 1;
		if (!xlsxFile.exists()) {
			System.err
					.println("Not found or not a file: " + xlsxFile.getPath());
			return;
		}

		int minColumns = -1;
		// if (args.length >= 2)
		// minColumns = Integer.parseInt(args[1]);

		minColumns = 2;
		// The package open is instantaneous, as it should be.
		OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
		PoiXlsx xlsx2csv = new PoiXlsx(p, System.out, minColumns, "");
		
		List<Map<String,Object>> dbMaplist = new ArrayList<Map<String,Object>>();
		dbMaplist = xlsx2csv.process();
		
		System.out.println("dbMaplist::::"+dbMaplist.size());
	}

}
