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

//填空类
//团员教育评议评优或处理记录
public class part2_10 extends HttpServlet {
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
	
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String Class = request.getParameter("Class");
		//从数据库查询信息
		String sql = "select * from 团员教育评议评优或处理记录 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		//准备除Class外的1条信息
		String eduAndEva = "";
		if(set.next()) {
			eduAndEva = set.getString("may");
		}
		else
			insert(Class);
		//向前端发送封装好的json对象
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("eduAndEva",eduAndEva);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
	    out.write(jsonObject.toString());	
	    //关闭数据库有关变量
		pst.close();
	    set.close();

	}
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//获取前端发送来的数据
		String Class = request.getParameter("Class");
		String eduAndEva = request.getParameter("eduAndEva");
		//准备sql语句
		String sql = "update 团员教育评议评优或处理记录 set eduAndEva = ?  where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,eduAndEva);
		pst.setString(2,Class);
		pst.executeUpdate();
		pst.close();
	}
	public void insert(String Class) throws SQLException {
		String sql = "insert into 团员教育评议评优或处理记录 values(?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,"");
		pst.setString(2, Class);
		pst.execute();
		pst.close();
	}

}
