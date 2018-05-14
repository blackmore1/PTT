package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import tool.tools;

public class AudioConvert {  
	public static int a = 0;
	 public static byte[] gZip(byte[] data) {
	  byte[] b = null;
	  try {
	   ByteArrayOutputStream bos = new ByteArrayOutputStream();
	   GZIPOutputStream gzip = new GZIPOutputStream(bos);
	   gzip.write(data);
	   gzip.finish();
	   gzip.close();
	   b = bos.toByteArray();
	   bos.close();
	  } catch (Exception ex) {
	   ex.printStackTrace();
	  }
	  return b;
	 }

	
	 public static byte[] unGZip(byte[] data) {
	  byte[] b = null;
	  try {
	   ByteArrayInputStream bis = new ByteArrayInputStream(data);
	   GZIPInputStream gzip = new GZIPInputStream(bis);
	   byte[] buf = new byte[1024];
	   int num = -1;
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   while ((num = gzip.read(buf, 0, buf.length)) != -1) {
	    baos.write(buf, 0, num);
	   }
	   b = baos.toByteArray();
	   baos.flush();
	   baos.close();
	   gzip.close();
	   bis.close();
	  } catch (Exception ex) {
	   ex.printStackTrace();
	  }
	  return b;
	 }
	 public static byte[] Zip(byte[] data) {
		  byte[] b = null;
		  try {
		   ByteArrayOutputStream bos = new ByteArrayOutputStream();
		   ZipOutputStream zip = new ZipOutputStream(bos);
		   ZipEntry entry = new ZipEntry("zip");
		   entry.setSize(data.length);
		   zip.putNextEntry(entry);
		   zip.write(data);
		   zip.closeEntry();
		   zip.close();
		   b = bos.toByteArray();
		   bos.close();
		  } catch (Exception ex) {
		   ex.printStackTrace();
		  }
		  return b;
		 }

		/***
		  * 解压Zip
		  * 
		  * @param data
		  * @return
		  */
		 public static byte[] unZip(byte[] data) {
		  byte[] b = null;
		  try {
		   ByteArrayInputStream bis = new ByteArrayInputStream(data);
		   ZipInputStream zip = new ZipInputStream(bis);
		   while (zip.getNextEntry() != null) {
		    byte[] buf = new byte[1024];
		    int num = -1;
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    while ((num = zip.read(buf, 0, buf.length)) != -1) {
		     baos.write(buf, 0, num);
		    }
		    b = baos.toByteArray();
		    baos.flush();
		    baos.close();
		   }
		   zip.close();
		   bis.close();
		  } catch (Exception ex) {
		   ex.printStackTrace();
		  }
		  return b;
		 }

	 public static void main(String[] args){
		 System.out.println(a);
		 byte[] source = new byte[10000];
		 for(int i=0;i<source.length;i++){
			 source[i] = (byte) (Math.random()*100);
		 }
		 tools.printArray(source);
		 byte[] gzip = gZip(source);
		 System.out.println(gzip.length);
		 tools.printArray(gzip);
		 byte[] ungzip = unGZip(gzip);
		 tools.printArray(ungzip);
//		 System.out.println(zip(new byte[100]).length);
	 }
}