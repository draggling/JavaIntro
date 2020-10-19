/**
 * 
 */
package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.Branch;

/**
 * @book danwoo
 *
 */
public class BookDAO extends BaseDAO<Book>{

	public BookDAO(Connection conn) {
		super(conn);
	}
	
	public boolean bookTitleExists(String title) throws ClassNotFoundException, SQLException {
		if(read("SELECT * from tbl_book WHERE title = (?)", new Object[] {title}).size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public int bookIsLoaned(int bookId) throws ClassNotFoundException, SQLException {
		int count = (read("SELECT * FROM tbl_book b WHERE EXIST("
				+ "SELECT * FROM tbl_book_copies bc WHERE bc.bookId = b.bookId and bc.bookId = ? and dateIn IS NULL)",
				new Object[] {bookId})).size();
		return count;
	}
	
	public Book getBook(Integer bookId) throws ClassNotFoundException, SQLException {
		return read("SELECT * from tbl_book WHERE bookId = ?", new Object[] { bookId }).get(0);
	}

	public void addBook(Book book) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_book (title) VALUES (?)", new Object[] { book.getTitle() });
	}
	
	public Integer addBookWithPk(Book book) throws ClassNotFoundException, SQLException {
		return saveWithPk("INSERT INTO tbl_book (title, pubId) VALUES (?,?)", new Object[] { book.getTitle(), book.getPubId() });
	}
	public void updateBookPublisher(Book book) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_book SET pubId = ? where bookId = ?", new Object[] {book.getPubId(), book.getBookId()});
	}
	
	public void updateBook(Book book) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_book SET title = (?) WHERE bookId = ?",
				new Object[] { book.getTitle(), book.getBookId() });
	}

	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_book WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readAllBooks() throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_book", null);
	}
	
	public boolean checkIfBookInBranch(Branch branch, Book book) throws SQLException, ClassNotFoundException {
		if(read("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and bookId = ?)",
				new Object[] {branch.getBranchId(), book.getBookId()}).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<Book> readAllBooksByName(String searchString) throws SQLException, ClassNotFoundException {
		searchString = "%"+searchString+"%";
		return read("SELECT * FROM tbl_book WHERE title LIKE ?", new Object[] {searchString});
	}
	public List<Book> readBranchBooks(int branchId) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and noOfCopies > 0)", new Object[] {branchId});
	}
	
	
	public List<Book> readAllBranchBooksByName(int branchId, String searchString) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and noOfCopies > 0 and b.title = (?))", 
				new Object[] {branchId, searchString});
	}
	
	public List<Book> readAvailableBranchBooks(int branchId) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and noOfCopies > "
				+ "(SELECT COUNT(*) FROM tbl_book_loans bl WHERE bl.bookId = b.bookId and bl.branchId = ? and dateIn IS NULL))",
				new Object[] {branchId, branchId}); 
	}
	
	public List<Book> readAvailableBranchBooksByName(int branchId, String searchString) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId  and b.title = (?) and branchId = ? and noOfCopies > "
				+ "(SELECT COUNT(*) FROM tbl_book_loans bl WHERE bl.bookId = b.bookId and bl.branchId = ? and dateIn IS NULL))",
				new Object[] {searchString, branchId,  branchId}); 
	}


		
	public List<Book> readAllLoanedBooks(int cardNo) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_book b "
				+ "WHERE EXISTS ( "
						+ "SELECT * " 
						+ "FROM tbl_book_loans bl " 
						+ " WHERE b.bookId = bl.bookId and bl.cardNo = ? and bl.dateIn IS NULL)", new Object[] {cardNo});
	}
	
	public boolean readBookCopies(Book b, int branchId) throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = conn.prepareStatement("SELECT noOfCopies FROM tbl_book_copies WHERE bookId = (?) and branchId = (?)");
		pstmt.setInt(1, b.getBookId());
		pstmt.setInt(2, branchId);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println("Number of book copies: "+ rs.getString("noOfCopies"));
			return true;
		}
		System.err.println("No copies found for book: " + b.getTitle());
		return false;
	}
	public void addBookCopies(Book b, int branchId, int copies) throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = conn.prepareStatement("UPDATE tbl_book_copies SET noOfCopies = noOfCopies + (?) WHERE bookId = (?) and branchId = (?)");
		pstmt.setInt(1, copies);
		pstmt.setInt(2, b.getBookId());
		pstmt.setInt(3, branchId);
		pstmt.executeUpdate();
	}
	
	public void addNewBookCopies(Book b, int branchId, int copies) throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES (?,?,?)");
		pstmt.setInt(1, b.getBookId());
		pstmt.setInt(2, branchId);
		pstmt.setInt(3, copies);
		pstmt.executeUpdate();
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Book> books = new ArrayList<>();
		AuthorDAO adao = new AuthorDAO(conn);
		while (rs.next()) {
			Book b = new Book(rs.getInt("bookId"), rs.getString("title"), rs.getInt("pubId"));
			b.setAuthors(adao.read("select * from tbl_author where authorId IN (select authorId from tbl_book_authors where bookId = ?)", new Object[] {b.getBookId()}));
			//b.setGenres()
			books.add(b);
		}
		return books;
	}
}