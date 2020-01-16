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

public class part1_1 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("����Servlet�ɹ�");
		database.connect();
		String flag_string = request.getParameter("flag");
		int flag = Integer.parseInt(flag_string);
		System.out.println(flag);
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
	//��ǰ�˷������ݿ��е�����
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String json_string = request.getParameter("json");
		JSONObject json = JSONObject.fromObject(json_string);
		
		String Class =  json.getString("Class");
		String sql = "select * from ��֧������ where �༶ = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		set.next();
		
		String basic = set.getString("�����ſ�");
		String specialty = set.getString("֧����ɫ");
		String innovation = set.getString("��������");
		String goal = set.getString("Ŀ������");
		
		//׼����ǰ�˷��͵�json����
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("basic", basic);
	    jsonObject.put("specialty", specialty);
	    jsonObject.put("innovation", innovation);
	    jsonObject.put("goal",goal);
	    
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
	    out.write(jsonObject.toString());
	    
		pst.close();
	    set.close();

	}
	//�������ݿ��еİ༶��Ϣ
	public void write(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//��ȡǰ�˷�������json
		
		//��json�л�ȡClass�������޸Ķ�Ӧ�༶����Ϣ
		String Class = request.getParameter("Class");
		String basic = request.getParameter("basic");
		String specialty = request.getParameter("specialty");
		String innovation = request.getParameter("innovation");
		String goal = request.getParameter("goal");
		System.out.println(Class);
		System.out.println(basic);
		System.out.print(specialty);
		System.out.println(innovation);
		System.out.println(goal);
		String sql = "update ��֧������ set basic = ?, specialty = ?, innovation = ?, goal = ?  where Class= ?";
		
		//׼��sql��䲢ִ��
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,basic);
		pst.setString(2,specialty);
		pst.setString(3,innovation);
		pst.setString(4, goal);
		pst.setString(5, Class);
		pst.executeUpdate();
		
		pst.close();
	}
	

}