package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import DB.database;
import net.sf.json.JSONArray;

public class main {
	public static void main(String[] args) {
		//idDeleteTest();
		//String url = "C:\\Users\\dell\\Desktop\\1.jpg";
		//FileDeleteTest(url);
		//sqlDeleteTest();
		GetDate();
	}
	//JSONArray能够存储不同类型的数据
	public static void JSONArrayTest() {
		String[] a = new String[5];
		for(int i = 0;i<5;i++)
			a[i] = ""+i;
		JSONArray array = new JSONArray();
		for(int i = 0;i<5;i++) {
			array.add(a[i]);
		}
		for(int i = 0;i<5;i++)
			System.out.println(array.get(i));
	}
	//以下sql写法也可以实现id的自增
	public static void idSelfIncreaseTest() {
		database.connect();
		
		String sql = "INSERT INTO test VALUES ()";
		database.executeUpdate(sql);
		
		database.disconnect();
	}
	//delete操作会删除多条Class为指定值的记录
	public static void sqlDeleteTest() {
		database.connect();
		String sql = "DELETE FROM test WHERE Class = 1";
		database.executeUpdate(sql);
		database.disconnect();
	}
	//用于删除某文件
	public static void FileDeleteTest(String url) {
		File f = new File(url);
		f.delete();
	}
	//获取当前时间
	public static void GetDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");//设置日期格式
		System.out.println(df.format(new Date()));//获取当前时间
	}
}
