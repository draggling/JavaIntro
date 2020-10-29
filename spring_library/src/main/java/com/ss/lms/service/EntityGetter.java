package com.ss.lms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BookCopiesDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.BorrowerDAO;
import com.ss.lms.dao.BranchDAO;
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

/* Entity getters
 * Used for print statements and data retrieval
 */
public class EntityGetter {
	
	@Autowired
	public LoanDAO ldao;
	
	@Autowired
	public BookDAO bdao;
	
	@Autowired
	public PublisherDAO pdao;
	
	@Autowired
	public AuthorDAO adao;
	
	@Autowired
	public GenreDAO gdao;	
	
	@Autowired
	public BorrowerDAO bodao;	
	
	@Autowired
	public BranchDAO brdao;
	
	@Autowired
	public BookCopiesDAO bcdao;
	
	Scanner scanner = new Scanner(System.in);
	
	
	public Book readBook(Book book) throws ClassNotFoundException {
		try {
			/*
			PublisherDAO pdao = new PublisherDAO(conn);
			AuthorDAO adao = new AuthorDAO(conn);
			GenreDAO gdao = new GenreDAO(conn);
			*/
			
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
	
	public Book chooseBook() {
		try {
			while(true) {
				System.out.print("Enter title of book: ");
				String searchString = scanner.nextLine();
				//BookDAO bdao = new BookDAO(conn);
				List<Book> book = bdao.readAllBooksByName(searchString);
				if(book.size() == 0) {
					System.out.println("Book not found");
				} else if(book.size() == 1) {
					return readBook((book.get(0)));
				} else {
					System.out.println("Choose a book:");
					List<Book> branchBooks = new ArrayList<>();
					branchBooks = bdao.readAllBooks();
					int counter = 1;
					for(Book b : branchBooks) {
						System.out.println(counter + ") " + readBook(b).toString());
						counter++;
					}
					while(true) {
						try {
							System.out.print("Choose: ");
							int input = scanner.nextInt();
							scanner.nextLine();
							if(input > 0 && input < counter) {
								return readBook(branchBooks.get(input - 1));
							} else {
								System.out.println("Invalid input");
							}
						} catch (InputMismatchException e) {
							System.out.println("Error: not an integer");
							scanner.nextLine();
						}
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* returns all active loans for a specific card number */
	public List<Loan> findLoans(Integer cardNo) {
		try {
			//LoanDAO lDAO = new LoanDAO(conn);
			return ldao.readAllActiveBorrowerLoans(cardNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Loan findActiveLoan(Integer bookId, Integer branchId, Integer cardNo) {
		try {
			//LoanDAO lDAO = new LoanDAO(conn);
			return ldao.getLoan(bookId, branchId, cardNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Borrower findBorrower(Integer cardNo) {
		try {
			//BorrowerDAO borDAO = new BorrowerDAO(conn);
			return bodao.getBorrower(cardNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Book findBook(Integer bookId) {
		try {
			//BookDAO bDAO = new BookDAO(conn);
			return bdao.getBook(bookId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Branch findBranch(Integer branchId) {
		try {
			//BranchDAO brDAO = new BranchDAO(conn);
			return brdao.getBranch(branchId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}