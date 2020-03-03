package com.rn.util;
  
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling; 

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Arrays; 
import java.util.LinkedList;

import javax.print.PrintService; 


public class TestPdfPrint {

    //获取指定目录下的pdf文件,以集合方式返回
    public static LinkedList<File> getFiles(String path) {

        File file = new File(path);
        LinkedList<File> list = new LinkedList<File>();

        //保存所有pdf文件的对象
        LinkedList<File> pdfList = new LinkedList<File>();

        //该路径对应的文件或文件夹是否存在
        if (file.exists()) {

            //如果该路径为文件或空文件夹
            if (null == file.listFiles()) {
                if(file.getAbsolutePath().toLowerCase().endsWith(".pdf"))
                    pdfList.add(file);
            }

            //如果该路径为非空文件夹
            else {
                //将该路径下的所有文件（文件或文件夹）对象加入队列
                list.addAll(Arrays.asList(file.listFiles()));
                //遍历该队列
                while (!list.isEmpty()) {
                    //匹配一个清除一个
                    File firstF = list.removeFirst();
                    //这里不论是文件夹还是文件，只需判断是否以“.pdf”结尾
                    if(firstF.getAbsolutePath().endsWith(".pdf"))
                        pdfList.add(firstF);

                    File[] files = firstF.listFiles();

                    if (files==null) {
                        continue;
                    }
                    for (File f : files) {
                        if (f.isDirectory()) {
                            //System.out.println("文件夹:" + f.getAbsolutePath());
                            list.add(f);
                        } else {
                            //System.out.println("文件:" + f.getAbsolutePath());
                            if(f.getAbsolutePath().toLowerCase().endsWith(".pdf"))
                                pdfList.add(f);
                        }
                    }
                }
            }

        } else {
            System.out.println("文件不存在!");
        }

        //测试输出所有pdf文件的路径
        /*for(File f : pdfList)
            System.out.println(f.getAbsolutePath());*/

        return pdfList;
    }

//  public static void main(String[] args) throws IOException, PrinterException {
//	  LinkedList<File> pdfList = getFiles("E:\\imagefile");
//  //    String pdfPath="a.pdf";
//      for (File f : pdfList){
//          //System.out.println(f.getAbsolutePath());
//          pdfPath=f.getAbsolutePath(); 
//          
//          // 使用打印机的名称
//          String printerName = "HP Laser NS 1020 PCLmS";//指定打印机
//          System.out.println(pdfPath);
//         // pdfPath = "E:\\imagefile\\demo.pdf";
//          File file = new File(pdfPath);
//          boolean isChoose;
//          // 读取pdf文件
//          PDDocument document = PDDocument.load(file);
//          // 创建打印任务
//          PrinterJob job = PrinterJob.getPrinterJob();
//          // 遍历所有打印机的名称
//          for (PrintService ps : PrinterJob.lookupPrintServices()) {
//              String psName = ps.toString();
//              // 选用指定打印机
//              if (psName.equals(printerName)) {
//                  isChoose = true;
//                  job.setPrintService(ps);
//                  break;
//              }
//          }
//          job.setPageable(new PDFPageable(document));
//
//          Paper paper = new Paper();
//          // 设置打印纸张大小
//          paper.setSize(598,842); // 1/72 inch
//          // 设置打印位置 坐标
//          paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins
//          // custom page format
//          PageFormat pageFormat = new PageFormat();
//          pageFormat.setPaper(paper);
//          // override the page format
//          Book book = new Book();
//          // append all pages 设置一些属性 是否缩放 打印张数等
//          book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pageFormat, 1);
//          job.setPageable(book);
//          // 开始打印
//          job.print();
//       }
//}
    
 
    
    public static void main(String[] args) throws PrinterException, InvalidPasswordException, IOException {
    	// 使用打印机的名称
    	 LinkedList<File> pdfList = getFiles("E:\\imagefile\\gg");
    	 String pdfPath = "E:\\imagefile\\gg\\demo1.pdf";
    	  
	      for (File f : pdfList){
	     	 pdfPath = f.getAbsolutePath();
	    	  System.out.println(pdfPath);
	    	  Boolean isChoose;
	      	File file = new File(pdfPath);
	      	// 读取pdf文件
	      	PDDocument document = PDDocument.load(file);
	      	// 创建打印任务
	      	PrinterJob job = PrinterJob.getPrinterJob();
	      	String printerName = "HP Laser NS 1020 PCLmS";//指定打印机
	      	// 遍历所有打印机的名称
	      	for (PrintService ps : PrinterJob.lookupPrintServices()) {
	      	String psName = ps.toString();
	      	    // 选用指定打印机
	      	    if (psName.equals(printerName)) {
	      	        isChoose = true;
	      	        job.setPrintService(ps);
	      	        break;
	      	    }
	      	}
	      	 
	      	job.setPageable(new PDFPageable(document));
	      	 
	      	Paper paper = new Paper();
	      	// 设置打印纸张大小
	      	paper.setSize(598,842); // 1/72 inch
	      	// 设置打印位置 坐标
	      	paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins
	      	// custom page format
	      	PageFormat pageFormat = new PageFormat();
	      	pageFormat.setPaper(paper);
	      	// override the page format
	      	Book book = new Book();
	      	// append all pages 设置一些属性 是否缩放 打印张数等
	      //	book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pageFormat, 1); 
	      	book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pageFormat, document.getNumberOfPages());
	      	job.setPageable(book);
	      	// 开始打印
	      	job.print();
     } 
	}
    
    
 
}
