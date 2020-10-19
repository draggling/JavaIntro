/**
 * 
 */
package com.ss.lms.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


/**
 * This is a demo class for JDBC Connection to Authors LMS Table
 * 
 * @author danwoo
 *
 */
public class ReadAllAuthorsByName {

	public static String driverName = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/library_java?useSSL=false&allowPublicKeyRetrieval=true";
	public static String userName = "root";
	public static String password = "dumpling97";

	/**	
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		System.err.println("Enter a Author Name to search: ");
		String authorName = scan.nextLine();
		String query = "SELECT * FROM tbl_author where authorName = '"+authorName+"'";
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(url, userName, password);
//			Statement stmt = conn.createStatement();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tbl_author where authorName = ?");
			pstmt.setString(1, authorName);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("Author Name: "+ rs.getString("authorName"));
				System.out.println("Author ID: "+rs.getInt("authorId"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

}
