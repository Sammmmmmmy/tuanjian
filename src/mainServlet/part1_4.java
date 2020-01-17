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


//工作事宜一月至十二月


public class part1_4 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("连接Servlet成功");
		database.connect();
		//获取前端传来的flag值
		//flag == 0表示查询数据
		//flag == 1表示修改数据
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
		//获取前端发送的json
		String json_string = request.getParameter("json");
		JSONObject json = JSONObject.fromObject(json_string);
		
		//获取json中的班级，用于查询该班级的所有信息
		String Class =  json.getString("Class");
		String sql = "select * from 团支部建设 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		set.next();
		
		String may = set.getString("may");
		String jun = set.getString("jun");
		String jul = set.getString("jul");
		String aug = set.getString("aug");
		String sep = set.getString("sep");
		String oct = set.getString("oct");
		String nov = set.getString("nov");
		String dec = set.getString("dec");
		String jan = set.getString("jan");
		String feb = set.getString("feb");
		String mar = set.getString("mar");
		String Apr = set.getString("Apr");
		
		//向前端发送封装好的json对象
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("may", may);
	    jsonObject.put("jun", jun);
	    jsonObject.put("jul", jul);
	    jsonObject.put("aug",aug);
		jsonObject.put("sep", sep);
	    jsonObject.put("oct", oct);
	    jsonObject.put("nov", nov);
	    jsonObject.put("dec",dec);
		jsonObject.put("jan", jan);
	    jsonObject.put("feb", feb);
	    jsonObject.put("mar", mar);
	    jsonObject.put("Apr",Apr);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
	    out.write(jsonObject.toString());	
	    
	    
		pst.close();
	    set.close();

	}
	public void write(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//获取前端发送的json
		String json_string = request.getParameter("json");
		JSONObject json = JSONObject.fromObject(json_string);
		String Class = json.getString("Class");
		String may = json.getString("may");
		String jun = json.getString("jun");
		String jul = json.getString("jul");
		String aug = json.getString("aug");
		String sep =  json.getString("sep");
		String oct = json.getString("oct");
		String nov = json.getString("nov");
		String dec = json.getString("dec");
		String jan =  json.getString("jan");
		String feb = json.getString("feb");
		String mar = json.getString("mar");
		String Apr = json.getString("Apr");
		String sql = "update 团支部建设 set may = ?, jun = ?, jul = ?, aug = ?, sep = ?, oct = ?, nov = ?, dec = ?, jan = ?, feb = ?, mar = ?, Apr = ?  where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,may);
		pst.setString(2,jun);
		pst.setString(3,jul);
		pst.setString(4,aug);
		pst.setString(5,sep);
		pst.setString(6,oct);
		pst.setString(7,nov);
		pst.setString(8,dec);
		pst.setString(9,jan);
		pst.setString(10,feb);
		pst.setString(11,mar);
		pst.setString(12,Apr);
		pst.setString(13, Class);
		pst.executeUpdate();
		pst.close();
	}
	

}
