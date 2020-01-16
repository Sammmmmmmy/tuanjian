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





public class part1_6 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("����Servlet�ɹ�");
		database.connect();
		//��ȡǰ�˴�����flagֵ
		//flag == 0��ʾ��ѯ����
		//flag == 1��ʾ�޸�����
		String flag_string = request.getParameter("flag");
		int flag = Integer.parseInt(flag_string);
		
		
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
				write(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    database.disconnect();
	}
	
	
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		//��ȡǰ�˷��͵�json
		String json_string = request.getParameter("json");
		JSONObject json = JSONObject.fromObject(json_string);
		
		//��ȡjson�еİ༶�����ڲ�ѯ�ð༶��������Ϣ
		String Class =  json.getString("Class");
		String sql = "select * from ���� where �༶ = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		set.next();
		
		String otherSet = set.getString("may");

		
		//��ǰ�˷��ͷ�װ�õ�json����
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("otherSet",otherSet);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
	    out.write(jsonObject.toString());	
	    
	    
		pst.close();
	    set.close();

	}
	public void write(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//��ȡǰ�˷��͵�json
		String json_string = request.getParameter("json");
		JSONObject json = JSONObject.fromObject(json_string);
		String Class = json.getString("Class");
		String otherSet = json.getString("otherSet");
		
		String sql = "update ���� set  otherSet = ?  where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,otherSet);
		pst.setString(2,Class);
		pst.executeUpdate();
		pst.close();
	}
	

}
