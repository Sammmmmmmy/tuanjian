package mainServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DB.database;
import net.sf.json.JSONObject;


//����


public class part1_6 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("����Servlet�ɹ�");
		database.connect();
		int flag = Integer.parseInt(request.getParameter("flag"));
		if(flag == 0) {
			try {
				show(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(flag == 1) {
			try {
				update(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    database.disconnect();
	}
	
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		//��ȡjson�еİ༶�����ڲ�ѯ�ð༶��������Ϣ
		String Class = request.getParameter("Class");
		//�����ݿ��ѯ��Ϣ
		String sql = "select * from ���� where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		//׼����ǰ�˷��͵���Ϣ
		String otherSet = "";
		if(set.next()) {
			otherSet = set.getString("may");
		}
		else
			insert(Class);
		//��ǰ�˷��ͷ�װ�õ�json����
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("otherSet",otherSet);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
	    out.write(jsonObject.toString());	
	    
	    //�ر����ݿ��йر���
		pst.close();
	    set.close();

	}
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//��ȡǰ�˷�����������
		String Class = request.getParameter("Class");
		String otherSet = request.getParameter("otherSet");
		//׼��sql���
		String sql = "update ���� set otherSet = ?  where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,otherSet);
		pst.setString(2,Class);
		pst.executeUpdate();
		pst.close();
	}
	public void insert(String Class) throws SQLException {
		String sql = "insert into ���� values(?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(2, Class);
		pst.execute();
		pst.close();
	}

}
