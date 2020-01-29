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


//团支部获奖情况
public class part1_21 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		database.connect();
		int flag = Integer.parseInt(request.getParameter("Class"));
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
		//准备sql并执行，得到结果集
		String sql = "select * from 团支部获奖情况 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//准备向前端发送给的array和数组大小size
		JSONArray array = new JSONArray();
		JSONObject awardjson = new JSONObject();
		int size = 0; 
		String awardName;
		String awardLevel;
		String awardDate;
		String awardCategory;
		while(set.next()) {
			awardjson = new JSONObject();
			//从数据库获取数据
			awardName = set.getString("awardName");
			awardLevel = set.getString("awardLevel");
			awardDate = set.getString("awardDate");
			awardCategory = set.getString("awardCategory");
			//填充json
			awardjson.put("awardName", awardName);
			awardjson.put("awardLevel",awardLevel);
			awardjson.put("awardDate",awardDate);
			awardjson.put("awardCategory",awardCategory);
			//添加至数组
			array.add(awardjson);
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
		String Class = request.getParameter("Class");
		JSONArray array = JSONArray.fromObject(request.getParameter("array"));
		//清空
		clear(Class);
		//重写
		rewrite(Class,array);
	}
	public void clear(String Class) throws SQLException {
		String sql = "delete * from 团支部获奖情况 values (?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	public void rewrite(String Class,JSONArray array) throws SQLException{
		int size = array.size();
		int count = 0;
		String sql = "insert into 团支部获奖情况 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		//从array中获取获奖json
		JSONObject awardjson;
		String awardName;
		String awardLevel;
		String awardDate;
		String awardCategory;
		while(count!=size) {
			awardjson = array.getJSONObject(count);
			//某一条获奖信息
			awardName = awardjson.getString("awardName");
			awardLevel = awardjson.getString("awardLevel");
			awardDate = awardjson.getString("awardDate");
			awardCategory = awardjson.getString("awardCategory");
			pst.setString(1, awardName);
			pst.setString(2, awardLevel);
			pst.setString(3, awardDate);
			pst.setString(4, awardCategory);
			pst.setString(5, Class);
			pst.execute();
			count++;
		}
		pst.close();
	}
}