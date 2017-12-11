package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Utils {
	
	private static String DRIVER = "com.mysql.jdbc.Driver";
	
	private static String URL = "jdbc:mysql://106.14.213.251:3306/mybatis?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
	
	private static String USR ="wsz";
	
	private static String PASSWORD ="wsz";
	
	public static Connection getConnection() {
		try {
			Class.forName(DRIVER);
			try {
				Connection connection = DriverManager.getConnection(URL, USR, PASSWORD);
				return connection;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("类加载失败");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void clearSelectConn(Connection conn,PreparedStatement pst, ResultSet rs) {
		try {
			if(rs != null) rs.close();
			if(pst != null) pst.close();
			if(conn != null) conn.close();
		}catch(SQLException  e) {
			e.printStackTrace();
		}
	}
	
	public static void clearUpdateConn(Connection conn, PreparedStatement pst) {
		try {
			if(conn != null )conn.close();
			if(pst != null) pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
			
	public static void main(String[] args) {
		insertData();
		selectData();
		selectData1();
		updateData();
		deletedData();
	}
	
	public static void deletedData() {
		Connection conn = Utils.getConnection();
		PreparedStatement pst = null;
		if(conn != null) {
			String sql = "delete from t_user where id = ?";
			try {
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 32);
				int a = pst.executeUpdate();
				System.out.println(a);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				clearUpdateConn(conn, pst);
			}
		}else {
			System.out.println("数据库连接失败");
		}
	}
	
	public static void selectData() {
		Connection conn = Utils.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(conn != null) {
			try {
				String sql = "select * from t_user where id > ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 20);
				rs = pst.executeQuery();
				while(rs.next()) {
					int id = rs.getInt("id");
					String username = rs.getString("username");
					String realName = rs.getString("real_name");
					System.out.println("id:"+id+" username:"+username+" realName:"+realName);
				}
			}catch (SQLException e) {
				
			}finally {
				clearSelectConn(conn, pst, rs);
			}
		}else {
			System.out.println("连接数据库失败");
		}
	}
	
	//sql注入
	public static void selectData1() {
		Connection conn = Utils.getConnection();
		PreparedStatement pst = null;
		Statement st= null;
		ResultSet rs = null;
		if(conn != null) {
			try {
				String a ="' or 1 or '";
				String sql = "select * from t_user where username = '"+a+"'";
				System.out.println(sql);//select * from t_user where username = '' or 1 or ''
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				while(rs.next()) {
					int id = rs.getInt("id");
					String username = rs.getString("username");
//					String realName = rs.getString("real_name");
					String realName = rs.getString(1);
					System.out.println("id:"+id+" username:"+username+" realName:"+realName);
				}
			}catch (SQLException e) {
				
			}finally {
				clearSelectConn(conn, pst, rs);
			}
		}else {
			System.out.println("连接数据库失败");
		}
	}
	
	
	public static void updateData() {
		Connection conn = Utils.getConnection();
		PreparedStatement pst = null;
		try {
			String sql = "update t_user set username = ? where id = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, "28");
			pst.setInt(2, 28);
			int rs = pst.executeUpdate();
			System.out.println(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			clearUpdateConn(conn, pst);
		}
	}
	
	public static void insertData() {
		Connection conn = Utils.getConnection();
		PreparedStatement pst = null;
		if(conn != null) {
			String sql = "insert into t_user(username,real_name) "
					+ " values(?,?)";
			try {
				pst = conn.prepareStatement(sql);
				pst.setString(1, "aaa");
				pst.setString(2, "aaa");
				int total = pst.executeUpdate();
				System.out.println(total);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				clearUpdateConn(conn,pst);
			}
		}else {
			System.out.println("连接数据库失败");
		}
	}
}
