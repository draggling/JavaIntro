package com.ss.lms.ui;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Loan;
import com.ss.lms.service.AdministratorService;
import com.ss.lms.service.BorrowerService;
import com.ss.lms.service.LibrarianService;

import java.util.Scanner;

public class Menus {
	AdministratorService AS = new AdministratorService();
	static LibrarianService LS = new LibrarianService();
	Scanner scanner = new Scanner(System.in);
	public void showBooks() {
		AdministratorService adminService = new AdministratorService();
		List<Book> books = adminService.getBooks(null);
		for (Book b : books) {
			System.out.println("Book Title: " + b.getTitle());
		}
	}
	/* choose librarian, administrator, or borrower */
	public void MainMenu() throws ClassNotFoundException, SQLException {
		System.out.println("Welcome to the SS Library Management System. Which category of a user are you"
				+ "\n1) Librarian"
				+ "\n2) Administrator"
				+ "\n3) Borrower");
		/* loops indefinitely until a number from 1-3 is chosen */
		while(true) {
			try {
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						LIB1();
						return;
					case(2):
						ADMIN();
						return;
					case(3):
						BORR1();
						return;
					default:
						System.out.println("Invalid input. Please choose a number from 1 to 3");
				}
			} catch(NumberFormatException | InputMismatchException e) {
				System.out.println("Invalid input. Please choose a number from 1 to 3");
			}
		}
	}
	/* Choose library or return to main menu */
	public void LIB1() throws ClassNotFoundException, SQLException {
		System.out.println("1) Enter Branch you manage"
				+ "\n2) Quit to Previous");
		try {
			while(true) {
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						LIB2();
						return;
					case(2):
						MainMenu();
						return;
					default:
						System.out.println("Invalid input. Please choose a number from 1 to 2");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid input. Please choose a number from 1 to 2");
		}
	}
	/* Choose library branch or return to LIB1 */
	public void LIB2() throws ClassNotFoundException, SQLException {
		/* retrieves all branches */
		int option = 1;
		List<Branch> branches = LS.getBranches();
		for(Branch br : branches) {
			System.out.println(option + ") " + br.getBranchName() + ", " + br.getBranchAddress());
			option++;
		}
		System.out.println(option + ") Quit to previous");
		while(true) {
			try {
				int input = scanner.nextInt();
				if(input == option) {
					MainMenu();
					return;
				} else if(input > 0 && input < option) {
					LIB3(branches.get(input - 1));
					return;
				}
			} catch(NumberFormatException | InputMismatchException e) {
				System.out.println("Invalid input. Please choose a number from 1 to " + option);
			}
		}
	}
	/* Update details, add copies of book, or return to LIB2 */
	public void LIB3(Branch branch) throws ClassNotFoundException, SQLException {
		System.out.println("1) Update the details of the Library"
				+ "\n2) Add copies of book to the branch"
				+ "\n3) Quit to previous");
		while(true) {
			try {
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						while(!LS.UpdateLibrary(branch)) {
							System.out.println("error: update crash");
						}
						System.out.println("Returning to main menu");
						MainMenu();
						return;
					case(2):
						boolean success = LS.AddBookCopies(branch);
						if(success) {
							System.out.println("returning to main menu");
							MainMenu();
						/* should never get here */
						} else {
							System.err.println("error: exiting");
							return;
						}
						return;
					case(3):
						LIB2();
						return;
					default:
						System.out.println("Invalid input. Please choose a number from 1 to 3");
				}
			} catch(NumberFormatException | InputMismatchException e) {
				System.out.println("Invalid input. Please choose a number from 1 to 3");
			}
		}
	}

	public void BORR1() throws ClassNotFoundException, SQLException {
		BorrowerService BS = new BorrowerService();
		while(true) {
			try {
				System.out.print("Please enter a valid card number: ");
				int card = scanner.nextInt();
				scanner.nextLine();
				Borrower user = BS.ValidateCard(card);
				if(user != null) {
					user.setLoans();
					//System.out.println(user.toString());
					System.out.println("Welcome, " + user.getName());
					System.out.println("Choose an option:");
					BORR2(user);
					return;
				} else {
					System.out.println("Invalid number");
				}
			} catch(NumberFormatException | InputMismatchException e) {
				System.out.println("error: a library card is an integer");
				scanner.nextLine();
			}
		}
	}
	
	public void BORR2(Borrower user) throws ClassNotFoundException, SQLException {
		try {
			while(true) {
				System.out.println("1) Check out a book"
						+ "\n2) Return a book"
						+ "\n3) quit to previous"
						+ "\nWhat would you like to do? ");
				int input = scanner.nextInt();
				scanner.nextLine();
				switch(input) {
					case(1):
						Checkout(user);
						return;
					case(2):
						Checkin(user);
						return;
					case(3):
						MainMenu();
						return;
					default:
						System.out.println("Invalid input");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Please input a number from 1 to 3");
			scanner.nextLine();
		}
	}
	
	public void Checkout(Borrower user) throws ClassNotFoundException, SQLException {
		System.out.println("Which library are you checking out from?");
		/* retrieves all branches */
		int option = 1;
		List<Branch> branches = LS.getBranches();
		for(Branch br : branches) {
			System.out.println(option + ") " + br.getBranchName() + ", " + br.getBranchAddress());
			option++;
		}
		System.out.println(option + ") Quit to previous");
		while(true) {
			try {
				int input = scanner.nextInt();
				if(input == option) {
					MainMenu();
					return;
				} else if(input > 0 && input < option) {
					CreateLoan(user, branches.get(input - 1));
					return;
				}
			} catch(NumberFormatException | InputMismatchException e) {
				System.out.println("Invalid input. Please choose a number from 1 to " + option);
				scanner.nextLine();
			} finally {
				
			}
		}
	}
	
	public void CreateLoan(Borrower user, Branch branch) throws ClassNotFoundException, SQLException {
		BorrowerService BS = new BorrowerService();
		try {
			while(true) {
				System.out.println("------------");
				System.out.println("1) print out all books at " + branch.getBranchName());
				System.out.println("2) search for a book by name ");
				System.out.println("3) return to borrower options");
				System.out.print("Input your option: ");
				int input = scanner.nextInt();
				switch(input) {
					case(1): 
						List<Book> bookOptions = BS.ReturnBranchBooks(branch);
						int counter = 1;
						for(Book b: bookOptions) {
							System.out.println(counter + ") " + BS.readBook(b).toString());
							counter++;
						}
						System.out.println(counter + ") return to borrower menu");
						try {
							int option = scanner.nextInt();
							if(option == counter) {
								BORR2(user);
								return;
							} else if (option > 0 && option < counter) {
								Book book = bookOptions.get(option-1);
								if(BS.checkBorrowerLoans(user, book)) {
									System.out.println("You already have this book loaned out");
								} else {
									System.out.println("\nchecking out book... ");
									BS.checkOutBook(user, branch, book);
								}
							} else {
								System.out.println("Invalid input. Please choose an option");
							}
						} catch(NumberFormatException | InputMismatchException e) {
							System.out.println("Invalid Input");
							scanner.nextLine();
						}
						break;
					case(2):
						Book book = BS.searchBranchAvailableBooks(user.getCardNo(), branch.getBranchId());
						if(book != null) {
							if(BS.checkBorrowerLoans(user, book)) {
								System.out.println("You already have this book loaned out");
							} else {
								System.out.println("Book Details:\n" + book.toString() + "\nchecking out book... ");
								BS.checkOutBook(user, branch, book);
							}
						}
					case(3):
						BORR2(user);
						return;
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid Answer");
			scanner.nextLine();
		}
	}
	public void Checkin(Borrower user) throws ClassNotFoundException, SQLException {
		BorrowerService BS = new BorrowerService();
		System.out.println("Displaying all loans");
		List<Loan> loans = user.getLoans();
		if(loans.size() == 0) {
			System.out.println("No loans due. Returning to the main menu");
			MainMenu();
			return;
		}
		int counter = 1;
		for(Loan loan : loans) {
			System.out.println(counter + ") " + loan.bookInfo());
			counter++;
		}
		System.out.println(counter + ") Return to main menu");
		while(true) {
			try {
				int input = scanner.nextInt();
				if(input == counter) {
					MainMenu();
					return;
				} else if(input > 0 && input < counter) {
					BS.checkInBook(loans.get(input - 1));
					System.out.println("Book checked in");
					MainMenu();
					return;
				}
			} catch(NumberFormatException | InputMismatchException e) {
				System.out.println("Invalid input. Please choose a number from 1 to " + counter);
				scanner.nextLine();
			}
		}
	}
	
	public void ADMIN() throws ClassNotFoundException, SQLException {
		try {
			while(true) {
				System.out.println("*************");
				System.out.println("1) Add/Update/Delete/Read Book");
				System.out.println("2) Add/Update/Delete/Read Author");
				System.out.println("3) Add/Update/Delete/Read Genres");
				System.out.println("4) Add/Update/Delete/Read Publishers");
				System.out.println("5) Add/Update/Delete/Read Library Branches");
				System.out.println("6) Add/Update/Delete/Read Borrowers");
				System.out.println("7) Over-ride Due Date for a Book Loan");
				System.out.println("8) Return to Main Menu");
				System.out.print("Choose an option: ");
				int option = scanner.nextInt();
				scanner.nextLine();
				switch(option) {
					case(1):
						Book();
						break;
					case(2):
						Author();
						break;
					case(3):
						Genre();
						break;
					case(4):
						Publisher();
						break;
					case(5):
						Branch();
						break;
					case(6):
						Borrower();
						break;
					case(7):
						OverrideDueDate();
						break;
					case(8):
						System.out.println("Returning to main menu...\n");
						MainMenu();
						return;
					default:
						System.out.println("Invalid Input");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid input. Please choose a number from 1 to 8");
			scanner.nextLine();
		}
	}
	
	public void Book() throws ClassNotFoundException, SQLException {
		try {
			while(true) {
				System.out.println("*************");
				System.out.println("Book Menu");
				System.out.println("1) Add Book");
				System.out.println("2) Update Book");
				System.out.println("3) Delete Book");
				System.out.println("4) Read Books");
				System.out.println("5) Return to Main Menu");
				System.out.print("Choose an option: ");
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						AS.addBook();
						break;
					case(2):
						AS.updateBook();
						break;
					case(3):
						AS.deleteBook();
						break;
					case(4):
						System.out.println(AS.readBook(AS.chooseBook()).toString());
						break;
					case(5):
						System.out.println("Returning to main menu...\n");
						MainMenu();
						return;
					default:
						System.out.println("Invalid Input");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid input. Please choose a number from 1 to 5");
			scanner.nextLine();
		}
	// Add book --> book title is unique
		// choose publisher, book title, author(s), genre(s)
	// Delete book --> check to see if bookId has any loaned out copies
	// Modify book --> change publisher, author(s), genre(s), 
		
	}
	public void Author() throws ClassNotFoundException, SQLException {
		try {
			while(true) {
				System.out.println("*************");
				System.out.println("Author Menu");
				System.out.println("1) Add author");
				System.out.println("2) Update author");
				System.out.println("3) Delete author");
				System.out.println("4) Read authors");
				System.out.println("5) Return to Main Menu");
				System.out.print("Choose an option: ");
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						AS.addAuthor();
						break;
					case(2):
						AS.updateAuthor();
						break;
					case(3):
						AS.deleteAuthor();
						break;
					case(4):
						AS.readAuthors();
						break;
					case(5):
						System.out.println("Returning to main menu...\n");
						MainMenu();
						return;
					default:
						System.out.println("Invalid Input");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid input. Please choose a number from 1 to 5");
			scanner.nextLine();
		}
	}
	
	public void Genre() throws ClassNotFoundException, SQLException {
		try {
			while(true) {
				System.out.println("*************");
				System.out.println("Genre Menu");
				System.out.println("1) Add genre");
				System.out.println("2) Update genre");
				System.out.println("3) Delete genre");
				System.out.println("4) Read genres");
				System.out.println("5) Return to Main Menu");
				System.out.print("Choose an option: ");
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						AS.addGenre();
						break;
					case(2):
						AS.updateGenre();
						break;
					case(3):
						AS.deleteGenre();
						break;
					case(4):
						AS.readGenres();
						break;
					case(5):
						System.out.println("Returning to main menu...\n");
						MainMenu();
						return;
					default:
						System.out.println("Invalid Input");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid input. Please choose a number from 1 to 5");
			scanner.nextLine();
		}
	}

	public void Publisher() throws ClassNotFoundException, SQLException {
		try {
			while(true) {
				System.out.println("*************");
				System.out.println("Publisher Menu");
				System.out.println("1) Add publisher");
				System.out.println("2) Update publisher");
				System.out.println("3) Delete publisher");
				System.out.println("4) Read Publishers");
				System.out.println("5) Return to Main Menu");
				System.out.print("Choose an option: ");
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						AS.addPublisher();
						break;
					case(2):
						AS.updatePublisher();
						break;
					case(3):
						AS.deletePublisher();
						break;
					case(4):
						AS.readPublishers();
						break;
					case(5):
						System.out.println("Returning to main menu...\n");
						MainMenu();
						return;
					default:
						System.out.println("Invalid Input");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid input. Please choose a number from 1 to 5");
			scanner.nextLine();
		}
	}

	public void Branch() throws ClassNotFoundException, SQLException {
		try {
			while(true) {
				System.out.println("*************");
				System.out.println("Branch Menu");
				System.out.println("1) Add branch");
				System.out.println("2) Update branch");
				System.out.println("3) Delete branch");
				System.out.println("4) Read branches");
				System.out.println("5) Return to Main Menu");
				System.out.print("Choose an option: ");
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						AS.addBranch();
						break;
					case(2):
						AS.updateBranch();
						break;
					case(3):
						AS.deleteBranch();
						break;
					case(4):
						AS.readBranches();
						break;
					case(5):
						System.out.println("Returning to main menu...\n");
						MainMenu();
						return;
					default:
						System.out.println("Invalid Input");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid input. Please choose a number from 1 to 5");
			scanner.nextLine();
		}
	}

	public void Borrower() throws ClassNotFoundException, SQLException {
		try {
			while(true) {
				System.out.println("*************");
				System.out.println("Borrower Menu");
				System.out.println("1) Add borrower");
				System.out.println("2) Update borrower");
				System.out.println("3) Delete borrower");
				System.out.println("4) Read borrowers");
				System.out.println("5) Return to Main Menu");
				System.out.print("Choose an option: ");
				int option = scanner.nextInt();
				switch(option) {
					case(1):
						AS.addBorrower();
						break;
					case(2):
						AS.updateBorrower();
						break;
					case(3):
						AS.deleteBorrower();
						break;
					case(4):
						AS.readBorrowers();
						break;
					case(5):
						System.out.println("Returning to main menu...\n");
						MainMenu();
						return;
					default:
						System.out.println("Invalid Input");
				}
			}
		} catch(NumberFormatException | InputMismatchException e) {
			System.out.println("Invalid input. Please choose a number from 1 to 5");
			scanner.nextLine();
		}
	}
	public void OverrideDueDate() {
		System.out.println("OverrideDueDate");
		AS.overrideDueDate();
	}
}
