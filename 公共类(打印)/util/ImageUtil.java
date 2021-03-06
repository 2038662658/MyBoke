package com.cakes.frameworks.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
  
public class ImageUtil {  
  
	/**
	 * 
	 * @param originalFile
	 * @param resizedFile
	 * @param newWidth
	 * @param newHeight  为-1 ,则根据 newWidth自动缩放
	 * @param quality
	 * @throws IOException
	 */
    public static void resize(File originalFile, File resizedFile,  
            int newWidth, int newHeight, float quality) throws IOException {  
  
        if (quality > 1) {  
            throw new IllegalArgumentException(  
                    "Quality has to be between 0 and 1");  
        }  
  
        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());  
        Image i = ii.getImage();  
        Image resizedImage = null;  
  
        if ( newHeight > 0 ) {
        	 resizedImage = i.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);  
        } else {
        	 int iWidth = i.getWidth(null);  
             int iHeight = i.getHeight(null);  
           
       
             if (iWidth > iHeight) {  
                 resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight)  
                         / iWidth, Image.SCALE_SMOOTH);  
             } else {  
            	  
                 if ( iHeight / iWidth >= 2)  {//如果高度比较高
                	 quality = 1f;
                	 newWidth += 100;
                 }
                
                 resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight,  
                         newWidth, Image.SCALE_SMOOTH);  
             }  
        }
       
  
        // This code ensures that all the pixels in the image are loaded.  
        Image temp = new ImageIcon(resizedImage).getImage();  
  
        // Create the buffered image.  
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),  
                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);  
  
        // Copy image to buffered image.  
        Graphics g = bufferedImage.createGraphics();  
  
        // Clear background and paint the image.  
        g.setColor(Color.white);  
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));  
        g.drawImage(temp, 0, 0, null);  
        g.dispose();  
  
        // Soften.  
        float softenFactor = 0.05f;  
        float[] softenArray = { 0, softenFactor, 0, softenFactor,  
                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };  
        Kernel kernel = new Kernel(3, 3, softenArray);  
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);  
        bufferedImage = cOp.filter(bufferedImage, null);  
  
        // Write the jpeg to a file.  
        FileOutputStream out = new FileOutputStream(resizedFile);  
  
        // Encodes image as a JPEG data stream  
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
  
        JPEGEncodeParam param = encoder  
                .getDefaultJPEGEncodeParam(bufferedImage);  
  
        param.setQuality(quality, true);  
  
        encoder.setJPEGEncodeParam(param);  
        encoder.encode(bufferedImage);  
    } // Example usage  
  
    public static void main(String[] args) throws IOException {  
//       File originalImage = new File("C:\\11.jpg");  
//       resize(originalImage, new File("c:\\11-0.jpg"),150, 0.7f);  
//       resize(originalImage, new File("c:\\11-1.jpg"),150, 1f);  
//         File originalImage = new File("D:\\work\\英非尼迪\\素材\\8.jpg");  
//         resize(originalImage, new File("D:\\work\\英非尼迪\\素材\\1207-0.jpg"),150, 0.7f);  
//         resize(originalImage, new File("D:\\work\\英非尼迪\\素材\\1207-1.jpg"),150, 1f);  
         String dirpath = "D:\\work\\英非尼迪\\素材\\online";
         String[] filePathArr = FileUts.listFiles(dirpath);
         for (String filepath : filePathArr ) {
        	 System.out.println(filepath);
        	 File originalImage = new File("D:\\work\\英非尼迪\\素材\\online\\"+filepath); 
        	 resize(originalImage, new File(dirpath+"\\mid\\"+filepath),276, 196, 0.7f);
        	 resize(originalImage, new File(dirpath+"\\thumb\\"+filepath),210, 210, 1f);
//        	 resize(originalImage, new File("D:\\work\\英非尼迪\\素材\\1207-0.jpg"),150, 0.7f);
         }
         
         
         
    }  
}  