/**
 * 
 */
package com.ss.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopies;
//import com.ss.lms.entity.Book;
import com.ss.lms.entity.Branch;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * @BookCopies danwoo
 *
 */
@Repository
public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>>{
	//Integer bookId, Integer branchId, Integer noOfCopies


	public BookCopies getBookCopies(Integer bookCopiesId) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * from tbl_book_opies WHERE bookCopiesId = ?", new Object[] { bookCopiesId }, this).get(0);
	}
	
	public List<BookCopies> findBookCopiesByBranch(Book book, Branch branch) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_copies WHERE bookId = ? and branchId = ?", new Object[] {book.getBookId(), branch.getBranchId()}, this);
	}
	
	public void addBookCopies(BookCopies BookCopies) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES (?,?,?)", new Object[] { BookCopies.getBookId(), BookCopies.getBranchId(), BookCopies.getNoOfCopies()});
	}
	
	public void addBookCopies(String bookCopiesName, String bookCopiesAddress) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_book_copies (bookCopiesName, bookCopiesAddress) VALUES (?,?)", new Object[] { bookCopiesName, bookCopiesAddress });
	}
	
	public void addBookCopies(Book b, int branchId, int copies) throws SQLException, ClassNotFoundException {
		jdbcTemplate.update("UPDATE tbl_book_copies SET noOfCopies = noOfCopies + (?) WHERE bookId = (?) and branchId = (?)",
				new Object[] {copies, b.getBookId(), branchId});
	}
	
	public void addNewBookCopies(Book b, int branchId, int copies) throws SQLException, ClassNotFoundException {
		jdbcTemplate.update("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES (?,?,?)",
				new Object[] {b.getBookId(), branchId, copies});
	}

	public void updateBookCopies(BookCopies BookCopies) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book_copies SET noOfCopies = (?) WHERE bookId = (?) and branchId = ?",
				new Object[] {BookCopies.getNoOfCopies(), BookCopies.getBookId(), BookCopies.getBranchId() });
	}

	public void deleteBookCopies(BookCopies BookCopies) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_book_copies WHERE BookId = ? and BranchId = ?", new Object[] { BookCopies.getBookId(), BookCopies.getBranchId() });
	}

	public List<BookCopies> readAllBookCopies() throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_copies", this);
	}

	public boolean readBookCopies(Book b, int branchId) throws SQLException, ClassNotFoundException {
		int count = jdbcTemplate.query("SELECT * FROM tbl_book_copies WHERE bookId = (?) and branchId = (?)",
				new Object[] {b.getBookId(), branchId}, this).get(0).getNoOfCopies();

		if(count > 0) {
			System.out.println("Number of book copies: "+ count);
			return true;
		} else {
			System.err.println("No copies found for book: " + b.getTitle());
			return false;
		}
	}
	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		List<BookCopies> BookCopies = new ArrayList<>();
		while (rs.next()) {
			BookCopies b = new BookCopies(rs.getInt("bookId"), rs.getInt("branchId"), rs.getInt("noOfCopies"));
			BookCopies.add(b);
		}
		return BookCopies;
	}
}