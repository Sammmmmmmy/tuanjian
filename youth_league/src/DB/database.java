package DB;
import java.sql.*;
public class database {
	private static Connection connection;
	private static Statement statement;
	private static PreparedStatement prestatement;
	private static ResultSet resultset;
	public static void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/youth_league_branch?useSSL=false&serverTimezone=UTC","root","123456");
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static ResultSet executeQuery(String sql){
		try {
			resultset = statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultset;
	}
	
	public static void executeUpdate_sta(String sql)  {
		try {
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static PreparedStatement getpst(String sql) {
		
		try {
			prestatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prestatement;
	}
	
	
	public static void disconnect() {
		try {
			if(statement != null)
				statement.close();
			if(prestatement != null)
				prestatement.close();
			if(resultset!=null)
				resultset.close();
			if(connection!=null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
