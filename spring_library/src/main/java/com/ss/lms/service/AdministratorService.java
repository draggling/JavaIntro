package com.ss.lms.service;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.ss.lms.crud.CRUDAuthor;
import com.ss.lms.crud.CRUDBook;
import com.ss.lms.crud.CRUDBookCopies;
import com.ss.lms.crud.CRUDBorrower;
import com.ss.lms.crud.CRUDBranch;
import com.ss.lms.crud.CRUDGenre;
import com.ss.lms.crud.CRUDPublisher;
import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BookCopiesDAO;
import com.ss.lms.dao.GenreDAO;
import com.ss.lms.dao.LoanDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.BorrowerDAO;
import com.ss.lms.dao.BranchDAO;
import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;
public class AdministratorService {
	
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
	
	LibrarianService LS = new LibrarianService();
	public ConnectionUtil conUtil = new ConnectionUtil();
	Scanner scanner = new Scanner(System.in);
	CRUDBook CBook = new CRUDBook();
	CRUDAuthor CAuthor = new CRUDAuthor();
	CRUDBookCopies CBookCopies = new CRUDBookCopies();
	CRUDBorrower CBorrower = new CRUDBorrower();
	CRUDBranch CBranch = new CRUDBranch();
	CRUDGenre CGenre = new CRUDGenre();
	CRUDPublisher CPublisher = new CRUDPublisher();

	/* BOOK FUNCTIONS */
	public void addBook() throws ClassNotFoundException, SQLException {CBook.addBook();}
	public Book updateBook() throws ClassNotFoundException, SQLException {return CBook.updateBook();}
	public void deleteBook() throws ClassNotFoundException {CBook.deleteBook();}

	/* AUTHOR FUNCTIONS */
	public void addAuthor() {CAuthor.addAuthor();}
	public void updateAuthor() {CAuthor.updateAuthor();}
	public void deleteAuthor() {CAuthor.deleteAuthor();}
	public void readAuthors() {CAuthor.readAuthors();}
	
	/* GENRE FUNCTIONS */
	public void addGenre() {CGenre.addGenre();}
	public void updateGenre() {CGenre.updateGenre();}
	public void deleteGenre() {CGenre.deleteGenre();}
	public void readGenres() {CGenre.readGenres();}


	/* PUBLISHER FUNCTIONS */
	public void addPublisher() {CPublisher.addPublisher();}
	public void updatePublisher() {CPublisher.updatePublisher();}
	public void deletePublisher() {CPublisher.deletePublisher();}
	public void readPublishers() {CPublisher.readPublishers();}

	/* BRANCH FUNCTIONS */
	public void addBranch() {CBranch.addBranch();}
	public void updateBranch() {CBranch.updateBranch();}
	public void deleteBranch() {CBranch.deleteBranch();}
	public void readBranches() {CBranch.readBranches();}

	/* BORROWER FUNCTIONS */
	public void addBorrower() {CBorrower.addBorrower();}
	public void updateBorrower() {CBorrower.updateBorrower();}
	public void deleteBorrower() {CBorrower.deleteBorrower();}
	public void readBorrowers() {CBorrower.readBorrowers();}
	/* OVERRIDE DUE DATE */
	public void overrideDueDate() {CBookCopies.overrideDueDate();}
	
	/* HELPER BOOK FUNCTIONS */
	/* returns a book */
	public Book chooseBook() {
		try(Connection conn = conUtil.getConnection()) {
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
					System.out.println("Pick the Book you want to add copies of to your branch:");
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
	/* get book or get all books */
	public List<Book> getBooks(String searchString) {
		try{
			//BookDAO bdao = new BookDAO(conn);
			if (searchString != null) {
				return bdao.readAllBooksByName(searchString);
			} else {
				return bdao.readAllBooks();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}