/**
 * 
 */
package com.ss.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;

/**
 * @book danwoo
 *
 */
@Repository
public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>>{
	
	public boolean bookTitleExists(String title) throws ClassNotFoundException, SQLException {
		if(jdbcTemplate.query("SELECT * from tbl_book WHERE title = (?)", new Object[] {title}, this).size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public Integer getBooIdkWithTitle(String title) throws ClassNotFoundException, SQLException {
		return (jdbcTemplate.query("SELECT * from tbl_book WHERE title = (?)", new Object[] {title}, this).get(0).getBookId()); 
	}
	
	public int bookIsLoaned(int bookId) throws ClassNotFoundException, SQLException {
		int count = (jdbcTemplate.query("SELECT * FROM tbl_book b WHERE EXISTS("
				+ "SELECT * FROM tbl_book_loans bc WHERE bc.bookId = b.bookId and bc.bookId = ? and dateIn IS NULL)",
				new Object[] {bookId}, this)).size();
		return count;
	}
	
	public Book getBook(Integer bookId) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * from tbl_book WHERE bookId = ?", new Object[] { bookId }, this).get(0);
	}

	public void addBook(Book book) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_book (title) VALUES (?)", new Object[] { book.getTitle() });
	}
	
	/*
	public Integer addBookWithPk(Book book) throws ClassNotFoundException, SQLException {
		return saveWithPk("INSERT INTO tbl_book (title, pubId) VALUES (?,?)", new Object[] { book.getTitle(), book.getPubId() });
	}
	*/
	
	public void updateBookPublisher(Book book) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book SET pubId = ? where bookId = ?", new Object[] {book.getPubId(), book.getBookId()});
	}
	
	public void updateBook(Book book) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book SET title = (?) WHERE bookId = ?",
				new Object[] { book.getTitle(), book.getBookId() });
	}

	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_book WHERE bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readAllBooks() throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book", this);
	}
	
	public boolean checkIfBookInBranch(Branch branch, Book book) throws SQLException, ClassNotFoundException {
		if(jdbcTemplate.query("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and bookId = ?)",
				new Object[] {branch.getBranchId(), book.getBookId()}, this).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<Book> readAllBranchBooks(Branch branch) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ?)",
				new Object[] {branch.getBranchId()}, this);
	}
		
	public int checkBookPublisherDependency(Publisher publisher) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book b WHERE 1 <= "
				+ "(SELECT COUNT(*) FROM tbl_book b2 WHERE b2.bookId = b.bookId and EXISTS ("
				+ "SELECT * FROM tbl_book b3 WHERE b2.bookId = b3.bookId and b3.pubId = ?))",
				new Object[] {publisher.getPublisherId()}, this).size();
	}
	
	public int checkBookSingleAuthor(Author author) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book b WHERE 1 <= "
				+ "(SELECT COUNT(*) FROM tbl_book_authors ba WHERE ba.bookId = b.bookId and EXISTS ("
				+ "SELECT * FROM tbl_book_authors ba2 WHERE ba.bookId = ba2.bookId and ba2.authorId = ?))",
				new Object[] {author.getAuthorId()}, this).size();
	}
	
	public int checkBookSingleGenre(Genre genre) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book b WHERE 1 <= "
				+ "(SELECT COUNT(*) FROM tbl_book_genres bg WHERE bg.bookId = b.bookId and EXISTS ("
				+ "SELECT * FROM tbl_book_genres bg2 WHERE bg.bookId = bg2.bookId and bg2.genreId = ?))",
				new Object[] {genre.getGenreId()}, this).size();
	}
	
	public List<Book> readAllBooksByName(String searchString) throws SQLException, ClassNotFoundException {
		searchString = "%"+searchString+"%";
		return jdbcTemplate.query("SELECT * FROM tbl_book WHERE title LIKE ?", 
				new Object[] {searchString}, this);
	}
	public List<Book> readBranchBooks(int branchId) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and noOfCopies > 0)", 
				new Object[] {branchId}, this);
	}
	
	
	public List<Book> readAllBranchBooksByName(int branchId, String searchString) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and noOfCopies > 0 and b.title = (?))", 
				new Object[] {branchId, searchString}, this);
	}
	
	public List<Book> readAvailableBranchBooks(int branchId) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book b WHERE EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and noOfCopies > "
				+ "(SELECT COUNT(*) FROM tbl_book_loans bl WHERE bl.bookId = b.bookId and bl.branchId = ? and dateIn IS NULL))",
				new Object[] {branchId, branchId},this); 
	}
	
	public List<Book> readAvailableBranchBooksByName(int branchId, String searchString) throws SQLException, ClassNotFoundException {
		searchString = "%" + searchString + "%";
		return jdbcTemplate.query("SELECT * FROM tbl_book b WHERE b.title LIKE (?) and EXISTS (SELECT * FROM tbl_book_copies WHERE bookId = b.bookId and branchId = ? and noOfCopies > "
				+ "(SELECT COUNT(*) FROM tbl_book_loans bl WHERE bl.bookId = b.bookId and bl.branchId = ? and dateIn IS NULL))",
				new Object[] {searchString, branchId,  branchId},this); 
	}


		
	public List<Book> readAllLoanedBooks(int cardNo) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book b "
				+ "WHERE EXISTS ( "
						+ "SELECT * " 
						+ "FROM tbl_book_loans bl " 
						+ " WHERE b.bookId = bl.bookId and bl.cardNo = ? and bl.dateIn IS NULL)", 
						new Object[] {cardNo}, this);
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		AuthorDAO adao = new AuthorDAO();
		while (rs.next()) {
			Book b = new Book(rs.getInt("bookId"), rs.getString("title"), rs.getInt("pubId"));
			try {
				b.setAuthors(adao.findBookAuthors(b.getBookId()));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			books.add(b);
		}
		return books;
	}
}