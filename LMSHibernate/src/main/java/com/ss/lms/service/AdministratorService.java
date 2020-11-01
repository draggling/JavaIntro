package com.ss.lms.service;

import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookGenres;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;
import com.ss.lms.repo.AuthorRepo;
import com.ss.lms.repo.BookAuthorsRepo;
import com.ss.lms.repo.BookCopiesRepo;
import com.ss.lms.repo.BookGenresRepo;
import com.ss.lms.repo.BookRepo;
import com.ss.lms.repo.BorrowerRepo;
import com.ss.lms.repo.BranchRepo;
import com.ss.lms.repo.GenreRepo;
import com.ss.lms.repo.LoanRepo;
import com.ss.lms.repo.PublisherRepo;

@RestController
public class AdministratorService {

	@Autowired
	BookRepo brepo;
	
	@Autowired
	AuthorRepo arepo;
	
	@Autowired
	PublisherRepo prepo;	
	
	@Autowired
	GenreRepo grepo;
	
	@Autowired
	BookGenresRepo bgrepo;
	
	@Autowired
	BookAuthorsRepo barepo;
	
	@Autowired
	BranchRepo brrepo;
	
	@Autowired
	BorrowerRepo borepo;
	
	@Autowired
	LoanRepo lrepo;
	
	@Autowired
	BookCopiesRepo bcrepo;
	 
	/* LIBRARY FUNCTIONS */
	
	/* add book copies to a branch */
	@Transactional
	@RequestMapping(value = "/addBookCopies", method = RequestMethod.PUT)
	public String addBookCopies(@RequestParam Integer bookId, @RequestParam Integer branchId, @RequestParam Integer noOfCopies) throws SQLException{
		if(noOfCopies <= 0) {
			return "Invalid noOfCopies";
		} else if(!brepo.existsById(bookId)) {
			return "Book with id: " + bookId + " does not exist";
		} else if(!brrepo.existsById(branchId)) {
			return "Branch with id: " + branchId + " does not exist";
		} else if(bcrepo.bookCopiesExist(bookId, branchId).size() == 0) {
			/* add new row to book copies table */
			bcrepo.addNewBookCopies(bookId, branchId, noOfCopies);
			return "new book copies added for bookId: " + bookId + " in branchId: " + branchId +
					"\nNew number of copies " + noOfCopies;
		} else {
			int currentcopies = bcrepo.bookCopiesExist(bookId, branchId).get(0).getNoOfCopies();
			bcrepo.addExistingBookCopies(bookId, branchId, noOfCopies);
			return "Former number of copies " + currentcopies + " copies of bookId:" + bookId + " in branch: " + branchId +
					"\nNew number of copies " + (currentcopies + noOfCopies);
		}
	}
	
	/* ADMIN FUNCTIONS */
	/* book functions */
	
	/* read all books */
	@RequestMapping(value = "/getBooks", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getBooks(){
		return brepo.findAll();
	}
	/* get books by title */
	@RequestMapping(value = "/getBooksByQuery", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getBooksByQuery(@RequestParam String searchString) throws SQLException{
		List<Book> books = new ArrayList<>();
		if (searchString != null && searchString.length() > 0 && searchString.length() <= 45) {
				books = brepo.readBooksByTitle(searchString);
		} else {
				books = brepo.findAll();
		}
		return books;
	}
	/* add book BROKEN */
	@Transactional
	@RequestMapping(value = "/addBook", method = RequestMethod.POST	)
	public String addBook(@RequestParam String title, @RequestParam Integer publisherId) throws SQLException {
		//System.out.println("addBook start");
		if (title == null || title.length() > 45) {
			//System.out.println("Book Title cannot be empty and should be 45 char in length");
			return "Book Title cannot be empty and should be 45 char in length";
		} else {
			if(prepo.publisherExists(publisherId).size() == 0) {
				//System.out.println("Error: Publisher does not exist");
				return "Error: Publisher does not exist";
			} else {
				brepo.addBook(title, publisherId);
				brepo.flush();
				//System.out.println(");
				return("Book successfully added");
			}
		}
	}
	
	/* update book name */
	@Transactional
	@RequestMapping(value = "/updateBookTitle", method = RequestMethod.PUT)
	public String updateBookTitle(@RequestParam Integer bookId, @RequestParam String newTitle) throws SQLException{
		if (newTitle == null || newTitle.length() > 45) {
			return "invalid title";
		} else {
			if(brepo.readBooksByBookId(bookId).size() == 0) {
				return "no such book with Id = "+ bookId + " exists";
			} else {
				brepo.updateBookTitle(bookId, newTitle);
				return "update successful";
			}
		}
	}
	
	/* delete book */
	@Transactional
	@RequestMapping(value = "/deleteBook", method = RequestMethod.DELETE)
	public String deleteBook(@RequestParam Integer bookId) throws SQLException{
		if(brepo.readBooksByBookId(bookId).size() == 0) {
			return "No such book exists with id = " + bookId;
		} else {
			brepo.deleteBook(bookId);
			return "Book deleted";
		}
	}
	
	/* update book publisher */
	@Transactional
	@RequestMapping(value = "/updateBookPublisher", method = RequestMethod.PUT)
	public String updateBookPublisher(
			@RequestParam Integer bookId, @RequestParam Integer publisherId) throws SQLException {
		if(prepo.publisherExists(publisherId).size() == 0) {
			return "Publisher does not exist with id = " + publisherId;
		} else {
			brepo.updateBookPublisher(bookId, publisherId);
			return "Publisher updated";
		}
	}
	/* add book genre */
	@Transactional
	@RequestMapping(value = "/addBookGenre", method = RequestMethod.POST)
	public String addBookGenre(
			@RequestParam Integer bookId, @RequestParam Integer genreId) throws SQLException {
		if(brepo.readBooksByBookId(bookId).size() == 0) {
			return "bookId of " + bookId + " does not exist";
		} else if (!grepo.existsById(genreId)) {
			return "genreId of " + genreId + " does not exist";
		} else if(bgrepo.readBookGenrePair(bookId, genreId).size() == 1) {
			return "the genre id is already associated with this book";
		} else {
			bgrepo.addBookGenre(bookId, genreId);
			return("Genre id = " + genreId + " added to book id = " + bookId);
		}
	}
	/* remove book genre */
	@Transactional
	@RequestMapping(value = "/deleteBookGenre", method = RequestMethod.DELETE)
	public String deleteBookGenre(
			@RequestParam Integer bookId, @RequestParam Integer genreId) throws SQLException {
		if(brepo.readBooksByBookId(bookId).size() == 0) {
			return "bookId of " + bookId + " does not exist";
		} else if (!grepo.existsById(genreId)) {
			return "genreId of " + genreId + " does not exist";
		} else if(bgrepo.readBookGenrePair(bookId, genreId).size() == 1) {
			bgrepo.deleteBookGenre(bookId, genreId);
			return "genre removed from book";
		} else {
			return "the genre id is not associated with this book";
		}
	}	

	
	/* read book genres test */
	@RequestMapping(value = "/readBookGenres", method = RequestMethod.GET, produces = "application/json")
	public List<BookGenres> readtBookGenres(){
		return bgrepo.readBookGenres();
	}
	
	/* read book genre pair */
	@RequestMapping(value = "/readBookGenrePair", method = RequestMethod.GET)
	public String getBookGenrePair(@RequestParam Integer bookId, @RequestParam Integer genreId) {
		if(bgrepo.readBookGenrePair(bookId, genreId).size() == 1) {
			return "book-genre pair exists";	
		} else {
			return "book-genre pair does not exist";
		}
	}
	
	@Transactional
	@RequestMapping(value = "/addBookAuthor", method = RequestMethod.POST)
	public String addBookAuthor(
			@RequestParam Integer bookId, @RequestParam Integer authorId) throws SQLException {
		if(brepo.readBooksByBookId(bookId).size() == 0) {
			return "bookId of " + bookId + " does not exist";
		} else if (!arepo.existsById(authorId)) {
			return "authorId of " + authorId + " does not exist";
		} else if(barepo.readBookAuthorPair(bookId, authorId).size() == 1) {
			return "the author id is already associated with this book";
		} else {
			barepo.addBookAuthor(bookId, authorId);
			return("Author id = " + authorId + " added to book id = " + bookId);
		}
	}
	/* remove author author */
	@Transactional
	@RequestMapping(value = "/deleteBookAuthor", method = RequestMethod.DELETE)
	public String deleteBookAuthor(
			@RequestParam Integer bookId, @RequestParam Integer authorId) throws SQLException {
		if(brepo.readBooksByBookId(bookId).size() == 0) {
			return "bookId of " + bookId + " does not exist";
		} else if (!arepo.existsById(authorId)) {
			return "authorId of " + authorId + " does not exist";
		} else if(barepo.readBookAuthorPair(bookId, authorId).size() == 1) {
			barepo.deleteBookAuthor(bookId, authorId);
			return "author removed from book";
		} else {
			return "the author id is not associated with this book";
		}
	}
	/* Add Author */
	@Transactional
	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST)
	public String addAuthor(@RequestParam String authorName) throws SQLException {
		arepo.addAuthor(authorName);
		return("Author added");
	}
	/* Read Authors */
	@RequestMapping(value = "/readAuthors", method = RequestMethod.GET)
	public List<Author> readAuthors() throws SQLException {
		return arepo.readAuthors();
	}
	/* Update Author */
	@Transactional
	@RequestMapping(value = "/updateAuthor", method = RequestMethod.PUT)
	public String updateAuthor(@RequestParam Integer authorId, @RequestParam String authorName) throws SQLException {
		if(arepo.existsById(authorId)) {
			arepo.updateAuthor(authorId, authorName);
			return "Author name updated";
		} else {
			return "Author with id = " + authorId + " does not exist";
		}
	}
	/* Delete Author */
	@Transactional
	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.DELETE)
	public String deleteAuthor(@RequestParam Integer authorId) throws SQLException {
		if(arepo.existsById(authorId)) {
			arepo.deleteAuthor(authorId);
			return "Author deleted";
		} else {
			return "Author with id = " + authorId + " does not exist";
		}
	}
	
	/* Add Genre */
	@Transactional
	@RequestMapping(value = "/addGenre", method = RequestMethod.POST)
	public String addGenre(@RequestParam String genreName) throws SQLException {
		grepo.addGenre(genreName);
		return("Genre added");
	}
	/* Read Genres */
	@RequestMapping(value = "/readGenres", method = RequestMethod.GET)
	public List<Genre> readGenres() throws SQLException {
		return grepo.readGenres();
	}
	/* Update Genres */
	@Transactional
	@RequestMapping(value = "/updateGenre", method = RequestMethod.PUT)
	public String updateGenre(@RequestParam Integer genreId, @RequestParam String genreName) throws SQLException {
		if(grepo.existsById(genreId)) {
			grepo.updateGenre(genreId, genreName);
			return "Genre name updated";
		} else {
			return "Genre with id = " + genreId + " does not exist";
		}
	}

	/* Delete Genres */
	@Transactional
	@RequestMapping(value = "/deleteGenre", method = RequestMethod.DELETE)
	public String deleteGenre(@RequestParam Integer genreId) throws SQLException {
		if(grepo.existsById(genreId)) {
			grepo.deleteGenre(genreId);
			return "Genre deleted";
		} else {
			return "Genre with id = " + genreId + " does not exist";
		}
	}

	
	/* Add Publisher */
	@Transactional
	@RequestMapping(value = "/addPublisher", method = RequestMethod.POST)
	public String addPublisher(@RequestParam String publisherName, 
			@RequestParam String publisherAddress, @RequestParam String publisherPhone) throws SQLException {
		prepo.addPublisher(publisherName, publisherAddress, publisherPhone);
		return("Publisher added");
	}
	/* Read Publishers */
	@RequestMapping(value = "/readPublishers", method = RequestMethod.GET)
	public List<Publisher> readPublishers() throws SQLException {
		return prepo.readPublishers();
	}
	/* Update Publishers */
	@Transactional
	@RequestMapping(value = "/updatePublisher", method = RequestMethod.PUT)
	public String updatePublisher(@RequestParam Integer publisherId, 
			@RequestParam String publisherName, @RequestParam String publisherAddress, @RequestParam String publisherPhone) throws SQLException {
		if(prepo.existsById(publisherId)) {
			prepo.updatePublisher(publisherId, publisherName, publisherAddress, publisherPhone);
			return "Publisher name updated";
		} else {
			return "Publisher with id = " + publisherId + " does not exist";
		}
	}

	/* Delete Publishers */
	@Transactional
	@RequestMapping(value = "/deletePublisher", method = RequestMethod.DELETE)
	public String deletePublisher(@RequestParam Integer publisherId) throws SQLException {
		if(prepo.existsById(publisherId)) {
			prepo.deletePublisher(publisherId);
			return "Publisher deleted";
		} else {
			return "Publisher with id = " + publisherId + " does not exist";
		}
	}
	/* Add Branch */
	@Transactional
	@RequestMapping(value = "/addBranch", method = RequestMethod.POST)
	public String addBranch(@RequestParam String branchName, 
			@RequestParam String branchAddress) throws SQLException {
		brrepo.addBranch(branchName, branchAddress);
		return("Branch added");
	}
	/* Read Branches */
	@RequestMapping(value = "/readBranches", method = RequestMethod.GET)
	public List<Branch> readBranches() throws SQLException {
		return brrepo.readBranches();
	}
	/* Update Branches */
	@Transactional
	@RequestMapping(value = "/updateBranch", method = RequestMethod.PUT)
	public String updateBranch(@RequestParam Integer branchId, 
			@RequestParam String branchName, @RequestParam String branchAddress) throws SQLException {
		if(brrepo.existsById(branchId)) {
			brrepo.updateBranch(branchId, branchName, branchAddress);
			return "Branch name updated";
		} else {
			return "Branch with id = " + branchId + " does not exist";
		}
	}

	/* Delete Branches */
	@Transactional
	@RequestMapping(value = "/deleteBranch", method = RequestMethod.DELETE)
	public String deleteBranch(@RequestParam Integer branchId) throws SQLException {
		if(brrepo.existsById(branchId)) {
			brrepo.deleteBranch(branchId);
			return "Branch deleted";
		} else {
			return "Branch with id = " + branchId + " does not exist";
		}
	}
	
	/* Add Borrower */
	@Transactional
	@RequestMapping(value = "/addBorrower", method = RequestMethod.POST)
	public String addBorrower(@RequestParam String name, 
			@RequestParam String address, @RequestParam String phone) throws SQLException {
		borepo.addBorrower(name, address, phone);
		return("Borrower added");
	}
	/* Read Borrowers */
	@RequestMapping(value = "/readBorrowers", method = RequestMethod.GET)
	public List<Borrower> readBorrowers() throws SQLException {
		return borepo.readBorrowers();
	}
	/* Update Borrowers */
	@Transactional
	@RequestMapping(value = "/updateBorrower", method = RequestMethod.PUT)
	public String updateBorrower(@RequestParam Integer cardNo, 
			@RequestParam String name, @RequestParam String address, @RequestParam String phone) throws SQLException {
		if(borepo.existsById(cardNo)) {
			borepo.updateBorrower(cardNo, name, address, phone);
			return "Borrower name updated";
		} else {
			return "Borrower with card number = " + cardNo + " does not exist";
		}
	}

	/* Delete Borrowers */
	@Transactional
	@RequestMapping(value = "/deleteBorrower", method = RequestMethod.DELETE)
	public String deleteBorrower(@RequestParam Integer cardNo) throws SQLException {
		if(borepo.existsById(cardNo)) {
			borepo.deleteBorrower(cardNo);
			return "Borrower deleted";
		} else {
			return "Borrower with card number = " + cardNo + " does not exist";
		}
	}
	
	/* Override Due Date of Loan */
	@Transactional
	@RequestMapping(value = "/overrideDueDate", method = RequestMethod.PUT)
	public String overrideDueDate(@RequestParam Integer cardNo, @RequestParam Integer bookId,
			@RequestParam Integer branchId, @RequestParam Integer days) throws SQLException {
		if(lrepo.loanExists(cardNo, bookId, branchId).size() == 0) {
			return "loan for cardNo: " + cardNo + " in branch: " + branchId + " does not exist for book: " + bookId;
		} else if(lrepo.bookCheckedOut(cardNo, bookId, branchId).size() == 0) {
			return "book is already checked in ";
		} else {
			lrepo.overrideDueDate(cardNo, bookId, branchId, days);
			if(days == 1) {
				return "book date extended by " + days + " day";
 			} else {
 				return "book date extended by " + days + " days";
 			}
		}
	}
	
	/* i was receiving errors creating 2 separate hibernate projects for administrator and borrower services */
	/* borrower functions */
	
	/* check out a book */
	@Transactional
	@RequestMapping(value = "/checkOutBook", method = RequestMethod.PUT)
	public String checkOutBook(@RequestParam Integer cardNo, @RequestParam Integer bookId,
			@RequestParam Integer branchId) throws SQLException {
		if (bcrepo.bookCopiesExist(bookId, branchId).get(0).getNoOfCopies() == 0) {
			return "there are no copies of book " + bookId + "in branch " + branchId;
		}else if(lrepo.loanExists(cardNo, bookId, branchId).size() != 0) {
			return "loan for cardNo: " + cardNo + " in branch: " + branchId + " alread exists for book: " + bookId;	
		} else if(bcrepo.bookCopiesExist(bookId, branchId).get(0).getNoOfCopies() <= lrepo.bookBranchLoans(bookId, branchId).size()) {
			return "all copies of book + " +bookId+ " in branch " +branchId+ " are currently checked out";
		} else if(lrepo.loanExpired(cardNo, bookId, branchId).size() == 0){
			/* row already exists but it is expired */
			lrepo.addLoan(cardNo, bookId, branchId);
			return "book checked out";
		} else {
			lrepo.updateLoan(cardNo, bookId, branchId);
			return "old loan exists: loan updated and book checked out again";
		}
	}
	
	/* check in a book */
	@Transactional
	@RequestMapping(value = "/checkInBook", method = RequestMethod.PUT)
	public String checkInBook(@RequestParam Integer cardNo, @RequestParam Integer bookId,
			@RequestParam Integer branchId) throws SQLException {
		if (bcrepo.bookCopiesExist(bookId, branchId).get(0).getNoOfCopies() == 0) {
			return "there are no loans of book " + bookId + "in branch " + branchId;
		}else if(lrepo.loanExpired(cardNo, bookId, branchId).size() != 0) {
			lrepo.updateLoan(cardNo, bookId, branchId);
			return "loan for cardNo: " + cardNo + " in branch: " + branchId + " already checked in for book: " + bookId;	
		} else {
			lrepo.checkInBook(cardNo, bookId, branchId);
			return "book checked in";
		}
	}
}
