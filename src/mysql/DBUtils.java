package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *数据库连接类
 *@author  wsz
 *@createdTime 2018年3月22日
*/
public class DBUtils {

	private static String DRIVERCLASS;
	private static String URL;
	private static String USERNAME;
	private static String PASSWORD;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("mysql/db");
		DRIVERCLASS = bundle.getString("DRIVERCLASS");
		URL 	    = bundle.getString("URL");
		USERNAME    = bundle.getString("USERNAME");
		PASSWORD    = bundle.getString("PASSWORD");
	}
	
	public static Connection getConn() {
		Connection conn = null;
		try {
			Class.forName(DRIVERCLASS);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void closeAll(Connection conn, PreparedStatement pstmt,ResultSet rs) {
		try {
			if(null != conn)
				conn.close();
			if(null != pstmt)
				pstmt.close();
			if(null != rs)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.err.println(DBUtils.getConn());
	}
}
