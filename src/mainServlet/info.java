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
public class info extends HttpServlet {
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
		
		
		String leaBrCondition = "";
		String leaName = "";
		String college = "";
		String leaBrSect = "";
		String leaBrMem = "";
		String leaMemNum = "";
		String reLeaMemNum  = "";
		String leaBrLogo = "";
		String chatGroups = "";
		String campus = "";
		String contacts = "";
		String contact = "";
		String contactInfor = "";
		String otherContacts= "";
		//判断数据库中有无数据，若没有则插入一条空数据
		if(set.next()) {
			leaBrCondition = set.getString("leaBrCondition");
			leaName = set.getString("leaName");
			college = set.getString("college");
			leaBrSect = set.getString("leaBrSect");
			leaBrMem = set.getString("leaBrMem");
			leaMemNum = set.getInt("leaMemNum")+"";
			reLeaMemNum = set.getInt("reLeaMemNum")+"";
			leaBrLogo = set.getString("leaBrLogo");
			chatGroups = set.getString("chatGroups");
			campus = set.getString("campus");
			contacts = set.getString("contacts");
			contact = set.getString("contact");
			contactInfor = set.getString("contactInfor");
			otherContacts = set.getString("otherContacts");
		}
		else
			insert(Class);
		//准备向前端发送的json数据
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("leaBrCondition", leaBrCondition);
	    jsonObject.put("leaName", leaName);
	    jsonObject.put("college", college);
	    jsonObject.put("leaBrSect",leaBrSect);
	    jsonObject.put("leaBrMem", leaBrMem);
	    jsonObject.put("leaMemNum", leaMemNum);
	    jsonObject.put("reLeaMemNum", reLeaMemNum);
	    jsonObject.put("leaBrLogo",leaBrLogo);
	    jsonObject.put("chatGroups", chatGroups);
	    jsonObject.put("campus", campus);
	    jsonObject.put("contacts", contacts);
	    jsonObject.put("contact",contact);
	    jsonObject.put("contactInfor", contactInfor);
	    jsonObject.put("otherContacts",otherContacts);
	    
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(jsonObject.toString());    
		pst.close();
		set.close();

	}
	//更新数据库中的班级信息
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		
		//从json中获取Class，用于修改对应班级的信息
		String leaBrCondition = request.getParameter("leaBrCondition");
		String leaName = request.getParameter("leaName");
		String college = request.getParameter("college");
		String leaBrSect = request.getParameter("college");
		String leaBrMem = request.getParameter("leaBrMem");
		String leaMemNum = request.getParameter("leaMemNum");
		String reLeaMemNum  = request.getParameter("reLeaMemNum");
		String leaBrLogo = request.getParameter("leaBrLogo");
		String chatGroups = request.getParameter("chatGroups");
		String campus = request.getParameter("campus");
		String contacts = request.getParameter("contacts");
		String contact = request.getParameter("contact");
		String contactInfor = request.getParameter("contactInfor");
		String otherContacts= request.getParameter("otherContacts");
	
		String sql = "update info set leaBrCondition = ?, leaName = ?, college = ?, leaBrSect = ?,leaBrMem = ?"
				   + ",leaMemNum = ?,reLeaMemNum = ?,leaBrLogo = ?,chatGroups = ?"
				   + ",campus = ?,contacts = ?,contact = ?,contactInfor?, otherContacts = ? where Class= ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,leaBrCondition);
		pst.setString(2,leaName);
		pst.setString(3,college);
		pst.setString(4, leaBrSect);
		pst.setString(5, leaBrMem);
		pst.setString(6, leaMemNum);
		pst.setString(7,reLeaMemNum);
		pst.setString(8, leaBrLogo);
		pst.setString(9, chatGroups);
		pst.setString(10, campus);
		pst.setString(11, contacts);
		pst.setString(12, contact);
		pst.setString(13, contactInfor);
		pst.setString(14, otherContacts);
		pst.executeUpdate();
		pst.close();
	}
	//向数据库中插入空语句
	public void insert(String Class) throws SQLException {
		String sql = "insert into 团支部建设 values (?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(5, Class);
		pst.execute();//还是executeupdate?
		pst.close();
	}

}
