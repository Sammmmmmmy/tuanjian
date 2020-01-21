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
import Model.Base64utilities;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


//��֧�����
public class part1_7 extends HttpServlet {
	
	private String dir = "E:\\�Ž�webͼƬ";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		database.connect();
		String flag_string = request.getParameter("flag");
		int flag = Integer.parseInt(flag_string);
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
		
		String sql = "select * from ��֧����� where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		
		//imagebase64����
		JSONArray array = new JSONArray();
		int size = 0; 
		
		String imageurl;
		String imagebase64;
		
		while(set.next()) {
			//��ȡ���ݿ��е�ͼƬ��ַ
			imageurl = set.getString("imageurl");
			
			//����url��ȡ�����������ͼƬ��base64
			imagebase64 = Base64utilities.ImageToBase64(imageurl);
			
			array.add(imagebase64);
			size++;
		}
		
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
		int size = array.size();
		String[] urls = rewrite(Class,size,array);
		
		
		
		
//		String imagebase64;
//		int id;
//		while(count!=size) {
//			operation = imagejson.getInt("operation");
//			if(operation == 0) {
//				key = imagejson.getInt("key");
//				delete(key);
//			}
//			else {
//				base64 = imagejson.getString("base64");
//				add(base64,Class);
//			}
//			count++;
//		}
	}
	public void clear(String Class) throws SQLException {
		//��ѯ�༶����ͼƬ���ӷ�������ɾ��
		String selectsql = "select * frome ��֧����� where Class = ?";
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
		String sql = "DELETE FROME ��֧����� WHERE Class = ?";
		pst = database.getpst(sql);
		pst.setString(1, Class);
		database.executeUpdate(sql);
		pst.close();

	}
	//����ͼƬ�ĵ�ַ��д�����ݿ�
	//д�������ָ��dir��
	public String[] rewrite(String Class,int size,JSONArray array) throws SQLException {

		//��ͼƬ��������
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
		String date = df.format(new Date());
		String[] urls = new String[size];
		for(int i = 0;i<size;i++)
			urls[i] = dir+date+i+".jpg";
		
		//׼��sql���
		String sql = "INSERT INTO ��֧����� VALUES (?,?)";
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
		
		
		return urls;
		
	}
	public void delete(int key) throws SQLException {
			String sql = "delete from ��֧����� where key = ?";
			PreparedStatement pst = database.getpst(sql);
			pst.setInt(1,key);
			pst.execute();
			pst.close();
		
	}
	
	public void add(String base64,String Class) throws SQLException {
		String sql = "INSERT INTO ��֧����� VALUES (?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, base64);
		pst.setString(2,Class);
		pst.execute();
		pst.close();
		
	}

}
