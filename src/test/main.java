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
}
