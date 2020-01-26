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


//��������һ����ʮ����


public class part1_4 extends HttpServlet {
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
	
		//��ȡjson�еİ༶�����ڲ�ѯ�ð༶��������Ϣ
		String Class =  request.getParameter("Class");
		String sql = "select * from �������⹤��ʱ��������Ч��Ŀ�� where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		ResultSet set = pst.executeQuery();
		
		//׼�����ݿ��е���Ϣ
		String may = "";
		String jun = "";
		String jul = "";
		String aug = "";
		String sep = "";
		String oct = "";
		String nov = "";
		String dec = "";
		String jan = "";
		String feb = "";
		String mar = "";
		String Apr = "";
		if(set.next()) {
			may = set.getString("may");
			jun = set.getString("jun");
			jul = set.getString("jul");
			aug = set.getString("aug");
			sep = set.getString("sep");
			oct = set.getString("oct");
			nov = set.getString("nov");
			dec = set.getString("dec");
			jan = set.getString("jan");
			feb = set.getString("feb");
			mar = set.getString("mar");
			Apr = set.getString("Apr");
		}
		else
			insert(Class);
		//��ǰ�˷��ͷ�װ�õ�json����
		JSONObject jsonObject = new JSONObject();	
		jsonObject.put("may", may);
	    jsonObject.put("jun", jun);
	    jsonObject.put("jul", jul);
	    jsonObject.put("aug",aug);
		jsonObject.put("sep", sep);
	    jsonObject.put("oct", oct);
	    jsonObject.put("nov", nov);
	    jsonObject.put("dec",dec);
		jsonObject.put("jan", jan);
	    jsonObject.put("feb", feb);
	    jsonObject.put("mar", mar);
	    jsonObject.put("Apr",Apr);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
	    out.write(jsonObject.toString());
	    //�ر����ݿ����
		pst.close();
	    set.close();

	}
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		//����ǰ�˷���������Ϣ�������ݿ�����
		String Class = request.getParameter("Class");
		String may = request.getParameter("may");
		String jun = request.getParameter("jun");
		String jul = request.getParameter("jul");
		String aug = request.getParameter("aug");
		String sep = request.getParameter("sep");
		String oct = request.getParameter("oct");
		String nov = request.getParameter("nov");
		String dec = request.getParameter("dec");
		String jan = request.getParameter("jan");
		String feb = request.getParameter("feb");
		String mar = request.getParameter("mar");
		String Apr = request.getParameter("Apr");
		String sql = "update ��֧������ set may = ?, jun = ?, jul = ?, aug = ?, sep = ?,"
				   + " oct = ?, nov = ?, dec = ?, jan = ?, feb = ?, mar = ?, Apr = ?  where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,may);
		pst.setString(2,jun);
		pst.setString(3,jul);
		pst.setString(4,aug);
		pst.setString(5,sep);
		pst.setString(6,oct);
		pst.setString(7,nov);
		pst.setString(8,dec);
		pst.setString(9,jan);
		pst.setString(10,feb);
		pst.setString(11,mar);
		pst.setString(12,Apr);
		pst.setString(13, Class);
		pst.executeUpdate();//executeupdate()
		pst.close();
	}
	//�����ݿ�û�иð༶���ݵ�����²���һ�������
	public void insert(String Class) throws SQLException {
		String sql = "insert into �������⹤��ʱ��������Ч��Ŀ�� values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(13,Class);
		pst.execute();
		pst.close();
	}
}
