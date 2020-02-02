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

//�ظ���
//��С���-�����¼
public class part2_8 extends HttpServlet {
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
				e1.printStackTrace();
			}
		else
			try {
				update(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		database.disconnect();
	}
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String Class = request.getParameter("Class");
		String sql = "select * from ��С���-�����¼ where �༶ = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//׼����ǰ�˷��͵�JSONArray
		JSONArray array = new JSONArray();
		JSONObject grpJSON;
		int size = 0;
		//׼����Class���8������
		String grpDate;
		String grpAddr;
		String grpHost;
		String grpTheme;
		int grpParticipantNum;
		String grpContent;
		String grpCondition;
		String grpMSum;
		while(set.next()) {
			grpJSON = new JSONObject();
			grpDate = set.getString("grpDate");
			grpAddr = set.getString("grpAddr");
			grpHost = set.getString("grpHost");
			grpTheme = set.getString("grpTheme");
			grpParticipantNum = set.getInt("grpParticipantNum");
			grpContent = set.getString("grpContent");
			grpCondition = set.getString("grpCondition");
			grpMSum = set.getString("grpMSum");
			//���ÿһ��grpJOSN
			grpJSON.put("grpDate", grpDate);
			grpJSON.put("grpAddr",grpAddr);
			grpJSON.put("grpHost",grpHost);
			grpJSON.put("grpTheme",grpTheme);
			grpJSON.put("grpParticipantNum",grpParticipantNum);
			grpJSON.put("grpContent",grpContent);
			grpJSON.put("grpCondition",grpCondition);
			grpJSON.put("grpMSum",grpMSum);
			//�����array
			array.add(grpJSON);
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
		//���
		clear(Class);
		//��д
		rewrite(Class,array);
	}
	
	public void clear(String Class) throws SQLException {
		String sql = "delete * from ��С���-�����¼ where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		pst.execute();
		pst.close();
	}
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject grpJSON;
		String grpDate;
		String grpAddr;
		String grpHost;
		String grpName;
		String grpTheme;
		int grpParticipantNum;
		String grpContent;
		String grpMSum;
		//׼��pst
		String sql = "insert into ��С���-�����¼ values (?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(9,Class);
		while(count!=size) {
			grpJSON = array.getJSONObject(count);
			grpDate = grpJSON.getString("grpDate");
			grpAddr = grpJSON.getString("grpAddr");
			grpHost = grpJSON.getString("grpHost");
			grpName = grpJSON.getString("grpName");
			grpTheme = grpJSON.getString("grpTheme");
			grpParticipantNum = grpJSON.getInt("grpParticipantNum");
			grpContent = grpJSON.getString("grpContent");
			grpMSum = grpJSON.getString("grpMSum");
			//���pst��ִ��
			pst.setString(1,grpDate);
			pst.setString(2,grpAddr);
			pst.setString(3,grpHost);
			pst.setString(4,grpName);
			pst.setString(5,grpTheme);
			pst.setInt(6,grpParticipantNum);
			pst.setString(7,grpContent);
			pst.setString(8,grpMSum);
			pst.execute();
			count++;
		}
		pst.close();
	}

}
