package com.ss.lms.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;
import com.ss.lms.service.ConnectionUtil;

public class CRUDBorrower {
	public ConnectionUtil conUtil = new ConnectionUtil();
	Scanner scanner = new Scanner(System.in);
	
	public void addBorrower() {
		String borrowerName = "";
		String borrowerAddress = "";
		String borrowerPhone = "";
   		try (Connection conn = conUtil.getConnection()){
   			BorrowerDAO bodao = new BorrowerDAO(conn);
   			/* get borrower name */
   			while(borrowerName.isEmpty() || borrowerName.length() > 45) {
   	   			System.out.print("New borrower name:");
   	   			borrowerName = scanner.nextLine();
   	   			if(borrowerName.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			}
   	   		}
   			/* get borrower address */
   			while(borrowerAddress.isEmpty() || borrowerAddress.length() > 45) {
   	   			System.out.print("New borrower address:");
   	   			borrowerAddress = scanner.nextLine();
   	   			if(borrowerAddress.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			}
   	   		}
   			/* get borrower phone */
   			while(borrowerPhone.isEmpty() || borrowerPhone.length() > 45) {
   	   			System.out.print("New borrower phone:");
   	   			borrowerPhone = scanner.nextLine();
   	   			if(borrowerPhone.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			}
   	   		}
   			bodao.addBorrower(borrowerName, borrowerAddress, borrowerPhone);
   			System.out.println("Borrower added");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			System.out.println("Could not add borrower");
 			return;
 		}
	}
	
	public void updateBorrower() {
		boolean unique = false;
   		try (Connection conn = conUtil.getConnection()){
   			BorrowerDAO bodao = new BorrowerDAO(conn);
   			List<Borrower> Borrowers;
   			Borrower borrower = null;
   			String input = "";
	   		int count = 1;
	   		/* get borrowerId */
   			while(borrower == null) {
   	   			System.out.println("Choose Borrower by keyword (leave blank to see a list of all borrowers)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Borrowers = bodao.readAllBorrowers();
   	   			} else {
   	   				Borrowers = bodao.readAllBorrowersByName(input);
   	   			}
   	   			/* create borrower list to choose from */
   	   			if(Borrowers.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Borrowers.size() == 1) {
   	   				borrower = Borrowers.get(0);
   	   				System.out.println("Borrower name = " + borrower.getName() + ", "
   	   						+ borrower.getAddress() + ": " + borrower.getPhone());
   	   			} else {
					System.out.println("Borrowers:");
					count = 1;
					for(Borrower p: Borrowers) {
						System.out.println(count + ") " + p.getName() + ", " + p.getAddress() + ": " + p.getPhone());
						count++;
					}
					System.out.print("Choose a borrower to update (from the integer list): ");
					int borrowerOption = 0;
					while(borrowerOption < 1 || borrowerOption >= count) {
						try {
							borrowerOption = scanner.nextInt();
							scanner.nextLine();
							if(borrowerOption < 1 || borrowerOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								borrower = Borrowers.get(borrowerOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
   			}
   			/* update Borrower Name using borrowerId and borrowerName */
   			while(!unique) {
   	   			System.out.println("New Borrower Name: (leave blank if no change)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty() || input.equalsIgnoreCase(borrower.getName())) {
   	   				System.out.println("Continuing...");
   	   				unique = true;
   	   			}
   			}
   			borrower.setName(input);
   			/* update Borrower address */
	   		System.out.println("New Borrower Address: (leave blank if no change)");
	   		input = scanner.nextLine();
	   		if(input.isEmpty() || input.equalsIgnoreCase(borrower.getAddress())) {
	   			System.out.println("Continuing...");
	   		} else if(input.length() <= 45) {
	   			borrower.setAddress(input);
	   		} else {
	   			System.out.println("Address is too long... skipping");
	   		}
   			/* update Borrower phone */
	   		System.out.println("New Borrower Phone: (leave blank if no change)");
	   		input = scanner.nextLine();
	   		if(input.isEmpty() || input.equalsIgnoreCase(borrower.getPhone())) {
	   			System.out.println("Continuing...");
	   		} else if(input.length() <= 45) {
	   			borrower.setPhone(input);
	   		} else {
	   			System.out.println("Phone Number is too long... skipping");
	   		}
   			bodao.updateBorrower(borrower);
   			System.out.println("Borrower updated");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			e.printStackTrace();
 			System.out.println("Could not add borrower");
 			return;
 		}
	}
	
	public void deleteBorrower() {
   		try (Connection conn = conUtil.getConnection()){
   			BorrowerDAO bodao = new BorrowerDAO(conn);
   			List<Borrower> Borrowers;
   			Borrower borrower = null;
   			String input = "";
	   		int count = 1;
	   		/* get borrowerId */
   			while(borrower == null) {
   	   			System.out.println("Choose Borrower by name (leave blank to see a list of all borrowers)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Borrowers = bodao.readAllBorrowers();
   	   			} else {
   	   				Borrowers = bodao.readAllBorrowersByName(input);
   	   			}
   	   			/* create borrower list to choose from */
   	   			if(Borrowers.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Borrowers.size() == 1) {
   	   				borrower = Borrowers.get(0);
   	   				System.out.println("Borrower name = " + borrower.getName() + ", "
   	   						+ borrower.getAddress() + ": " + borrower.getPhone());
   	   			} else {
					System.out.println("Borrowers:");
					count = 1;
					for(Borrower p: Borrowers) {
						System.out.println(count + ") " + p.getName() + ", " + p.getAddress() + ": " + p.getPhone());
						count++;
					}
					System.out.print("Choose a borrower to delete (from the integer list): ");
					int borrowerOption = 0;
					while(borrowerOption < 1 || borrowerOption >= count) {
						try {
							borrowerOption = scanner.nextInt();
							scanner.nextLine();
							if(borrowerOption < 1 || borrowerOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								borrower = Borrowers.get(borrowerOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
				LoanDAO ldao = new LoanDAO(conn);
				int dependencies = ldao.checkBorrowerLoanDependency(borrower);
				if(dependencies > 0) {
					System.out.println("Cannot Delete: Borrower has " + dependencies + " books loaned out");
				} else {
					bodao.deleteBorrower(borrower);
					System.out.println("borrower deleted");
				}
   			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	public void readBorrowers() {
   		try (Connection conn = conUtil.getConnection()){
   			BorrowerDAO bodao = new BorrowerDAO(conn);
   			List<Borrower> Borrowers = bodao.readAllBorrowers();
   			for(Borrower b: Borrowers) {
   				b.adminPrint();;
   			}
   		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
   		}
   	}
	/* returns a book */
	public Book chooseBook() {
		try(Connection conn = conUtil.getConnection()) {
			while(true) {
				System.out.print("Enter title of book: ");
				String searchString = scanner.nextLine();
				BookDAO bdao = new BookDAO(conn);
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
}
