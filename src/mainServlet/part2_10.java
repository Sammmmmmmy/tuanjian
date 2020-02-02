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

//�����
//��Ա�����������Ż����¼
public class part2_10 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("����Servlet�ɹ�");
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
	
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String Class = request.getParameter("Class");
		//�����ݿ��ѯ��Ϣ
		String sql = "select * from ��Ա�����������Ż����¼ where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		//׼����Class���1����Ϣ
		String eduAndEva = "";
		if(set.next()) {
			eduAndEva = set.getString("may");
		}
		else
			insert(Class);
		//��ǰ�˷��ͷ�װ�õ�json����
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("eduAndEva",eduAndEva);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
	    out.write(jsonObject.toString());	
	    //�ر����ݿ��йر���
		pst.close();
	    set.close();

	}
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//��ȡǰ�˷�����������
		String Class = request.getParameter("Class");
		String eduAndEva = request.getParameter("eduAndEva");
		//׼��sql���
		String sql = "update ��Ա�����������Ż����¼ set eduAndEva = ?  where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,eduAndEva);
		pst.setString(2,Class);
		pst.executeUpdate();
		pst.close();
	}
	public void insert(String Class) throws SQLException {
		String sql = "insert into ��Ա�����������Ż����¼ values(?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,"");
		pst.setString(2, Class);
		pst.execute();
		pst.close();
	}

}
