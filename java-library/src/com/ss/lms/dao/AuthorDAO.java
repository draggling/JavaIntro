/**
 * 
 */
package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Author;

/**
 * @author danwoo
 *
 */
public class AuthorDAO extends BaseDAO<Author> {

	public AuthorDAO(Connection conn) {
		super(conn);
	}

	public void addAuthor(String authorName) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_author (authorName) VALUES (?)", new Object[] { authorName });
	}

	public void updateAuthor(Author author) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_author SET authorName = ? WHERE authorId = ?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });
	}

	public void deleteAuthor(Author author) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_author WHERE authorId = ?", new Object[] { author.getAuthorId() });
	}
	
	public void deleteBookAuthorPair(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_book_authors WHERE bookId = ? and authorId = ?", new Object[] {bookId, authorId});
	}

	public List<Author> readAllAuthors() throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_author", null);
	}
	public boolean searchAuthorBoolean(String authorName) throws SQLException, ClassNotFoundException {
		if(read("SELECT * FROM tbl_author WHERE authorName = (?)", new Object[] {authorName}).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<Author> readAllAuthorsByName(String searchString) throws SQLException, ClassNotFoundException {
		searchString = "%"+searchString+"%";
		return read("SELECT * FROM tbl_author WHERE authorName LIKE (?)", new Object[] {searchString});
	}
	
	public List<Author> searchAuthor(String searchString) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_author WHERE authorName = ?", new Object[] {searchString});
	}
	
	
	public boolean findBookAuthorPair(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException {
		if(read("SELECT * FROM tbl_book_authors WHERE bookId = ? and authorId = ?", new Object[] {bookId, authorId}).size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addBookAuthorPair(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_book_authors VALUES (?, ?)", new Object[] { bookId, authorId });
	}
	
	public List<Author> findBookAuthors(Integer bookId) throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM tbl_author a WHERE "
				+ "EXISTS (SELECT * FROM tbl_book_authors ba WHERE ba.authorId = a.authorId and ba.bookID = ?)",
				new Object[] {bookId});
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			authors.add(new Author(rs.getInt("authorId"), rs.getString("authorName")));
			//also populate the books written by this Author
		}
		return authors;
	}
}
