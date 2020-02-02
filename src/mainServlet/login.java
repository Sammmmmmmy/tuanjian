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

public class login extends HttpServlet {
      

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		database.connect();
		//��ǰ�˷���int���͵�pass,pass==1ʱ����������ȷ
		int pass = 1;
		//ǰ������
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		System.out.println(user);
		System.out.println(password);
		//ʹ��ѡ����䣬���set��ֵ����pass
		String sql = "select * from login where Class = ? and pwd = ?";
		PreparedStatement pst = database.getpst(sql);
		try {
			pst.setString(1, user);
			pst.setString(2, password);
			ResultSet set = pst.executeQuery();
			if(set.next())
				pass = 0;
			else
				pass = 1;
			System.out.println(pass);
			JSONObject write = new JSONObject();	
			write.put("pass",pass);
		    response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out= response.getWriter();
			out.write(write.toString()); 
			pst.close();
		    set.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		database.disconnect();
	}

}
