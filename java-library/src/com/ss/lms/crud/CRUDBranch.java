package com.ss.lms.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BookCopiesDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.BranchDAO;
import com.ss.lms.dao.GenreDAO;
import com.ss.lms.dao.LoanDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopies;
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;
import com.ss.lms.service.ConnectionUtil;
import com.ss.lms.service.LibrarianService;

public class CRUDBranch {
	public ConnectionUtil conUtil = new ConnectionUtil();
	LibrarianService LS = new LibrarianService();
	Scanner scanner = new Scanner(System.in);
	public void addBranch() {
		boolean unique = false;
		String branchName = "";
		String branchAddress = "";
   		try (Connection conn = conUtil.getConnection()){
   			BranchDAO brdao = new BranchDAO(conn);
   			/* get branch name */
   			while(!unique) {
   	   			System.out.println("New branch name: ");
   	   			branchName = scanner.nextLine();
   	   			if(branchName.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			} else {
   	   				unique = !brdao.searchBranchBoolean(branchName);
   	   				if(!unique) {
   	   					System.out.println("Branch name already exists");
   	   				}
   	   			}
   			}
   			/* get branch address */
   			while(branchAddress.isEmpty() || branchAddress.length() > 45) {
   	   			System.out.print("New branch address:");
   	   			branchAddress = scanner.nextLine();
   	   			if(branchAddress.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			}
   	   		}
   			
   			brdao.addBranch(branchName, branchAddress);
   			System.out.println("Branch added");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			System.out.println("Could not add branch");
 			return;
 		}
	}
	
	public void updateBranch() {
		/* retrieves all branches */
		int option = 1;
		Branch branch = null;
		List<Branch> branches = LS.getBranches();
		for(Branch br : branches) {
			System.out.println(option + ") " + br.getBranchName() + ", " + br.getBranchAddress());
			option++;
		}
		/* choose branch */
		System.out.println(option + ") Return");
		while(branch == null) {
			try {
				int input = scanner.nextInt();
				scanner.nextLine();
				if(input == option) {
					System.out.println("returning...");
					return;
				} else if(input > 0 && input < option) {
					branch = branches.get(input - 1);
				} else {
					System.out.println("Invalid input. Please choose a number from 1 to " + option);
				}
			} catch(NumberFormatException | InputMismatchException e) {
				System.out.println("Invalid input" + option);
				scanner.nextLine();
			}
		}
		System.out.println("Would you like to update the library's name and address or add/remove book copies?"
				+ "\n1) update library name/address"
				+ "\n2) modify book copies"
				+ "\n3) return");
		int answer = 0;
		while (answer < 1 | answer > 3) {
			answer = scanner.nextInt();
			scanner.nextLine();
			if(answer == 3) {
				System.out.println("returning...");
			} else if(answer < 1 | answer > 3) {
				System.out.println("Invalid answer");
			}
		}
		if(answer == 1) updateBranchDetails(branch);
		if(answer == 2) setBookCopies(branch);
	}
	public void deleteBranch() {
		int option = 1;
		Branch branch = null;
		List<Branch> branches = LS.getBranches();
		for(Branch br : branches) {
			System.out.println(option + ") " + br.getBranchName() + ", " + br.getBranchAddress());
			option++;
		}
		/* choose branch */
		System.out.println(option + ") Return");
		while(branch == null) {
			try {
				int input = scanner.nextInt();
				scanner.nextLine();
				if(input == option) {
					System.out.println("returning...");
					return;
				} else if(input > 0 && input < option) {
					branch = branches.get(input - 1);
				} else {
					System.out.println("Invalid input. Please choose a number from 1 to " + option);
				}
			} catch(NumberFormatException | InputMismatchException e) {
				System.out.println("Invalid input" + option);
				scanner.nextLine();
			}
		}
		/* check branch dependencies (books cannot be loaned out from this branch) */
		try (Connection conn = conUtil.getConnection()){
			LoanDAO ldao = new LoanDAO(conn);
			BranchDAO brDAO = new BranchDAO(conn);
			int dependencies = ldao.checkBranchLoanDependency(branch);
			if(dependencies == 0) {
				brDAO.deleteBranch(branch);
				System.out.println("Branch deleted");
			} else {
				System.out.println("ERROR: There are " + dependencies + " books currently loaned out from this branch");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void readBranches() {
   		try (Connection conn = conUtil.getConnection()){
   			BranchDAO brdao = new BranchDAO(conn);
   			List<Branch> Branches = brdao.readAllBranches();
   			for(Branch b: Branches) {
   				b.adminPrint();;
   			}
   		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
   		}
   	}
	
	/* BRANCH HELPER FUNCTIONS */
	public void updateBranchDetails(Branch branch) {
		LS.UpdateLibrary(branch);
	}
	public void setBookCopies(Branch branch) {
		try (Connection conn = conUtil.getConnection()){
			/* get book */
			Book book = readBook(chooseBook());
			/* get loans */
			LoanDAO ldao = new LoanDAO(conn);
			int bookLoans = ldao.getBranchBookLoans(branch, book);
			System.out.println("There are " + bookLoans + " outstanding loans for this book");
			/* get number of copies */
			BookCopiesDAO bcdao = new BookCopiesDAO(conn);
			List<BookCopies> bookCopies = bcdao.findBookCopiesByBranch(book, branch);
			BookCopies BC = null;
			if(bookCopies.size() == 0) {
				System.out.println("There are currently no copies of the book in the current branch");
				BC = new BookCopies(book.getBookId(), branch.getBranchId(), 0);
				bcdao.addBookCopies(BC);
			} else {
				BC = bookCopies.get(0);
				System.out.println("There are currently " + BC.getNoOfCopies() + " copies of the book in the current branch");
			}
			System.out.print("New number of book copies: ");
			
			int numCopies = -1;
			while(numCopies < bookLoans) {
				numCopies = scanner.nextInt();
				if(numCopies == 0 && bookLoans == 0) {
					// delete book
					bcdao.deleteBookCopies(BC);
					System.out.println("All Book copies deleted");
				} else if(numCopies == 0 && bookLoans > 0) {
					System.out.println("Error: You cannot have less book copies than there are books loaned out");
					numCopies = -1;
				} else if(numCopies < 0) {
					System.out.println("Error: Input must be greater than or equal to 0");
				} else {
					// change # of books
					BC.setnoOfCopies(numCopies);
					bcdao.updateBookCopies(BC);
					System.out.println("There are now " + numCopies + " copies");
				}
			}
	 	} catch (ClassNotFoundException | SQLException e) {
	 		System.out.print("SQL ERROR :(");
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
