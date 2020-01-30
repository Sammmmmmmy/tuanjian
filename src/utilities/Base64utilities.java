package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64utilities {
//	public static void main(String[] args) {
//		String url = "C:\\Users\\dell\\Desktop\\山东大学团支部工作日志\\PART1\\1-团支部建设.jpg";
//		String imagebase64 = ImageToBase64(url);
//		System.out.println(imagebase64);
//		
//		Base64ToImage(imagebase64);
//		
//		
//	}
	public static boolean Base64ToImage(String base64,String url) {
		String outputurl = "C:\\Users\\dell\\Desktop\\1.jpg";
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(base64);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
 
			OutputStream out = new FileOutputStream(url);
			out.write(b);
			out.flush();
			out.close();
 
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	public static String ImageToBase64(String url) {
		InputStream in = null;
		byte[] data = null;
 
		// 读取图片字节数组
		try {
			in = new FileInputStream(url);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
 
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串

	}
}
