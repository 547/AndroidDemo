package com.sevendream.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


//读取流的工具类
public class StreamUtils {
	//从输入流里读取数据并转成String类型的
    public static String readFromStream(InputStream in) throws IOException {
    	
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		int len=0;
		//缓存
		byte[] buffer=new byte[1024];
		while ((len=in.read(buffer))!=-1) {
			baos.write(buffer, 0, len);
		}
		String result=baos.toString();
		in.close();
		baos.close();
		
    	return result;
	}
}
