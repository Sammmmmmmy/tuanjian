package mainServlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DB.database;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import utilities.Base64utilities;

//�������ͼƬ��
//��֧�����
public class part2_11 extends HttpServlet {
	//����Ҫ�ĳɷ�������ַ
	private String dir = "E:\\�Ž�webͼƬ\\";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		database.connect();
		int flag = Integer.parseInt(request.getParameter("flag"));
		if(flag == 0)
			try {
				show(request,response);;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else
			try {
				update(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		database.disconnect();
	}
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String Class = request.getParameter("Class");
		//׼��sql��ִ�еõ������
		String sql = "select * from ��ѧ��ע�������¼ճ���� where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//imagebase64����
		JSONArray array = new JSONArray();
		int size = 0; 
		//ͼƬ��url��base64
		String imageurl;
		String imagebase64;
		//����set��ȡͼƬ��ַ��base64
		while(set.next()) {
			//��ȡ���ݿ��е�ͼƬ��ַ
			imageurl = set.getString("imageurl");
			//����url��ȡ�����������ͼƬ��base64
			imagebase64 = Base64utilities.ImageToBase64(imageurl);
			//������array��
			array.add(imagebase64);
			size++;
		}
		//���͵�ǰ��
		JSONObject write = new JSONObject();
		write.put("size",size);
		write.put("array", array);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(write.toString());
		pst.close();
		set.close();
	}
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException  {
		//��ǰ�˻�ȡClass��array
		String Class = request.getParameter("Class");
		JSONArray array = JSONArray.fromObject(request.getParameter("array"));
		//����ɾ�����ݿ��е�����url�Ͷ�Ӧ�ı���ͼƬ
		clear(Class);
		//��ͼƬд�����ݿ�ͷ�����
		//���ϴ�ͼƬ��ʱ���array��ͼƬ��˳���������
		rewrite(Class,array);
	}
	public void clear(String Class) throws SQLException {
		//��ѯ�༶����ͼƬ���ӷ�������ɾ��
		String selectsql = "select * from ��ѧ��ע�������¼ճ���� where Class = ?";
		PreparedStatement pst = database.getpst(selectsql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		File file;
		while(set.next()) {
			file = new File(set.getString("imageurl"));
			file.delete();
		}
		pst.close();//������bug
		set.close();
		//�����ݿ���ɾ�����е�ͼƬ��ַ
		String sql = "DELETE FROME ��ѧ��ע�������¼ճ����  WHERE Class = ?";
		pst = database.getpst(sql);
		pst.setString(1, Class);
		database.executeUpdate(sql);
		pst.close();
	}
	//����ͼƬ�ĵ�ַ��д�����ݿ�
	//д�������ָ��dir��
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		//��ͼƬ��������
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
		String date = df.format(new Date());
		String[] urls = new String[size];
		for(int i = 0;i<size;i++)
			urls[i] = dir+date+i+".jpg";
		//׼��sql���
		String sql = "INSERT INTO ��ѧ��ע�������¼ճ���� VALUES (?,?)";
		PreparedStatement pst = database.getpst(sql);
		for(int i = 0;i<size;i++) {
			pst.setString(1, urls[i]);
			pst.setString(2, Class);
			pst.execute();
		}
		pst.close();
		//��ͼƬд�������
		for(int i = 0;i<size;i++) {
			Base64utilities.Base64ToImage(array.getString(i), urls[i]);
		}
		
	}
//	public void delete(int key) throws SQLException {
//			String sql = "delete from ��֧����� where key = ?";
//			PreparedStatement pst = database.getpst(sql);
//			pst.setInt(1,key);
//			pst.execute();
//			pst.close();
//		
//	}
//	
//	public void add(String base64,String Class) throws SQLException {
//		String sql = "INSERT INTO ��֧����� VALUES (?,?)";
//		PreparedStatement pst = database.getpst(sql);
//		pst.setString(1, base64);
//		pst.setString(2,Class);
//		pst.execute();
//		pst.close();
//		
//	}
}