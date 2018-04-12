package com.example.renshaole.testarcgis.widget;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class HexadecimalConver {  
    
	private static String hexString="0123456789ABCDEF";
	/**
	 * 将16进制数字字符串以GBK格式解码为字符串
	 */
	  public static String toStringHex(String s){
	    byte[] baKeyword = new byte[s.length()/2];  
	    for(int i = 0; i < baKeyword.length; i++){  
	     try{  
	      baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
	     }catch(Exception e){
	      e.printStackTrace();  
	     }  
	    }  
	    try{  
	     s = new String(baKeyword, "GB2312");
	    }catch (Exception e1){
	     e1.printStackTrace();  
	    }  
	    return s;  
	   }  
	     
	   /** 
	    * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	    */  
	   public static String encode(String str){
	    //根据默认编码获取字节数组
	    byte[] bytes=str.getBytes();
	    StringBuilder sb=new StringBuilder(bytes.length*2);
	    //将字节数组中每个字节拆解成2位16进制整数
	    for(int i=0;i<bytes.length;i++){  
	     sb.append(hexString.charAt((bytes[i]&0xf0)>>4));  
	     sb.append(hexString.charAt((bytes[i] & 0x0f)));
	    }  
	    return sb.toString();  
	   }  
	   /** 
	    * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	    */  
	   public static String decode(String bytes)
	   {  
	        ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
	        //将每2位16进制整数组装成一个字节
	        for(int i=0;i<bytes.length();i+=2)
	        baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));  
	        String bb = "";
	        try {  
	            bb = new String(baos.toByteArray(), "GB2312");
	        } catch (Exception e) {
	            e.printStackTrace();  
	        }  
	    return bb;  
	   } 
	   //编码 将汉字 解码为gbk格式的16进制数据
	   public static String encodeCN(String data) {
	       byte[] bytes;  
	       try {  
	           bytes = data.getBytes("gbk");  
	           StringBuilder sb = new StringBuilder(bytes.length * 2);
	 
	           for (int i = 0; i < bytes.length; i++) {
	           	//获得一个字节的前四位二进制，与后四位二进制数据，然后转为16进制放到sb中
	               sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));  
	               sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
	           }
	           //sb.tostring 返回的一系列的16进制字符构成的字符串，可以用来组成数据
	           return sb.toString();  
	       } catch (UnsupportedEncodingException e) {
	           e.printStackTrace();  
	       }  
	       return "";  
	   }  
	     
	   public static String encodeStr(String data) {
	       String result = "";
	       byte[] bytes;  
	       try {  
	           bytes = data.getBytes("gbk");  
	           for (int i = 0; i < bytes.length; i++) {  
	               result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
	           }  
	           return result;  
	       } catch (UnsupportedEncodingException e) {
	           e.printStackTrace();  
	       }  
	       return "";  
	   }  
	     
	   /** 
	    * 判定是否为中文汉字
	    * @param data 
	    * @return 
	    */  
	   public static boolean isCN(String data) {
	       boolean flag = false;  
	       String regex = "^[\u4e00-\u9fa5]*$";
	       if (data.matches(regex)) {  
	           flag = true;  
	       }  
	       return flag;  
	   }  
	 /**
	  * 将字符串转化为16进制
	  * */
	   public static String getHexResult(String targetStr) {
	       StringBuilder hexStr = new StringBuilder();
	       int len = targetStr.length();  
	       if (len > 0) {  
	           for (int i = 0; i < len; i++) {  
	               char tempStr = targetStr.charAt(i);  
	               String data = String.valueOf(tempStr);
	               if (isCN(data)) {  
	                   hexStr.append(encodeCN(data));  
	               } else {  
	                   hexStr.append(encodeStr(data));  
	               }  
	           }  
	       }  
	       return hexStr.toString();  
	   }  
	}
