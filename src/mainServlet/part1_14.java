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


//团支部风采
public class part1_14 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		database.connect();
		String flag_string = request.getParameter("flag");
		int flag = Integer.parseInt(flag_string);
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
		String json_string = request.getParameter("JSON");
		JSONObject json = JSONObject.fromObject(json_string);
		String Class = json.getString("Class");
		
		String sql = "select * from 团支部风采 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		
		JSONArray array = new JSONArray();
		JSONObject imagejson;
		int size = 0; 
		String image;
		int key;
		while(set.next()) {
			
			imagejson = new JSONObject();
			image = set.getString("image");
			key = set.getInt("key");
			
			imagejson.put("image",image);
			imagejson.put("key",key);
			array.add(imagejson);
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
		
	
		String json_string = request.getParameter("JSON");
		JSONObject json = JSONObject.fromObject(json_string);
		JSONArray array = json.getJSONArray("Array");
		String Class = json.getString("Class");
		
		int size = array.size();
		int count = 0;
		int operation;
		String base64;
		int key;
		JSONObject imagejson;

		while(count!=size) {
			imagejson = array.getJSONObject(count);
			
			operation = imagejson.getInt("operation");
			if(operation == 0) {
				key = imagejson.getInt("key");
				delete(key);
			}
			else {
				base64 = imagejson.getString("base64");
				add(base64,Class);
			}
			count++;
			
		}
	}
	public void delete(int key) throws SQLException {
			String sql = "delete from 团支部风采 where key = ?";
			PreparedStatement pst = database.getpst(sql);
			pst.setInt(1,key);
			pst.execute();
			pst.close();
		
	}
	public void add(String base64,String Class) throws SQLException {
		String sql = "INSERT INTO 团支部风采 VALUES (?,?)";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, base64);
		pst.setString(2,Class);
		pst.execute();
		pst.close();
		
		
		
		
	}

}
