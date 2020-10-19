/**
 * 
 */
package com.ss.lms.dao;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopies;
//import com.ss.lms.entity.Book;
import com.ss.lms.entity.Branch;

/**
 * @BookCopies danwoo
 *
 */
public class BookCopiesDAO extends BaseDAO<BookCopies>{
	//Integer bookId, Integer branchId, Integer noOfCopies
	public BookCopiesDAO(Connection conn) {
		super(conn);
	}

	public BookCopies getBookCopies(Integer bookCopiesId) throws ClassNotFoundException, SQLException {
		return read("SELECT * from tbl_book_opies WHERE bookCopiesId = ?", new Object[] { bookCopiesId }).get(0);
	}
	
	public List<BookCopies> findBookCopiesByBranch(Book book, Branch branch) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_book_copies WHERE bookId = ? and branchId = ?", new Object[] {book.getBookId(), branch.getBranchId()});
	}
	
	public void addBookCopies(BookCopies BookCopies) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES (?,?,?)", new Object[] { BookCopies.getBookId(), BookCopies.getBranchId(), BookCopies.getNoOfCopies()});
	}
	
	public void addBookCopies(String bookCopiesName, String bookCopiesAddress) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_book_copies (bookCopiesName, bookCopiesAddress) VALUES (?,?)", new Object[] { bookCopiesName, bookCopiesAddress });
	}

	public void updateBookCopies(BookCopies BookCopies) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_book_copies SET noOfCopies = (?) WHERE bookId = (?) and branchId = ?",
				new Object[] {BookCopies.getNoOfCopies(), BookCopies.getBookId(), BookCopies.getBranchId() });
	}

	public void deleteBookCopies(BookCopies BookCopies) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_book_copies WHERE BookId = ? and BranchId = ?", new Object[] { BookCopies.getBookId(), BookCopies.getBranchId() });
	}

	public List<BookCopies> readAllBookCopies() throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_book_copies", null);
	}

	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<BookCopies> BookCopies = new ArrayList<>();
		while (rs.next()) {
			BookCopies b = new BookCopies(rs.getInt("bookId"), rs.getInt("branchId"), rs.getInt("noOfCopies"));
			BookCopies.add(b);
		}
		return BookCopies;
	}
}