package mainServlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DB.database;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import utilities.Base64utilities;

//多界面多图片类
//团支部风采
public class part1_20 extends HttpServlet {
	//后期要改成服务器地址
	private String dir = "E:\\团建web图片\\";
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
		//准备sql查询团费收缴界面表，得到界面的页数和各个界面的详细信息
		String sql = "select * from 团费收缴界面 where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//size是某班级所有的界面数
		int Pagesize = 0;
		//pageJSON是一个界面JSON，最后添加到Pages中
		JSONObject pageJSON;
		JSONArray Pages = new JSONArray();
		
		//下列均为pageJSON中的元素
		//用不用初始化？
		String receiveDate;
		int totalNum;
		double totalMoney;
		int ImageSize = 0;
		JSONArray Images;
		
		while(set.next()) {
			pageJSON = new JSONObject();
			receiveDate = set.getString("receiveDate");
			totalNum = set.getInt("totalNum");
			totalMoney = set.getDouble("totalMoney");
			//根据Class和page序号获取该page界面下的images，序号从1开始
			Images = getImages(Class,Pagesize+1);
			ImageSize = Images.size();
			pageJSON.put("receiveDate", receiveDate);
			pageJSON.put("totalNum",totalNum);
			pageJSON.put("totalMoney",totalMoney);
			pageJSON.put("ImageSize",ImageSize);
			pageJSON.put("Images",Images);
			Pagesize++;
			Pages.add(pageJSON);
		}

		JSONObject write = new JSONObject();
		write.put("PageSize",Pagesize);
		write.put("Pages", Pages);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(write.toString());
		
		pst.close();
		set.close();
		
	}
	public void update(HttpServletRequest request, HttpServletResponse response) throws SQLException  {
		//从前端获取Class和array
		String Class = request.getParameter("Class");
		JSONArray Pages = JSONArray.fromObject(request.getParameter("Pages"));
		//首先删除数据库中的所有url和对应的本地图片
		//在清楚所有的界面信息
		clearimage(Class);
		clearpage(Class);
		//将界面和每一个界面的图片重新写入数据库
		rewrite(Class,Pages);
	}
	
	//生成图片的地址并写入数据库,保存图片到本地
	//将界面信息写入数据库
	public void rewrite(String Class,JSONArray Pages) throws SQLException {
		int PageSize = Pages.size();
		JSONObject PageJSON;
		
		JSONArray Images;
		String receiveDate;
		int totalNum;
		double totalMoney;
		//用于将界面写入数据库的sql语句
		String pagesql = "insert into 团费收缴界面 values (?,?,?,?)";
		PreparedStatement pagepst = database.getpst(pagesql);
		pagepst.setString(4, Class);
		//用于保存图片的语句
		String imagesql = "insert into 团费收缴图片 values (?,?,?)";
		PreparedStatement imagepst = database.getpst(imagesql);
		imagepst.setString(3, Class);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
		String url;
		String date;
		for(int i = 0;i<PageSize;i++) {
			PageJSON = Pages.getJSONObject(i);
			//将界面信息写入
			receiveDate = PageJSON.getString("receiveDate");
			totalNum = PageJSON.getInt("totalNum");
			totalMoney = PageJSON.getDouble("totalMoney");
			pagepst.setString(1, receiveDate);
			pagepst.setInt(2, totalNum);
			pagepst.setDouble(3, totalMoney);
			pagepst.execute();
			//将图片写入
			Images = PageJSON.getJSONArray("Images");
			int ImageSize = Images.size();
			for(int j = 0;j<ImageSize;j++) {
				date = df.format(new Date());
				url = this.dir+date+"_"+i+"_"+j+".jpg";
				imagepst.setString(1, url);
				imagepst.setInt(2,j+1);
				imagepst.execute();
				Base64utilities.Base64ToImage(Images.getString(j), url);
			}
			}
	}
	//根据数据库内图片的id获取某一界面内的所有的图片的url
	//再根据url获取图片的base64
	public JSONArray getImages(String Class,int id) {
		//base64数组
		JSONArray images = new JSONArray();
		String sql = "select * form 团费收缴图片 where Class = ? and id = ?";
		PreparedStatement pst = database.getpst(sql);
		try {
			//填充pst并执行得到ResultSet
			pst.setString(1,Class);
			pst.setInt(2,id);
			ResultSet set = pst.executeQuery(sql);
			//遍历ResultSet获取url，并得到base64
			String imageurl = null;
			String imagebase64 = null;
			while(set.next()) {
				imageurl = set.getString("imageurl");
				imagebase64 = Base64utilities.ImageToBase64(imageurl);
				images.add(imagebase64);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return images;
	}
	public void clearimage(String Class) throws SQLException {
		//查询班级所有图片的url并根据url从服务器上删除图片
		String selectsql = "select * from 团费收缴图片 where Class = ?";
		PreparedStatement pst = database.getpst(selectsql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		
		File file;
		while(set.next()) {
			file = new File(set.getString("imageurl"));
			file.delete();
		}
		
		pst.close();//可能有bug
		set.close();
		
		//从数据库中删除所有的图片地址
		String sql = "DELETE FROM 团费收缴图片 WHERE Class = ?";
		pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	public void clearpage(String Class) throws SQLException {
		String sql = "DELETE FROM 团费收缴界面 WHERE Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	

}
