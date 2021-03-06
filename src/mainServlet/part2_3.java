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
//支部大会-会议记录
public class part2_3 extends HttpServlet {
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
		String sql = "select * from 支部大会-会议记录 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//准备向前端发送的JSONArray
		JSONArray array = new JSONArray();
		JSONObject genJSON;
		int size = 0;
		//准备除Class外的8条数据
		String genDate;
		String genAddr;
		String genHost;
		String genTheme;
		int genParticipantNum;
		String genContent;
		String genCondition;
		String genMSum;
		while(set.next()) {
			genJSON = new JSONObject();
			genDate = set.getString("genDate");
			genAddr = set.getString("genAddr");
			genHost = set.getString("genHost");
			genTheme = set.getString("genTheme");
			genParticipantNum = set.getInt("genParticipantNum");
			genContent = set.getString("genContent");
			genCondition = set.getString("genCondition");
			genMSum = set.getString("genMSum");
			//填充每一个genJOSN
			genJSON.put("genDate", genDate);
			genJSON.put("genAddr",genAddr);
			genJSON.put("genHost",genHost);
			genJSON.put("genTheme",genTheme);
			genJSON.put("genParticipantNum",genParticipantNum);
			genJSON.put("genContent",genContent);
			genJSON.put("genCondition",genCondition);
			genJSON.put("genMSum",genMSum);
			//添加至array
			array.add(genJSON);
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
		String sql = "delete * from 支部大会-会议记录 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		pst.execute();
		pst.close();
	}
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject genJSON;
		String genDate;
		String genAddr;
		String genHost;
		String genCondition;
		String genTheme;
		int genParticipantNum;
		String genContent;
		String genMSum;
		//准备pst
		String sql = "insert into 支部大会-会议记录 values (?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(9,Class);
		while(count!=size) {
			genJSON = array.getJSONObject(count);
			genDate = genJSON.getString("genDate");
			genAddr = genJSON.getString("genAddr");
			genHost = genJSON.getString("genHost");
			genCondition = genJSON.getString("genCondition");
			genTheme = genJSON.getString("genTheme");
			genParticipantNum = genJSON.getInt("genParticipantNum");
			genContent = genJSON.getString("genContent");
			genMSum = genJSON.getString("genMSum");
			//填充pst并执行
			pst.setString(1,genDate);
			pst.setString(2,genAddr);
			pst.setString(3,genHost);
			pst.setString(4,genCondition);
			pst.setString(5,genTheme);
			pst.setInt(6,genParticipantNum);
			pst.setString(7,genContent);
			pst.setString(8,genMSum);
			pst.execute();
			count++;
		}
		pst.close();
	}

}
