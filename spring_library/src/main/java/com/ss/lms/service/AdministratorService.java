package com.ss.lms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
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
	
	@Autowired
	LibrarianService LS = new LibrarianService();
	Scanner scanner = new Scanner(System.in);
	CRUDBook CBook = new CRUDBook();
	CRUDAuthor CAuthor = new CRUDAuthor();
	CRUDBookCopies CBookCopies = new CRUDBookCopies();
	CRUDBorrower CBorrower = new CRUDBorrower();
	CRUDBranch CBranch = new CRUDBranch();
	CRUDGenre CGenre = new CRUDGenre();
	CRUDPublisher CPublisher = new CRUDPublisher();

	/* BOOK FUNCTIONS */
	@Transactional
	@RequestMapping(value = "/addBook", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addBook() throws ClassNotFoundException, SQLException {CBook.addBook();}
	@Transactional
	@RequestMapping(value = "/updateBook", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public Book updateBook() throws ClassNotFoundException, SQLException {return CBook.updateBook();}
	@RequestMapping(value = "/deleteBook", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	public void deleteBook() throws ClassNotFoundException {CBook.deleteBook();}

	/* AUTHOR FUNCTIONS */
	@Transactional
	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addAuthor() {CAuthor.addAuthor();}
	@Transactional
	@RequestMapping(value = "/updateAuthor", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public void updateAuthor() {CAuthor.updateAuthor();}
	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	public void deleteAuthor() {CAuthor.deleteAuthor();}
	@RequestMapping(value = "/readAuthors", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
	public void readAuthors() {CAuthor.readAuthors();}
	
	/* GENRE FUNCTIONS */
	@Transactional
	@RequestMapping(value = "/addGenre", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addGenre() {CGenre.addGenre();}
	@Transactional
	@RequestMapping(value = "/updateGenre", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public void updateGenre() {CGenre.updateGenre();}
	@RequestMapping(value = "/deleteGenre", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	public void deleteGenre() {CGenre.deleteGenre();}
	@RequestMapping(value = "/readGenres", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
	public void readGenres() {CGenre.readGenres();}


	/* PUBLISHER FUNCTIONS */
	@Transactional
	@RequestMapping(value = "/addPublisher", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addPublisher() {CPublisher.addPublisher();}
	@Transactional
	@RequestMapping(value = "/updatePublisher", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public void updatePublisher() {CPublisher.updatePublisher();}
	@RequestMapping(value = "/deletePublisher", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	public void deletePublisher() {CPublisher.deletePublisher();}
	@RequestMapping(value = "/readPublishers", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
	public void readPublishers() {CPublisher.readPublishers();}

	/* BRANCH FUNCTIONS */
	@Transactional
	@RequestMapping(value = "/addBranch", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addBranch() {CBranch.addBranch();}
	@Transactional
	@RequestMapping(value = "/updateBranch", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public void updateBranch() {CBranch.updateBranch();}
	@RequestMapping(value = "/deleteBranch", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	public void deleteBranch() {CBranch.deleteBranch();}
	@RequestMapping(value = "/readBranches", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
	public void readBranches() {CBranch.readBranches();}

	/* BORROWER FUNCTIONS */
	@Transactional
	@RequestMapping(value = "/addBorrower", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addBorrower() {CBorrower.addBorrower();}
	@Transactional
	@RequestMapping(value = "/updateBorrower", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public void updateBorrower() {CBorrower.updateBorrower();}
	@RequestMapping(value = "/deleteBorrower", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	public void deleteBorrower() {CBorrower.deleteBorrower();}
	@RequestMapping(value = "/readBorrowers", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
	public void readBorrowers() {CBorrower.readBorrowers();}
	
	/* OVERRIDE DUE DATE */
	@Transactional
	@RequestMapping(value = "/overrideDueDate", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public void overrideDueDate() {CBookCopies.overrideDueDate();}
	
	/* HELPER BOOK FUNCTIONS */
	/* returns a book */
	
	@RequestMapping(value = "/chooseBook", method = RequestMethod.GET, produces = "application/json")
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
	
	@RequestMapping(value = "/readBook", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
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
	@RequestMapping(value = "/getBooks", method = RequestMethod.GET, produces = "application/json"	)
	public List<Book> getBooks(@PathVariable String searchString) {
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