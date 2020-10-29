package com.ss.lms.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.GenreDAO;
import com.ss.lms.dao.LoanDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Loan;
import com.ss.lms.entity.Publisher;
import com.ss.lms.service.ConnectionUtil;

public class CRUDBookCopies {
	
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
	
	public ConnectionUtil conUtil = new ConnectionUtil();
	Scanner scanner = new Scanner(System.in);
	public void overrideDueDate() {
		int value = -1;
		System.out.println("Printing all loans currently out");
   		try (Connection conn = conUtil.getConnection()){
   			//LoanDAO ldao = new LoanDAO(conn);
   			List<Loan> Loans = ldao.readAllActiveLoans();
   			int counter = 1;
   			if(Loans.size() == 0) {
   				System.out.println("No active loans. Returning...");
   				return;
   			} else {
   				for(Loan l : Loans) {
   					System.out.println(counter + ")\n" + l.toString());
   					counter++;
   				}
   				System.out.println(counter + ") Return");
   				int choice = 0;
   				while (choice < 1 || choice > counter) {
   					choice = scanner.nextInt();
   					scanner.nextLine();
   					if(choice == counter) {
   						System.out.println("returning");
   						return;
   					} else if(choice < 1 || choice > counter) {
   						System.out.println("Invalid choice");
   					} else {
   						Loan currentLoan = Loans.get(choice-1);
   			   			//LoanDAO ldao2 = new LoanDAO(conn);
   			   			try {
			   				System.out.println("Your loan dueDate " + currentLoan.getDueDate());
   			   				while(value < 0) {
   			   					System.out.print("How many days would you like to extend your due date? ");
   			   					value = scanner.nextInt();
   			   					scanner.nextLine();
   			   				}
   			   			} catch(InputMismatchException e) {
   			   				System.out.println("Please input an integer greater than or equal to 0");
   			   			}
   						//ldao2.extendLoan(currentLoan, value);
   			   			ldao.extendLoan(currentLoan, value);
   						System.out.println("Loan due date extended by " + value + " days");
   					}
   				}
   			}
   		} catch (ClassNotFoundException | SQLException| InputMismatchException e) {
			scanner.nextLine();
			return;
   		}
	}
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
		try (Connection conn = conUtil.getConnection()){
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
}
