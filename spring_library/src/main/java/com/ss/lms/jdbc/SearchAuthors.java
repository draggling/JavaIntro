/**
 * 
 */
package com.ss.lms.jdbc;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.entity.Author;
import com.ss.lms.service.ConnectionUtil;

/**
 * This is a demo class for JDBC Connection to Authors LMS Table
 * 
 * @author danwoo
 *
 */
public class SearchAuthors {

	public static String driverName = "com.mysql.jdbc.Driver";
	public static ConnectionUtil conUtil = new ConnectionUtil();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(searchAuthor());
		/*
		 * Scanner scan = new Scanner(System.in);
		 * System.err.println("Enter a Author Name to search: "); String authorName =
		 * scan.nextLine(); String query =
		 * "SELECT * FROM tbl_author where authorName = '"+authorName+"'"; try
		 * (Connection conn = conUtil.getConnection()) { Class.forName(driverName);
		 * Statement stmt = conn.createStatement(); ResultSet rs =
		 * stmt.executeQuery(query); no results are found if(!rs.next()) {
		 * System.out.println("No authors found"); } else {
		 * System.out.println("Author Name: "+ rs.getString("authorName") +
		 * ", Author ID: "+rs.getInt("authorId")); } if(rs.next()) {
		 * System.out.println("Duplicate Author with id = " + rs.getInt("authorId")); }
		 * 
		 * rs.beforeFirst(); while(rs.next()) { System.out.println("Author Name: "+
		 * rs.getString("authorName") + ", Author ID: "+rs.getInt("authorId"));
		 * //System.out.println("Author ID: "+rs.getInt("authorId")); }
		 * 
		 * } catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
		 */
	}

	public static String searchAuthor() {
		Scanner scan = new Scanner(System.in);
		System.err.println("Enter a Author Name to search: ");
		String authorName = scan.nextLine();
		try (Connection conn = conUtil.getConnection()) {
			Class.forName(driverName);
			AuthorDAO adao = new AuthorDAO();
			List<Author> authorList = adao.searchAuthor(authorName);
			System.out.println("authorList.size() = " + authorList.size());
			switch (authorList.size()) {
			case (0):
				return authorName + " not found";
			case (1):
				return "Author Name = " + authorList.get(0).getAuthorName() + ", Author ID = " + authorList.get(0).getAuthorId();
			default:
				return "error: multiple authors with the same name found";
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Error";
		} finally {
			scan.close();
		}
	}
}
