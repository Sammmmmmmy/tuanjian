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

//主题团日活动
public class part1_23 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		database.connect();
		int flag = Integer.parseInt(request.getParameter("flag"));
		if(flag == 0)
			try {
				show(request,response);
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
		String sql = "select * from 支部团员奖惩记录 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//准备向前端发送的JSONArray
		JSONArray array = new JSONArray();
		JSONObject activityjson;
		int size = 0;
		String ActivTheme;
		String ActivDate;
		String ActivOrganizer;
		String ActivPaticipant;
		String ActivContent;
		while(set.next()) {
			activityjson = new JSONObject();
			ActivTheme = set.getString("stuMPName");
			ActivDate = set.getString("MPContent");
			ActivOrganizer = set.getString("MPlevel");
			ActivPaticipant = set.getString("MPDate");
			ActivContent = set.getString("MPCategory");
			activityjson.put("ActivTheme", ActivTheme);
			activityjson.put("ActivDate",ActivDate);
			activityjson.put("ActivOrganizer",ActivOrganizer);
			activityjson.put("ActivPaticipant",ActivPaticipant);
			activityjson.put("ActivContent",ActivContent);
			//添加至array
			array.add(activityjson);
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
		JSONArray array = JSONArray.fromObject(request.getParameter("array"));
		String Class = request.getParameter("Class");
		//清空
		clear(Class);
		//重写
		rewrite(Class,array);
	}
	public void clear(String Class) throws SQLException {
		String sql = "delete * from where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		pst.execute();
		pst.close();
	}
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject activityjson;
		String ActivTheme;
		String ActivDate;
		String ActivOrganizer;
		String ActivPaticipant;
		String ActivContent;
		//准备pst
		String sql = "insert into 主题团日活动 values (?,?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		while(count!=size) {
			activityjson = array.getJSONObject(count);
			ActivTheme = activityjson.getString("stuMPName");
			ActivDate = activityjson.getString("MPContent");
			ActivOrganizer = activityjson.getString("MPlevel");
			ActivPaticipant = activityjson.getString("MPDate");
			ActivContent = activityjson.getString("MPCategory");
			//填充pst并执行
			pst.setString(1,ActivTheme);
			pst.setString(2,ActivDate);
			pst.setString(3,ActivOrganizer);
			pst.setString(4,ActivPaticipant);
			pst.setString(5,ActivContent);
			pst.setString(6,Class);
			pst.execute();
			count++;
		}
		pst.close();
	}

}
