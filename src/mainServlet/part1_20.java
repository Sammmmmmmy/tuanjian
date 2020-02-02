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

//������ͼƬ��
//��֧�����
public class part1_20 extends HttpServlet {
	//����Ҫ�ĳɷ�������ַ
	private String dir = "E:\\�Ž�webͼƬ\\";
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
		//׼��sql��ѯ�ŷ��սɽ�����õ������ҳ���͸����������ϸ��Ϣ
		String sql = "select * from �ŷ��սɽ��� where Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		//size��ĳ�༶���еĽ�����
		int Pagesize = 0;
		//pageJSON��һ������JSON�������ӵ�Pages��
		JSONObject pageJSON;
		JSONArray Pages = new JSONArray();
		
		//���о�ΪpageJSON�е�Ԫ��
		//�ò��ó�ʼ����
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
			//����Class��page��Ż�ȡ��page�����µ�images����Ŵ�1��ʼ
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
		//��ǰ�˻�ȡClass��array
		String Class = request.getParameter("Class");
		JSONArray Pages = JSONArray.fromObject(request.getParameter("Pages"));
		//����ɾ�����ݿ��е�����url�Ͷ�Ӧ�ı���ͼƬ
		//��������еĽ�����Ϣ
		clearimage(Class);
		clearpage(Class);
		//�������ÿһ�������ͼƬ����д�����ݿ�
		rewrite(Class,Pages);
	}
	
	//����ͼƬ�ĵ�ַ��д�����ݿ�,����ͼƬ������
	//��������Ϣд�����ݿ�
	public void rewrite(String Class,JSONArray Pages) throws SQLException {
		int PageSize = Pages.size();
		JSONObject PageJSON;
		
		JSONArray Images;
		String receiveDate;
		int totalNum;
		double totalMoney;
		//���ڽ�����д�����ݿ��sql���
		String pagesql = "insert into �ŷ��սɽ��� values (?,?,?,?)";
		PreparedStatement pagepst = database.getpst(pagesql);
		pagepst.setString(4, Class);
		//���ڱ���ͼƬ�����
		String imagesql = "insert into �ŷ��ս�ͼƬ values (?,?,?)";
		PreparedStatement imagepst = database.getpst(imagesql);
		imagepst.setString(3, Class);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
		String url;
		String date;
		for(int i = 0;i<PageSize;i++) {
			PageJSON = Pages.getJSONObject(i);
			//��������Ϣд��
			receiveDate = PageJSON.getString("receiveDate");
			totalNum = PageJSON.getInt("totalNum");
			totalMoney = PageJSON.getDouble("totalMoney");
			pagepst.setString(1, receiveDate);
			pagepst.setInt(2, totalNum);
			pagepst.setDouble(3, totalMoney);
			pagepst.execute();
			//��ͼƬд��
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
	//�������ݿ���ͼƬ��id��ȡĳһ�����ڵ����е�ͼƬ��url
	//�ٸ���url��ȡͼƬ��base64
	public JSONArray getImages(String Class,int id) {
		//base64����
		JSONArray images = new JSONArray();
		String sql = "select * form �ŷ��ս�ͼƬ where Class = ? and id = ?";
		PreparedStatement pst = database.getpst(sql);
		try {
			//���pst��ִ�еõ�ResultSet
			pst.setString(1,Class);
			pst.setInt(2,id);
			ResultSet set = pst.executeQuery(sql);
			//����ResultSet��ȡurl�����õ�base64
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
		//��ѯ�༶����ͼƬ��url������url�ӷ�������ɾ��ͼƬ
		String selectsql = "select * from �ŷ��ս�ͼƬ where Class = ?";
		PreparedStatement pst = database.getpst(selectsql);
		pst.setString(1, Class);
		ResultSet set = pst.executeQuery();
		
		File file;
		while(set.next()) {
			file = new File(set.getString("imageurl"));
			file.delete();
		}
		
		pst.close();//������bug
		set.close();
		
		//�����ݿ���ɾ�����е�ͼƬ��ַ
		String sql = "DELETE FROM �ŷ��ս�ͼƬ WHERE Class = ?";
		pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	public void clearpage(String Class) throws SQLException {
		String sql = "DELETE FROM �ŷ��սɽ��� WHERE Class = ?";
		PreparedStatement pst = database.getpst(sql);
		pst.setString(1, Class);
		pst.execute();
		pst.close();
	}
	

}
