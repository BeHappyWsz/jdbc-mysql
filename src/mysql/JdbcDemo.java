package mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
 * jdbc操作数据库-(批量)增删改查/事务模拟/存储过程/sql注入
 * @author wsz
 * @date 2018年3月23日
 */
public class JdbcDemo {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	@Before
	public void init() {
		conn = DBUtils.getConn();
	}
	
	@After
	public void destory() {
		DBUtils.closeAll(conn, pstmt, rs);
	}
	
	@Test
	public void add() {
		String sql = "insert into t_user(username,password) values(?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "test");
			pstmt.setString(2, "test");
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量增加三组数据
	 */
	@Test
	public void batchAdd() {
		String sql = "insert into t_user(username,password) values(?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, "b1");
			pstmt.setString(2, "b1");
			pstmt.addBatch();
			
			pstmt.setString(1, "b2");
			pstmt.setString(2, "b3");
			pstmt.addBatch();
			
			pstmt.setString(1, "b2");
			pstmt.setString(2, "b3");
			pstmt.addBatch();
			
			int[] flag = pstmt.executeBatch();
			for (int i : flag) {
				System.out.print(i+" ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void delete() {
		String sql = "delete from t_user where username = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "test");
			int count = pstmt.executeUpdate();
			System.out.println("操作数量:"+count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void batchDelete() {
		String sql = "delete from t_user where username = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "a");
			pstmt.addBatch();
			
			pstmt.setString(1, "b");
			pstmt.addBatch();
			
			pstmt.setString(1, "add");
			pstmt.addBatch();
			
			int[] count = pstmt.executeBatch();
			for (int i : count) {
				System.out.println(i+" ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void update() {
		String sql = "update t_user set password=? where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "update");
			pstmt.setInt(2, 28);
			int count = pstmt.executeUpdate();
			System.out.println("操作数量:"+count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void batchUpdate() {
		String sql = "update t_user set password=? where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "58");
			pstmt.setInt(2, 58);
			pstmt.addBatch();
			
			pstmt.setString(1, "59");
			pstmt.setInt(2, 59);
			pstmt.addBatch();
			
			pstmt.setString(1, "60");
			pstmt.setInt(2, 60);
			pstmt.addBatch();
			
			int[] count = pstmt.executeBatch();
			for (int i : count) {
				System.out.println(i+" ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void search() {
		String sql = "select id,username,password from t_user where id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 28);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getInt("id")+" "+rs.getString("username")+" "+rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getAll() {
		String sql = "select id,username,password from t_user ";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getInt("id")+" "+rs.getString("username")+" "+rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 模拟transaction事务处理
	 * 1.必须关闭自动提交功能
	 * 2.手动进行提交
	 * 3.提交动作之前出现错误不会进行数据库更新操作
	 */
	@Test
	public void transaction() {
		try {
			conn.setAutoCommit(false);
			update(27,"27");
			System.out.println(1/0);
			update(28,"28");
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(int id,String password) {
		String sql = "update t_user set password=? where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setInt(2, id);
			int count = pstmt.executeUpdate();
			System.out.println("操作数量:"+count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 调用存储过程
	 * 1.定义存储过程
create procedure countByName(in username varchar(255),out count int(11))
begin
  select count(*) into count
  from t_user
  where username = username;
end
	 * 2.调用
	 * 3.获取返回结果
	 */
	@Test
	public void procedure() {
		String sql = "call countByName(?,?);";
		try {
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, "admin");
			cstmt.registerOutParameter(2, Types.INTEGER);
			cstmt.execute();
			int count = cstmt.getInt(2);
			System.out.println("查询总数量:"+count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试sql注入
	 * 模糊查询出了所有数据
	 */
	@Test
	public void sqlInjec() {
		String param = "'%%' or 1=1";
		String sql = "select * from t_user where username like "+param;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				System.out.println( rs.getInt("id")+" "+rs.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
