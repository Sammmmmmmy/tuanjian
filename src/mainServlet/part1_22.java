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


//团员奖惩记录
public class part1_22 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		database.connect();
		String flag_string = request.getParameter("flag");
		int flag = Integer.parseInt(flag_string);
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
		String sql = "select * from 支部团员奖惩记录 where 班级 = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//准备JOSNarray
		JSONArray array = new JSONArray();
		JSONObject mandpjson;
		int size = 0;
		String stuMPName;
		String MPContent;
		String MPlevel;
		String MPDate;
		String MPCategory;
		while(set.next()) {
			mandpjson = new JSONObject();
			stuMPName = set.getString("stuMPName");
			MPContent = set.getString("MPContent");
			MPlevel = set.getString("MPlevel");
			MPDate = set.getString("MPDate");
			MPCategory = set.getString("MPCategory");
			mandpjson.put("stuMPName", stuMPName);
			mandpjson.put("MPContent",MPContent);
			mandpjson.put("MPlevel",MPlevel);
			mandpjson.put("MPDate",MPDate);
			mandpjson.put("MPCategory",MPCategory);
			//添加至数组
			array.add(mandpjson);
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
		String Class = request.getString("Class");
		//清空
		clear(Class);
		//重写
		rewrite(Class,array);
		
	}
	public void savemandp(MandP m) throws SQLException {
			String sql = "INSERT INTO 注册团员名单 VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement pst = database.getpst(sql);
			pst.setString(1,m.getStuMPName());
			pst.setString(2,m.getMPContent());
			pst.setString(3,m.getMPlevel());
			pst.setString(4,m.getMPDate());
			pst.setString(5,m.getMPCategory());
			pst.setString(6,m.get_Class());
			pst.execute();
			pst.close();
	}
	public void clear(String Class) throws SQLException {
		String sql = "delete * from where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1,Class);
		pst.execute();
		pst.close();
	}
	public void rewrite(String Class,JSONArray array) {
		int size = array.size();
		int count = 0;
		String sql = "insert into 支部团员奖惩记录";
		JSONObject mandpjson;
		String stuMPName;
		String MPContent;
		String MPlevel;
		String MPDate;
		String MPCategory;

		while(count!=size) {
			mandpjson = array.getJSONObject(count);
			stuMPName = mandpjson.getString("stuMPName");
			MPContent = mandpjson.getString("MPContent");
			MPlevel = mandpjson.getString("MPlevel");
			MPDate = mandpjson.getString("MPDate");
			MPCategory = mandpjson.getString("MPCategory");
			m = new MandP(stuMPName,MPContent,MPlevel,MPDate,MPCategory ,Class);
			savemandp(m);
			count++;
		}
	}

}
