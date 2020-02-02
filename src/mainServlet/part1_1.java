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
//��֧������
public class part1_1 extends HttpServlet {
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
	//��ǰ�˷������ݿ��е�����
	public void show(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String Class =  request.getParameter("Class");
		//׼��sql��䲢ִ�еĵ�ResultSet
		String sql = "select * from ��֧������ where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		//׼����ǰ�˷��͵ĳ�Class���4����Ϣ��������ʼ��
		String basic = "" ;
		String specialty = "";
		String innovation = "";
		String goal = "";
		//�ж����ݿ��������ݣ���û�������һ��������
		if(set.next()) {
			basic = set.getString("basic");
			specialty = set.getString("specialty");
			innovation = set.getString("innovation");
			goal = set.getString("goal");
			System.out.println("b:"+basic);
			System.out.println("s:"+specialty);
			System.out.println("i:"+innovation);
			System.out.println("g:"+goal);
		}
		else
			insert(Class);//�����Ľ��涼��Ҫ�����жϽ�����Ƿ�Ϊ��
		//׼����ǰ�˷��͵�JSON����
		JSONObject write = new JSONObject();	
		write.put("basic", basic);
		write.put("specialty", specialty);
		write.put("innovation", innovation);
		write.put("goal",goal);
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(write.toString()); 
		//�ر����ݿ����
		pst.close();
		set.close();

	}
	//�������ݿ��еİ༶��Ϣ
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//��JSON�л�ȡ�����������Ϣ�������޸Ķ�Ӧ�༶����Ϣ
		String Class = request.getParameter("Class");
		String basic = request.getParameter("basic");
		String specialty = request.getParameter("specialty");
		String innovation = request.getParameter("innovation");
		String goal = request.getParameter("goal");
		System.out.println("Class:"+Class);
		System.out.println("basic:"+basic);
		System.out.println("specialty:"+specialty);
		System.out.println("innovation:"+innovation);
		System.out.println("goal:"+goal);
		//׼��sql��ִ��update
		String sql = "update ��֧������ set basic = ?, specialty = ?, innovation = ?, goal = ?  where Class= ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,basic);
		pst.setString(2,specialty);
		pst.setString(3,innovation);
		pst.setString(4, goal);
		pst.setString(5, Class);
		pst.executeUpdate();
		//�ر����ݿ��йر���
		pst.close();
	}
	//�����ݿ��в�������
	public void insert(String Class) throws SQLException {
		String sql = "insert into ��֧������ values (?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, "");
		pst.setString(2, "");
		pst.setString(3, "");
		pst.setString(4, "");
		pst.setString(5, Class);
		pst.execute();//����executeupdate
		pst.close();
	}
	

}
