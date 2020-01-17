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
import Model.Award;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


//团支部获奖情况
public class part1_21 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		database.connect();
		String flag_string = request.getParameter("flag");
		int flag = Integer.parseInt(flag_string);
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
		String json_string = request.getParameter("JSON");
		JSONObject json = JSONObject.fromObject(json_string);
		String Class = json.getString("Class");
		
		String sql = "select * from 团支部获奖情况 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		
		JSONArray array = new JSONArray();
		JSONObject awardjson = new JSONObject();
		int size = 0; 
		String awardName;
		String awardLevel;
		String awardDate;
		String awardCategory;
		while(set.next()) {
			
			awardjson = new JSONObject();
			awardName = set.getString("awardName");
			awardLevel = set.getString("awardLevel");
			awardDate = set.getString("awardDate");
			awardCategory = set.getString("awardCategory");

			awardjson.put("awardName", awardName);
			awardjson.put("awardLevel",awardLevel);
			awardjson.put("awardDate",awardDate);
			awardjson.put("awardCategory",awardCategory);
			
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
		
	
		String json_string = request.getParameter("JSON");
		JSONObject json = JSONObject.fromObject(json_string);
		JSONArray array = json.getJSONArray("Array");
		String Class = json.getString("Class");
		
		String sql = "delete * from 团支部获奖情况 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
		
		int size = array.size();
		int count = 0;
		System.out.println(size);
		
		JSONObject awardjson;
		String awardName;
		String awardLevel;
		String awardDate;
		String awardCategory;
		Award a;
		while(count!=size) {
			awardjson = array.getJSONObject(count);
			
			awardName = awardjson.getString("awardName");
			awardLevel = awardjson.getString("awardLevel");
			awardDate = awardjson.getString("awardDate");
			awardCategory = awardjson.getString("awardCategory");
				
			a = new Award(awardName,awardLevel,awardDate,awardCategory);
			saveaward(a);
			count++;
			
		}
	}
	public void saveaward(Award a) throws SQLException {
			String sql = "INSERT INTO 注册团员名单 VALUES (?,?,?,?,?)";
			PreparedStatement pst = database.getpst(sql);
			pst.setString(1,a.getAwardName());
			pst.setString(2,a.getAwardLevel());
			pst.setString(3,a.getAwardDate());
			pst.setString(4,a.getAwardCategory());
			pst.setString(5, a.get_Class());
			pst.execute();
			pst.close();
		
	}

}
