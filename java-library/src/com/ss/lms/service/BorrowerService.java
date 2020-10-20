package com.ss.lms.service;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.BorrowerDAO;
import com.ss.lms.dao.GenreDAO;
import com.ss.lms.dao.LoanDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Loan;
import com.ss.lms.entity.Publisher;

public class BorrowerService {
	public ConnectionUtil conUtil = new ConnectionUtil();
	Scanner scanner = new Scanner(System.in);
	
	public Book readBook(Book book) throws ClassNotFoundException {
		try (Connection conn = conUtil.getConnection()){
			PublisherDAO pdao = new PublisherDAO(conn);
			AuthorDAO adao = new AuthorDAO(conn);
			GenreDAO gdao = new GenreDAO(conn);
			/* retrieve publisher, author(s), and genre(s) */
			List<Publisher> tempPub = pdao.getPublishersByBook(book.getBookId());
			if(tempPub.size() == 1) {
				book.setPublisher(tempPub.get(0));
			}
			List<Author> authors = adao.findBookAuthors(book.getBookId());
			List<Genre> genres = gdao.findBookGenres(book.getBookId());
			book.setAuthors(authors);
			book.setGenres(genres);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
	}
	
	public Borrower ValidateCard(int cardNo) {
		try(Connection conn = conUtil.getConnection()) {
			BorrowerDAO borDAO = new BorrowerDAO(conn);
			List<Borrower> userList = borDAO.searchBorrower(cardNo);
			if(userList.size() == 0) {
				return null;
			} else {
				Borrower user = userList.get(0);
				return user;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Book> ReturnBranchBooks(Branch branch) {
		int branchId = branch.getBranchId();
		try(Connection conn = conUtil.getConnection()) {
			BookDAO bDAO = new BookDAO(conn);
			return bDAO.readBranchBooks(branchId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Loan> readAllActiveBorrowerLoans(Borrower borrower) {
		try(Connection conn = conUtil.getConnection()) {
			LoanDAO LDAO = new LoanDAO(conn);
			return LDAO.readAllActiveBorrowerLoans(borrower.getCardNo());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean checkBorrowerLoans(Borrower borrower, Book book) {
		for(Loan b: borrower.getLoans()) {
			if(b.getBookId() == book.getBookId()) {
				return true;
			}
		}
		return false;
	}
	
	public Book searchBranchAvailableBooks(Integer cardNo, Integer branchId) {
		try(Connection conn = conUtil.getConnection()) {
			System.out.print("Enter title of book: ");
			String searchString = scanner.nextLine();
			BookDAO bDAO = new BookDAO(conn);
			List<Book> book = bDAO.readAvailableBranchBooksByName(branchId, searchString);

			if(book.size() == 0) {
				System.out.println("Book not found");
				return null;
			} else if(book.size() == 1) {
				return readBook(book.get(0));
			} else {
				System.err.println("ERROR: multiple books with the same title found");
				return null;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void checkOutBook(Borrower borrower, Branch branch, Book book) {
		Loan loan = new Loan(book.getBookId(), branch.getBranchId(), borrower.getCardNo());
		try(Connection conn = conUtil.getConnection()) {
			LoanDAO LDAO = new LoanDAO(conn);
			LDAO.checkOutBook(loan);
			System.out.println("loan issued");
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	public void checkInBook(Loan loan) {
		try(Connection conn = conUtil.getConnection()) {
			LoanDAO LDAO = new LoanDAO(conn);
			LDAO.checkInBook(loan);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}
}
