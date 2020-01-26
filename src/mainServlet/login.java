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

public class login extends HttpServlet {
      

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		database.connect();
		
		//向前端发送int类型的pass,pass==1时代表密码正确
		int pass = 1;
		
		
		String Class = request.getParameter("Class");
		String pwd = request.getParameter("pwd");
		
		
		String sql = "select * from login where Class = ? and pwd = ?";
		PreparedStatement pst = database.getpst(sql);
		try {
			pst.setString(1, Class);
			pst.setString(2, pwd);
			ResultSet set = pst.executeQuery();
			if(set.next())
				pass = 0;
			else
				pass = 1;
			response.setCharacterEncoding("UTF-8");
			PrintWriter out= response.getWriter();
		    out.write(pass);
			pst.close();
		    set.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		database.disconnect();
		
		
	}

}
