package com.rn.util;
 
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.PrintService;
 
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
 
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
 
import net.sf.json.JSONObject;
 
/**
 * 自助打印终端打印工具类
 * @author wangjiao01
 *
 */		
public class PDFPrintUtil {
		
	public static void main(String[] args) throws IOException { 
	 
	//	getNewPDFPath(downLoadByUrl);
		List<String> urls= new ArrayList<String>();
		urls.add("http://localhost:8080/cbbdata/aa/one.pdf"); 
		urls.add("http://localhost:8080/cbbdata/aa/two.pdf");
		for (String url : urls) {
			byte[] downLoadByUrl = downLoadByUrl(url);
			String doPrintByPDFBox = doPrintByPDFBox(downLoadByUrl,"HP Laser NS 1020 PCLmS",1);
			System.out.println(doPrintByPDFBox);
		}
		//String doPrintByPDFBox = doPrintByPDFBox(downLoadByUrl,"HP Laser NS 1020 PCLmS",1);
		//System.out.println(doPrintByPDFBox);
	}	
	 	
	/**	 
	 * 获取临时生成的pdf文件路径
	 * @param pdfData
	 * @return
	 */
	public static String getNewPDFPath(byte[] pdfData) {
		
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newPdfName = df.format(new Date());
		String newPdfPath = "E:\\imagefile\\pdf\\" + newPdfName + ".pdf";// 随具体环境变化
		
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(newPdfPath);
			outputStream.write(pdfData);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 	return newPdfPath;
	}
	
	/**
	 * 执行打印
	 * @param pdfData pdf文档对应的二进制数组
	 * @param printerName 打印机标识
	 * @param copyCount 打印份数
	 * @return
	 * @throws IOException
	 */
	public static String doPrintByPDFBox(byte[] pdfData, String printerName, Integer copyCount) throws IOException {
		String result = null;
		PDDocument document = null;
		try {
			document = PDDocument.load(pdfData);
			PrinterJob printerJob = PrinterJob.getPrinterJob();
			
			// 查找并设置打印机
			PrintService[] printServices = PrinterJob.lookupPrintServices();
			if(printServices == null || printServices.length == 0) {
				result = getPrintMessage(false, "打印失败，计算机未安装打印机，请检查。");
//				makeSound("打印失败，计算机未安装打印机，请检查。");
				return result;
			}
			PrintService printService = null;
			for(int i = 0; i < printServices.length; i++) {
				if(printServices[i].getName().equalsIgnoreCase(printerName)) {
					System.out.println(printServices[i].getName());
					printService = printServices[i];
					break;
				}
			}
			if(printService != null) {
				printerJob.setPrintService(printService);
			} else {
				result = getPrintMessage(false, "打印失败，未找到名称为" + printerName + "的打印机，请检查。");
//				makeSound("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
				return result;
			}
			
			// 设置纸张
			PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
			PageFormat pageFormat = new PageFormat();					
			pageFormat.setOrientation(PageFormat.PORTRAIT);				
			pageFormat.setPaper(getPaper(printerName));								
			// Book 的方式实现打印多张（已测试，可行）												
			Book book = new Book();															
			//book.append(pdfPrintable, pageFormat, document.getNumberOfPages());			
			book.append(pdfPrintable, pageFormat,1);	
			printerJob.setPageable(book);												
			// PDFPageable 的方式实现打印多张（未测试，应该也可行）		
//			PDFPageable pdfPageable = new PDFPageable(document);		
//			pdfPageable.append(pdfPrintable, pageFormat, document.getNumberOfPages());
//			printerJob.setPageable(pdfPageable);						
																		
			// 测试
			System.out.println(document.getNumberOfPages());
			System.out.println(book.getNumberOfPages());
//			System.out.println(pdfPageable.getNumberOfPages());
			
			// 执行打印
			printerJob.setCopies(copyCount);
			printerJob.print();
			result = getPrintMessage(true, "打印成功。");
//			makeSound("打印成功，请取件。");
		} catch (Exception e) {
			e.printStackTrace();
			result = getPrintMessage(false, "打印失败：发生异常。");
//			makeSound("打印失败，打印时发生异常，请检查。");
		} finally {
			if(document != null) {
				document.close();// 起初文件删除失败，关闭文档之后，删除成功
			}
		}
		
		return result;
	}
 
	/**
	 * 获取打印结果信息，成功或失败，用以返回前台界面
	 * @param isPrintSuccess
	 * @param message
	 * @return
	 */
	public static String getPrintMessage(boolean isPrintSuccess, String message) {
		JSONObject object = new JSONObject();
		if(isPrintSuccess) {
			object.put("code", 1);
		}else {
			object.put("code", 0);
		}
		object.put("message", message);
		System.out.println(message);
		return object.toString();
	}
	
	/**
	 * 删除打印过程中创建的临时pdf文件
	 * @param newPdfPath
	 * @return
	 */
	public static boolean deleteFile(String newPdfPath) {
		File file = new File(newPdfPath);
		if(file.exists()) {
			if(file.isFile()) {
				return file.delete();
			}
		}else {
			System.out.println("文件 " + newPdfPath + " 不存在！");
		}
		return false;
	}
	
	/**
	 * 打印语音提示：成功或失败，并提示失败原因
	 * @param message
	 */
	public static void makeSound(String message) {
		ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
		try {
			// 音量 0-100
			sap.setProperty("Volume", new Variant(100));
			// 语音朗读速度 -10 到 +10
			sap.setProperty("Rate", new Variant(0));
			// 获取执行对象
			Dispatch sapo = sap.getObject();
			// 执行朗读
			Dispatch.call(sapo, "Speak", new Variant(message));
			// 关闭执行对象
			sapo.safeRelease();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭应用程序连接
			sap.safeRelease();
		}
	}
	
	/**
	 * 根据打印机名称判断是单据打印还是条码打印，进而创建对应Paper对象并返回
	 * @param printerName
	 * @return
	 */
	public static Paper getPaper(String printerName) {
		Paper paper = new Paper();
		// 默认为A4纸张，对应像素宽和高分别为 595, 848
		int width = 595;
		int height = 848;
		// 设置边距，单位是像素，10mm边距，对应 28px
		int marginLeft = 10;
		int marginRight = 0;
		int marginTop = 10;
		int marginBottom = 0;
		if(printerName.contains("bar")) {
			// 云南大学条码纸张规格70mm宽*40mm高，对应像素值为 198, 113
			width = 198;
			height = 113;
		}
		paper.setSize(width, height);
		// 下面一行代码，解决了打印内容为空的问题
		paper.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
		return paper;
	}
	/**
	 * 下方 林
	 */
	 /**
     * 从网络Url中下载文件
     * @param urlStr
     * @throws IOException
     */
    public static byte[]  downLoadByUrl(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(5*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        if(inputStream!=null){
            inputStream.close();
        }
        return getData;
    }


    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    } 
	
	
 
}