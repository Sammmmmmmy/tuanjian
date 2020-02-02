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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//重复类
//团员发展
public class part1_19 extends HttpServlet {
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
		//准备sql语句并执行得到ResultSet
		String sql = "select * from 团员发展 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//准备JSONArray和JSON
		JSONArray array = new JSONArray();
		JSONObject member;
		int size = 0; 
		//准备除Class外的3条信息
		String DPerCondition;
        String DMemcon;
        String DInvestigation;
		while(set.next()) {
			member= new JSONObject();
			DPerCondition = set.getString("DPerCondition");
			DMemcon = set.getString("DMemcon");
			DInvestigation = set.getString("DInvestigation");
			//填充json
			member.put("DPerCondition",DPerCondition);
			member.put("DMemcon",DMemcon);
			member.put("DInvestigation",DInvestigation);
			//插入数组
			array.add(member);
			size++;
		}
		//向前端发送数据
		JSONObject write = new JSONObject();
		write.put("size",size);
		write.put("array", array);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(write.toString());
		//关闭数据库有关变量
		pst.close();
		set.close();
		
		
	}
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException  {
		//从request获取Class和array
		String Class = request.getParameter("Class");
		JSONArray array = JSONArray.fromObject(request.getParameter("array"));
		//先将数据库内容清除
		clear(Class);
		//再将前端发来的数据写入
		rewrite(Class,array);
	}
	
	public void clear(String Class) throws SQLException {
		String sql = "delete from 团员发展 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject meeting;
		String DPerCondition;
        String DMemcon;
        String DInvestigation;
        //准备sql
        String sql = "insert into 年度团支部选举记录 values(?,?,?,?)";
        PreparedStatement pst = database.getpst(sql);
		pst.setString(4,Class);
		while(count != size) {
			meeting = array.getJSONObject(count);
			DPerCondition = meeting.getString("selectDate");
			DMemcon = meeting.getString("selectPlace");
			DInvestigation = meeting.getString("joinSelNum");
			//填充pst
			pst.setString(1,DPerCondition);
			pst.setString(2,DMemcon);
			pst.setString(3,DInvestigation);
			pst.execute();
		}
		pst.close();
	}
}