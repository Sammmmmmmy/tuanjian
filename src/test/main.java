package test;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import DB.database;
import net.sf.json.JSONArray;

public class main {
	public static void main(String[] args) throws SQLException {
		//idDeleteTest();
		//String url = "C:\\Users\\dell\\Desktop\\1.jpg";
		//FileDeleteTest(url);
		//sqlDeleteTest();
//		sizeOfJSONArray();
		//sizeofSet();
		numbertest();
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
	//String和int转换
	public static void IntAndString() throws SQLException {
		database.connect();
		String sql = "select * form test where Class = 2";
		ResultSet set = database.executeQuery(sql);
		
		String test = "";
		if(set.next()) {
			test = set.getInt(0)+"";
		}
		database.disconnect();
	}
	//JSONArray的大小
	public static void sizeOfJSONArray() {
		JSONArray array = new JSONArray();
		System.out.println(array.size());
	}
	//ResultSize的大小
	public static void sizeofSet() throws SQLException {
		String sql = "select * from login";
		database.connect();
		ResultSet set = database.executeQuery(sql);

		
		while(set.next())
			System.out.println(set.getString("pwd"));
		set.close();
		database.disconnect();

	}
	//数据库数字的测试
	//结论：当数字列为空时通过查询得到内容为0
	public static void numbertest() throws SQLException {
		String sql = "select * from test";
		database.connect();
		ResultSet set = database.executeQuery(sql);
		while(set.next()) {
			System.out.println(set.getInt("id"));
		}
	}
	
}
