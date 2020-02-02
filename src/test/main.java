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
	//JSONArray�ܹ��洢��ͬ���͵�����
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
	//����sqlд��Ҳ����ʵ��id������
	public static void idSelfIncreaseTest() {
		database.connect();
		
		String sql = "INSERT INTO test VALUES ()";
		database.executeUpdate(sql);
		
		database.disconnect();
	}
	//delete������ɾ������ClassΪָ��ֵ�ļ�¼
	public static void sqlDeleteTest() {
		database.connect();
		String sql = "DELETE FROM test WHERE Class = 1";
		database.executeUpdate(sql);
		database.disconnect();
	}
	//����ɾ��ĳ�ļ�
	public static void FileDeleteTest(String url) {
		File f = new File(url);
		f.delete();
	}
	//��ȡ��ǰʱ��
	public static void GetDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");//�������ڸ�ʽ
		System.out.println(df.format(new Date()));//��ȡ��ǰʱ��
	}
	//String��intת��
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
	//JSONArray�Ĵ�С
	public static void sizeOfJSONArray() {
		JSONArray array = new JSONArray();
		System.out.println(array.size());
	}
	//ResultSize�Ĵ�С
	public static void sizeofSet() throws SQLException {
		String sql = "select * from login";
		database.connect();
		ResultSet set = database.executeQuery(sql);

		
		while(set.next())
			System.out.println(set.getString("pwd"));
		set.close();
		database.disconnect();

	}
	//���ݿ����ֵĲ���
	//���ۣ���������Ϊ��ʱͨ����ѯ�õ�����Ϊ0
	public static void numbertest() throws SQLException {
		String sql = "select * from test";
		database.connect();
		ResultSet set = database.executeQuery(sql);
		while(set.next()) {
			System.out.println(set.getInt("id"));
		}
	}
	
}
