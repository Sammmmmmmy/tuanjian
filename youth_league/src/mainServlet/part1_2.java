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
import Model.LeaMember;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class part1_2 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		database.connect();
		String flag_string = request.getParameter("flag");
		int flag = Integer.parseInt(flag_string);
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
		String json_string = request.getParameter("JSON");
		JSONObject json = JSONObject.fromObject(json_string);
		String Class = json.getString("Class");
		String sql = "select * from 注册团员名单 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		
		JSONArray array = new JSONArray();
		JSONObject memjson = new JSONObject();
		int size = 0;
		String memName;
		String sex;
		String nation;
		String nativePlace;
		String birthday;
		String politicStatus;
		String joinPartyDate;
		String joinLeaDate;
		while(set.next()) {
			memjson = new JSONObject();
			memName = set.getString("memName");
			sex = set.getString("sex");
			nation = set.getString("nation");
			nativePlace = set.getString("nativePlace");
			birthday = set.getString("birthday");
			politicStatus = set.getString("politicStatus");
			joinPartyDate = set.getString("joinPartyDate");
			joinLeaDate = set.getString("joinLeaDate");
			
			memjson.put("memName",memName);
			memjson.put("sex",sex);
			memjson.put("nation",nation);
			memjson.put("nativePlace",nativePlace);
			memjson.put("birthday",birthday);
			memjson.put("politicStatus",politicStatus);
			memjson.put("joinPartyDate",joinPartyDate);
			memjson.put("joinLeaDate",joinLeaDate);
			
			array.add(memjson);
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
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException  {
		

		
		String json_string = request.getParameter("JSON");
		JSONObject json = JSONObject.fromObject(json_string);
		JSONArray array = json.getJSONArray("Array");
		String Class = json.getString("Class");
		
		String sql = "delete * from 注册团员名单 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
		
		int size = array.size();
		int count = 0;
		System.out.println(size);
		
		JSONObject memjson;
		String memName;
		String sex;
		String nation;
		String nativePlace;
		String birthday;
		String politicStatus;
		String joinPartyDate;
		String joinLeaDate;
		LeaMember m;
		
		while(count!=size) {
			memjson = array.getJSONObject(count);
			memName = memjson.getString("memName");
			sex = memjson.getString("sex");
			nation = memjson.getString("nation");
			nativePlace = memjson.getString("nativePlace");
			birthday = memjson.getString("birthday");
			politicStatus = memjson.getString("politicStatus");
			joinPartyDate = memjson.getString("joinPartyDate");
			joinLeaDate = memjson.getString("joinLeaDate");
			
			m = new LeaMember(memName, sex, nation,  nativePlace,birthday, politicStatus,joinPartyDate, joinLeaDate, Class);
			savemem(m);
			count++;
			
		}
	}
	public void savemem(LeaMember m) throws SQLException {
			String sql = "INSERT INTO 注册团员名单 VALUES (?,?,?,?,?,?,?,?,?)";
			PreparedStatement pst = database.getpst(sql);
			pst.setString(1,m.getMemName());
			pst.setString(2,m.getSex());
			pst.setString(3,m.getNation());
			pst.setString(4,m.getNativePlace());
			pst.setString(5,m.getBirthday());
			pst.setString(6,m.getPoliticStatus());
			pst.setString(7,m.getJoinPartyDate());
			pst.setString(8,m.getJoinLeaDate());
			pst.setString(9,m.get_Class());
			pst.execute();
			pst.close();
		}
  }

