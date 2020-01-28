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

//团支部信息
public class part1_1 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("连接Servlet成功");
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
	//向前端发送数据库中的数据
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		
		String Class =  request.getParameter("Class");
		System.out.println("Class:"+Class);
		
		//准备sql语句
		String sql = "select * from info where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		
		
		String basic = "";
		String specialty = "";
		String innovation = "";
		String goal = "";
		//判断数据库中有无数据，若没有则插入一条空数据
		if(set.next()) {
			basic = set.getString("basic");
			specialty = set.getString("specialty");
			innovation = set.getString("innovation");
			goal = set.getString("goal");
			System.out.println("b:"+basic);
			System.out.println("s:"+specialty);
			System.out.println("i:"+innovation);
			System.out.println("g:"+goal);
		}
		else
			insert(Class);//填空类的界面都需要首先判断结果集是否为空
		//准备向前端发送的json数据
		JSONObject write = new JSONObject();	
		write.put("basic", basic);
		write.put("specialty", specialty);
		write.put("innovation", innovation);
		write.put("goal",goal);
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(write.toString());    
		pst.close();
		set.close();

	}
	//更新数据库中的班级信息
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		
		//从json中获取Class，用于修改对应班级的信息
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
		String sql = "update 团支部建设 set basic = ?, specialty = ?, innovation = ?, goal = ?  where Class= ?";
		

		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,basic);
		pst.setString(2,specialty);
		pst.setString(3,innovation);
		pst.setString(4, goal);
		pst.setString(5, Class);
		pst.executeUpdate();
		
		pst.close();
	}
	//向数据库中插入空语句
	public void insert(String Class) throws SQLException {
		String sql = "insert into 团支部建设 values (?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(5, Class);
		pst.execute();//还是executeupdate
		pst.close();
	}
	

}
