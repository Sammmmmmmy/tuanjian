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


//年度团支部选举记录
public class part1_14 extends HttpServlet {
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
		//准备sql语句
		String sql = "select * from 年度团支部选举记录 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		
		JSONArray array = new JSONArray();
		JSONObject meeting;
		String selectDate;
        String selectPlace;
        String joinSelNum;
        String selections;
        String candidate;
        String selTicketNum;
        String selCounter;
        String scrutineer;
        String selResult;
        String demonsWay;
        String demonsDate;
		int size = 0; 
		while(set.next()) {
			meeting= new JSONObject();
			selectDate = set.getString("selectDate");
			selectPlace = set.getString("selectPlace");
			joinSelNum = set.getString("joinSelNum");
			selections = set.getString("selections");
			candidate = set.getString("candidate");
			selTicketNum = set.getString("selTicketNum");
			selCounter = set.getString("selCounter");
			scrutineer = set.getString("scrutineer");
			selResult = set.getString("selResult");
			demonsWay = set.getString("demonsWay");
			demonsDate = set.getString("demonsDate");
			
			meeting.put("selectDate",selectDate);
			meeting.put("selectPlace",selectPlace);
			meeting.put("joinSelNum",joinSelNum);
			meeting.put("selections",selections);
			meeting.put("candidate",candidate);
			meeting.put("selTicketNum",selTicketNum);
			meeting.put("selCounter",selCounter);
			meeting.put("scrutineer",scrutineer);
			meeting.put("selResult",selResult);
			meeting.put("demonsWay",demonsWay);
			meeting.put("demonsDate",demonsDate);
			
			array.add(meeting);
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
		
		//先将数据库内容清除
		clear(Class);
		//再将前端发来的数据写入
		rewrite(Class,array);
	}
	
	public void clear(String Class) throws SQLException {
		String sql = "delete from 年度团支部选举记录 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject meeting;
		String selectDate;
        String selectPlace;
        String joinSelNum;
        String selections;
        String candidate;
        String selTicketNum;
        String selCounter;
        String scrutineer;
        String selResult;
        String demonsWay;
        String demonsDate;
        
        String sql = "insert into 年度团支部选举记录 values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = database.getpst(sql);
		while(count != size) {
			meeting = array.getJSONObject(count);
			selectDate = meeting.getString("selectDate");
			selectPlace = meeting.getString("selectPlace");
			joinSelNum = meeting.getString("joinSelNum");
			selections = meeting.getString("selections");
			candidate = meeting.getString("candidate");
			selTicketNum = meeting.getString("selTicketNum");
			selCounter = meeting.getString("selCounter");
			scrutineer = meeting.getString("scrutineer");
			selResult = meeting.getString("selResult");
			demonsWay = meeting.getString("demonsWay");
			demonsDate = meeting.getString("demonsDate");
			
			pst.setString(1,selectDate);
			pst.setString(2,selectPlace);
			pst.setString(3,joinSelNum);
			pst.setString(4,selections);
			pst.setString(5,candidate);
			pst.setString(6,selTicketNum);
			pst.setString(7,selCounter);
			pst.setString(8,scrutineer);
			pst.setString(9,selResult);
			pst.setString(10,demonsWay);
			pst.setString(11,demonsDate);
			pst.setString(12,Class);
			pst.execute();
		}
		pst.close();
	}
	
	
	public void delete(int key) throws SQLException {
			String sql = "delete from 团支部风采 where key = ?";
			PreparedStatement pst = database.getpst(sql);
			pst.setInt(1,key);
			pst.execute();
			pst.close();
		
	}
	public void add(String base64,String Class) throws SQLException {
		String sql = "INSERT INTO 团支部风采 VALUES (?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, base64);
		pst.setString(2,Class);
		pst.execute();
		pst.close();
	}

}
