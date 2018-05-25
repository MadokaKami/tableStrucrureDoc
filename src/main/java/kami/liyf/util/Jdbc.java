package kami.liyf.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jdbc {
	private static volatile Connection conn = null;
	private static PreparedStatement pstmt = null;
	private static String user;
	private static String password;
	private static String url;
	public Jdbc() {
		// 空构造器
	}
	/*static{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			synchronized(new byte[0]) {
				conn = DriverManager.getConnection(url, user, password);
			}
		} catch (ClassNotFoundException e) {
			System.err.println("加载JDBC驱动失败");
			e.printStackTrace();
		} catch(SQLException e){
			System.err.println("数据库异常");
			JOptionPane.showConfirmDialog(null, "数据库连接失败","错误",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}*/
	public static Connection getConnection(){
		if(conn == null){
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				synchronized (conn != null ? conn : new byte[0]) {
					conn = DriverManager.getConnection(url, user, password);
				}
			} catch (ClassNotFoundException e) {
				System.err.println("加载JDBC驱动失败");
				e.printStackTrace();
			} catch(SQLException e){
				System.err.println("数据库异常");
				JOptionPane.showConfirmDialog(null, "数据库连接失败","错误",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return conn;
	}
	public static Connection getPoolConnection(){
		if(conn == null){
			try {
				Context context = new InitialContext();
				DataSource ds =
		 		(DataSource) context.lookup("java:/comp/env/jdbc/oracleds");
				conn = ds.getConnection();
				System.out.println("连接数据库成功！");
			} catch (NamingException e) {
				System.out.println("连接数据库失败！");
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("连接数据库失败！");
				e.printStackTrace();
			}
		} 
		return conn;
	}
	
	/**
	 * 关闭数据库连接
	 */
	public static void closeConnection(){
		synchronized (conn != null ? conn : new byte[0]) {
			try {
				if(pstmt != null){
					pstmt.close();
					pstmt = null;
				}
				if(conn != null){
					conn.close();
					conn = null;
				}
				System.out.println("连接关闭成功");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("关闭失败");
			}
		}		
	}
	
	/**
	 * 关闭Statement
	 */
	public static void closeStmt(){
		try {
			if(pstmt != null){
				pstmt.close();
				pstmt = null;
			}
			System.out.println("连接关闭成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("关闭失败");
		}		
	}
	
	/**
	 * 更新操作
	 * @param sql
	 * @param obj
	 * @return
	 */
	public static int executeUpdate(String sql, Object[] obj){
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	/**
	 * 无参查询操作
	 * @param sql
	 * @return
	 */
	public static ResultSet executeQuery(String sql){
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * 有参查询操作
	 * @param sql
	 * @param obj 查询条件数组
	 * @return
	 */
	public static ResultSet executeQuery(String sql, Object[] obj){
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement(sql);
			for(int i = 0; i < obj.length; i++){
				pstmt.setObject(i + 1, obj[i]);
			}
			rs = pstmt.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rs;
	}
	
	public static List<Map<String,Object>> queryForList(String sql, Object[] obj) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();//返回值
		ResultSet rs = executeQuery(sql, obj);
		try {
			ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据  
	        int columnCount = md.getColumnCount();   //获得列数   
	        while (rs.next()) {  
	            Map<String,Object> rowData = new HashMap<String,Object>();  
	            for (int i = 1; i <= columnCount; i++) {  
	                rowData.put(md.getColumnName(i), rs.getObject(i));  
	            }  
	            list.add(rowData);  
	  
	        } 
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
		return list;
		
	}
	
	/**
	 * 开启事务
	 */
	public static void beginTransaction(){
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 提交
	 */
	public static void commit(){
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 回滚
	 */
	public static void rollback(){
		try {
			conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setUser(String user) {
		Jdbc.user = user;
	}

	public static void setPassword(String password) {
		Jdbc.password = password;
	}

	public static void setUrl(String url) {
		Jdbc.url = url;
	}
}
