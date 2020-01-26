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


//��Ա��չ
public class part1_19 extends HttpServlet {
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
		//׼��sql���
		String sql = "select * from ��Ա��չ where Class = ?";
		//�����ݿ��в�ѯ��Ա��չ��Ϣ
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//�����ݿ����Ϣд��json������
		JSONArray array = new JSONArray();
		JSONObject member;
		String DPerCondition;
        String DMemcon;
        String DInvestigation;
		int size = 0; 
		while(set.next()) {
			member= new JSONObject();
			//�ӽ����set��ȡString
			DPerCondition = set.getString("DPerCondition");
			DMemcon = set.getString("DMemcon");
			DInvestigation = set.getString("DInvestigation");
			//���json
			member.put("DPerCondition",DPerCondition);
			member.put("DMemcon",DMemcon);
			member.put("DInvestigation",DInvestigation);
			//��������
			array.add(member);
			size++;
		}
		//��ǰ�˷�������
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
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException  {
		//��request��ȡClass��array
		String Class = request.getParameter("Class");
		JSONArray array = JSONArray.fromObject(request.getParameter("array"));
		//�Ƚ����ݿ��������
		clear(Class);
		//�ٽ�ǰ�˷���������д��
		rewrite(Class,array);
	}
	
	public void clear(String Class) throws SQLException {
		String sql = "delete from ��Ա��չ where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	
	public void rewrite(String Class,JSONArray array) throws SQLException {
		int size = array.size();
		int count = 0;
		JSONObject meeting;
		String DPerCondition;
        String DMemcon;
        String DInvestigation;
        //׼��sql
        String sql = "insert into �����֧��ѡ�ټ�¼ values(?,?,?,?)";
        PreparedStatement pst = database.getpst(sql);
		while(count != size) {
			meeting = array.getJSONObject(count);
			DPerCondition = meeting.getString("selectDate");
			DMemcon = meeting.getString("selectPlace");
			DInvestigation = meeting.getString("joinSelNum");
			
			pst.setString(1,DPerCondition);
			pst.setString(2,DMemcon);
			pst.setString(3,DInvestigation);
			pst.setString(4,Class);
			pst.execute();
		}
		pst.close();
	}
}