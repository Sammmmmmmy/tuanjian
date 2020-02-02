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
//注册团员名单
public class part1_2 extends HttpServlet {
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
		//准备sql并执行得到结果集
		String sql = "select * from 注册团员名单 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//准备向前端发送的array和数组大小size
		JSONArray array = new JSONArray();
		JSONObject memjson;
		int size = 0;
		//准备除Class外的8条信息
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
			//从数据库中获取数据
			memName = set.getString("memName");
			sex = set.getString("sex");
			nation = set.getString("nation");
			nativePlace = set.getString("nativePlace");
			birthday = set.getString("birthday");
			politicStatus = set.getString("politicStatus");
			joinPartyDate = set.getString("joinPartyDate");
			joinLeaDate = set.getString("joinLeaDate");
			//填充JSON
			memjson.put("memName",memName);
			memjson.put("sex",sex);
			memjson.put("nation",nation);
			memjson.put("nativePlace",nativePlace);
			memjson.put("birthday",birthday);
			memjson.put("politicStatus",politicStatus);
			memjson.put("joinPartyDate",joinPartyDate);
			memjson.put("joinLeaDate",joinLeaDate);
			//添加至array
			array.add(memjson);
			size++;
		}
		//发送给前端的JSON
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
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException  {
		String Class = request.getParameter("Class");
		JSONArray array = JSONArray.fromObject(request.getParameter("Array"));
		//首先删除数据库中该班级所有的团员信息
		clear(Class);
		//重写
		rewrite(Class,array);
	}
	public void clear(String Class) throws SQLException {
		String sql = "delete * from 注册团员名单 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject memjson;
		String memName;
		String sex;
		String nation;
		String nativePlace;
		String birthday;
		String politicStatus;
		String joinPartyDate;
		String joinLeaDate;
		//准备sql并得到pst
		String sql = "insert into 注册团员名单 values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(9,Class);
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
			//填充pst
			pst.setString(1,memName);
			pst.setString(2,sex);
			pst.setString(3,nation);
			pst.setString(4,nativePlace);
			pst.setString(5,birthday);
			pst.setString(6,politicStatus);
			pst.setString(7,joinPartyDate);
			pst.setString(8,joinLeaDate);
			pst.execute();
			count++;		
			}
		pst.close();
	}
  }

