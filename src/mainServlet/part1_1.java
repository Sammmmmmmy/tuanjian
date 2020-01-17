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
		System.out.println("flag:"+flag);
		
		
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
		
		String Class =  request.getParameter("Class");
		System.out.println("Class:"+Class);
		
		
		String sql = "select * from ��֧������ where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		
		
		set.next();
		String basic = set.getString("basic");
		String specialty = set.getString("specialty");
		String innovation = set.getString("innovation");
		String goal = set.getString("goal");
		System.out.println("b:"+basic);
		System.out.println("s:"+specialty);
		System.out.println("i:"+innovation);
		System.out.println("g:"+goal);
		
		
		
		
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
		System.out.println("Class:"+Class);
		System.out.println("basic:"+basic);
		System.out.println("specialty:"+specialty);
		System.out.println("innovation:"+innovation);
		System.out.println("goal:"+goal);
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
