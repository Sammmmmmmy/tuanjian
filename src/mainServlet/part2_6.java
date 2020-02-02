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
//֧��ίԱ��-�����¼
public class part2_6 extends HttpServlet {
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
		String sql = "select * from ֧��ίԱ��-�����¼ where �༶ = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//׼����ǰ�˷��͵�JSONArray
		JSONArray array = new JSONArray();
		JSONObject comJSON;
		int size = 0;
		//׼����Class���8������
		String comDate;
		String comAddr;
		String comHost;
		String comTheme;
		int comParticipantNum;
		String comContent;
		String comCondition;
		String comMSum;
		while(set.next()) {
			comJSON = new JSONObject();
			comDate = set.getString("comDate");
			comAddr = set.getString("comAddr");
			comHost = set.getString("comHost");
			comTheme = set.getString("comTheme");
			comParticipantNum = set.getInt("comParticipantNum");
			comContent = set.getString("comContent");
			comCondition = set.getString("comCondition");
			comMSum = set.getString("comMSum");
			//���ÿһ��comJOSN
			comJSON.put("comDate", comDate);
			comJSON.put("comAddr",comAddr);
			comJSON.put("comHost",comHost);
			comJSON.put("comTheme",comTheme);
			comJSON.put("comParticipantNum",comParticipantNum);
			comJSON.put("comContent",comContent);
			comJSON.put("comCondition",comCondition);
			comJSON.put("comMSum",comMSum);
			//�����array
			array.add(comJSON);
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
		String sql = "delete * from ֧�����-�����¼ where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		pst.execute();
		pst.close();
	}
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject comJSON;
		String comDate;
		String comAddr;
		String comHost;
		String comName;
		String comTheme;
		int comParticipantNum;
		String comContent;
		String comMSum;
		//׼��pst
		String sql = "insert into ֧�����-�����¼ values (?,?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(9,Class);
		while(count!=size) {
			comJSON = array.getJSONObject(count);
			comDate = comJSON.getString("comDate");
			comAddr = comJSON.getString("comAddr");
			comHost = comJSON.getString("comHost");
			comName = comJSON.getString("comName");
			comTheme = comJSON.getString("comTheme");
			comParticipantNum = comJSON.getInt("comParticipantNum");
			comContent = comJSON.getString("comContent");
			comMSum = comJSON.getString("comMSum");
			//���pst��ִ��
			pst.setString(1,comDate);
			pst.setString(2,comAddr);
			pst.setString(3,comHost);
			pst.setString(4,comName);
			pst.setString(5,comTheme);
			pst.setInt(6,comParticipantNum);
			pst.setString(7,comContent);
			pst.setString(8,comMSum);
			pst.execute();
			count++;
		}
		pst.close();
	}

}
