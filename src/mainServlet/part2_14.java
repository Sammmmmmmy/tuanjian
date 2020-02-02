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
//团课-团课记录
public class part2_14 extends HttpServlet {
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
		String sql = "select * from 团课-团课记录 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//准备向前端发送的JSONArray
		JSONArray array = new JSONArray();
		JSONObject LecJSON;
		int size = 0;
		//准备除Class外的5条数据
		String LecDate;
		String LecAddr;
		String LecTheme;
		int LecParticipantNum;
		String LecContent;
		while(set.next()) {
			LecJSON = new JSONObject();
			LecDate = set.getString("LecDate");
			LecAddr = set.getString("LecAddr");
			LecTheme = set.getString("LecTheme");
			LecParticipantNum = set.getInt("LecParticipantNum");
			LecContent = set.getString("LecContent");
			//填充每一个genJOSN
			LecJSON.put("LecJSON", LecJSON);
			LecJSON.put("LecAddr",LecAddr);
			LecJSON.put("LecTheme",LecTheme);
			LecJSON.put("LecParticipantNum",LecParticipantNum);
			LecJSON.put("LecContent",LecContent);
			//添加至array
			array.add(LecJSON);
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
		String sql = "delete * from 团课-团课记录 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		pst.execute();
		pst.close();
	}
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject LecJSON;
		String LecDate;
		String LecAddr;
		String LecTheme;
		int LecParticipantNum;
		String LecContent;
		//准备pst
		String sql = "insert into 团课-团课记录 values (?,?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(6,Class);
		while(count!=size) {
			LecJSON = array.getJSONObject(count);
			LecDate = LecJSON.getString("LecDate");
			LecAddr = LecJSON.getString("LecAddr");
			LecTheme = LecJSON.getString("LecTheme");
			LecParticipantNum = LecJSON.getInt("LecParticipantNum");
			LecContent = LecJSON.getString("LecContent");
			//填充pst并执行
			pst.setString(1,LecDate);
			pst.setString(2,LecAddr);
			pst.setString(3,LecTheme);
			pst.setInt(4,LecParticipantNum);
			pst.setString(5,LecContent);
			pst.execute();
			count++;
		}
		pst.close();
	}

}
