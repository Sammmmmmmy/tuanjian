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
		//准备sql语句并执行得到ResultSet
		String sql = "select * from info where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		//准备向前端发送的除Class外的15条填空类信息，并作初始化
		String leaBrCondition = "";
		String leaName = "";
		String college = "";
		String leaBrSect = "";
		String leaBrdeSect = "";
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
			//从数据库获取数据
			leaBrCondition = set.getString("leaBrCondition");
			leaName = set.getString("leaName");
			college = set.getString("college");
			leaBrSect = set.getString("leaBrSect");
			leaBrdeSect = set.getString("leaBrdeSect");
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
		//准备向前端发送的JSON数据
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("leaBrCondition", leaBrCondition);
	    jsonObject.put("leaName", leaName);
	    jsonObject.put("college", college);
	    jsonObject.put("leaBrSect",leaBrSect);
	    jsonObject.put("leadeBrSect",leaBrdeSect);
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
	    //向前端发送
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(jsonObject.toString());    
		pst.close();
		set.close();

	}
	//更新数据库中的班级信息
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String Class = request.getParameter("Class");
		//从JSON中获取填空类信息，用于修改数据库内容
		String leaBrCondition = request.getParameter("leaBrCondition");
		String leaName = request.getParameter("leaName");
		String college = request.getParameter("college");
		String leaBrSect = request.getParameter("leaBrSect");
		String leaBrdeSect = request.getParameter("leaBrdeSect");
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
		//准备sql并执行update
		String sql = "update info set leaBrCondition = ?, leaName = ?, college = ?, leaBrSect = ?, leaBrdeSect = ?,"
				   + "leaBrMem = ? ,leaMemNum = ?,reLeaMemNum = ?,leaBrLogo = ?,chatGroups = ?"
				   + ",campus = ?,contacts = ?,contact = ?,contactInfor = ?, otherContacts = ? where Class= ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,leaBrCondition);
		pst.setString(2,leaName);
		pst.setString(3,college);
		pst.setString(4, leaBrSect);
		pst.setString(5, leaBrdeSect);
		pst.setString(6, leaBrMem);
		pst.setString(7, leaMemNum);
		pst.setString(8,reLeaMemNum);
		pst.setString(9, leaBrLogo);
		pst.setString(10, chatGroups);
		pst.setString(12, campus);
		pst.setString(13, contacts);
		pst.setString(14, contact);
		pst.setString(14, contactInfor);
		pst.setString(15, otherContacts);
		pst.setString(16,Class);
		pst.executeUpdate();
		pst.close();
	}
	//向数据库中插入空语句
	public void insert(String Class) throws SQLException {
		String sql = "insert into 团支部建设 values (?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,"");
		pst.setString(2,"");
		pst.setString(3,"");
		pst.setString(4,"");
		pst.setString(5, Class);
		pst.execute();//还是executeupdate?
		pst.close();
	}

}
