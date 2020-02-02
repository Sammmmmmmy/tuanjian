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
//ע����Ա����
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
		//׼��sql��ִ�еõ������
		String sql = "select * from ע����Ա���� where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//׼����ǰ�˷��͵�array�������Сsize
		JSONArray array = new JSONArray();
		JSONObject memjson;
		int size = 0;
		//׼����Class���8����Ϣ
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
			//�����ݿ��л�ȡ����
			memName = set.getString("memName");
			sex = set.getString("sex");
			nation = set.getString("nation");
			nativePlace = set.getString("nativePlace");
			birthday = set.getString("birthday");
			politicStatus = set.getString("politicStatus");
			joinPartyDate = set.getString("joinPartyDate");
			joinLeaDate = set.getString("joinLeaDate");
			//���JSON
			memjson.put("memName",memName);
			memjson.put("sex",sex);
			memjson.put("nation",nation);
			memjson.put("nativePlace",nativePlace);
			memjson.put("birthday",birthday);
			memjson.put("politicStatus",politicStatus);
			memjson.put("joinPartyDate",joinPartyDate);
			memjson.put("joinLeaDate",joinLeaDate);
			//�����array
			array.add(memjson);
			size++;
		}
		//���͸�ǰ�˵�JSON
		JSONObject write = new JSONObject();
		write.put("size",size);
		write.put("array", array);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(write.toString());
		//�ر����ݿ��йر���
		pst.close();
		set.close();
	}
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException  {
		String Class = request.getParameter("Class");
		JSONArray array = JSONArray.fromObject(request.getParameter("Array"));
		//����ɾ�����ݿ��иð༶���е���Ա��Ϣ
		clear(Class);
		//��д
		rewrite(Class,array);
	}
	public void clear(String Class) throws SQLException {
		String sql = "delete * from ע����Ա���� where Class = ?";
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
		//׼��sql���õ�pst
		String sql = "insert into ע����Ա���� values(?,?,?,?,?,?,?,?,?)";
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
			//���pst
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

