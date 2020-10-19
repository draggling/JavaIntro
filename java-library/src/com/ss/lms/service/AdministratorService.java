package com.ss.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BookCopiesDAO;
import com.ss.lms.dao.BranchDAO;
import com.ss.lms.dao.GenreDAO;
import com.ss.lms.dao.LoanDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.BorrowerDAO;
import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopies;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Loan;
import com.ss.lms.entity.Publisher;

public class AdministratorService {

	LibrarianService LS = new LibrarianService();
	public ConnectionUtil conUtil = new ConnectionUtil();
	Scanner scanner = new Scanner(System.in);

	/* BOOK FUNCTIONS */
	public void addBook() throws ClassNotFoundException, SQLException {
		String title = "";
		Integer publisherId = 0;
		String publisherName = "";
		try (Connection conn = conUtil.getConnection()){
			BookDAO bdao = new BookDAO(conn);
			AuthorDAO adao = new AuthorDAO(conn);
			PublisherDAO pdao = new PublisherDAO(conn);
			GenreDAO gdao = new GenreDAO(conn);
			System.out.println("Generating new book...");
			/* Title */
			try {
				while(title.isEmpty() || title.length() > 45) {
					System.out.print("Title: ");
					title = scanner.nextLine();
				}
			} catch(InputMismatchException e) {
				System.out.println("Invalid title");
				scanner.nextLine();
			}
			/* get publisher */
			try {
				while(publisherId == 0) {
					System.out.print("Publisher Name: ");
					publisherName = scanner.nextLine();
					List<Publisher> temp = pdao.getPublishersByName(publisherName);
					if(temp.size() == 0) {
						System.out.println("No publisher found");
					} else if (temp.size() == 1) {
						publisherId = temp.get(0).getPublisherId();
					} else if (temp.size() > 1){
						System.out.println("Choose from the list");
						int counter = 1;
						for(Publisher p : temp) {
							System.out.println(counter + ") " + p.toString() + "\n");
							counter++;
						}
						System.out.println(counter + ") Return to admin menu");
						try {
							System.out.print("Input: ");
							int input = scanner.nextInt();
							if(input == counter) {
								return;
							} else if(input > 0 && input < counter) {
								publisherId = temp.get(input-1).getPublisherId();
							} else {
								System.out.println("Please input a positive number in the choice range");
							}
						} catch(NumberFormatException | InputMismatchException e) {
							System.out.println("Invalid input: please input an integer");
							scanner.nextLine();
						}
					} else {
						System.out.println("ERROR - Exiting");
						return;
					}
				}
			} catch(InputMismatchException | SQLException e) {
				System.out.println("Invalid input. Please choose a number from 1 to 5");
				scanner.nextLine();
			}
			
			/* generate book */
			Book temp = new Book(title, publisherId);
			Book book = new Book(bdao.addBookWithPk(temp), title, publisherId);
			
			/* get authors */
			int authorCount = 0;
			boolean authorDone = false;
			HashMap<Integer, Integer> duplicateTest = new HashMap<>();
			while (authorCount == 0 || !authorDone) {
				try {
					System.out.print("Add an author: ");
					if(authorCount != 0) {
						System.out.print("(Press enter to finish): ");
					}
					String authorName = scanner.nextLine();
					if (authorName.isEmpty() && authorCount > 0) {
						authorDone = true;
					} else if (authorName.isEmpty()) {
						System.out.println("Book must have at least one author");
					} else {
						List<Author> authorSearch = adao.searchAuthor(authorName);
						if(authorSearch.size() == 0) {
							System.out.println("No author found");
						} else {
							//
							Integer authorId = authorSearch.get(0).getAuthorId();
							if(!adao.findBookAuthorPair(book.getBookId(), authorId)) {
								/* pair author to book in bookAuthor table */
								if(duplicateTest.containsKey(authorId)) {
									System.out.println("Book already contain this author");
								} else {
									duplicateTest.put(authorId, 1);
									adao.addBookAuthorPair(book.getBookId(), authorId);
									authorCount++;
								}
							} else {
								System.out.println("Author already associated with book");
							}
						}
					}
				} catch(InputMismatchException | SQLException e) {
					System.out.println("Invalid Author");
					scanner.nextLine();
				}
			}
			/* get genres  */
			duplicateTest.clear();
			int genreCount = 0;
			boolean genreDone = false;
			while (genreCount == 0 || !genreDone) {
				try {
					System.out.print("Add a genre: ");
					if(genreCount != 0) {
						System.out.print("(Press enter to finish): ");
					}
					String genreName = scanner.nextLine();
					if (genreName.isEmpty() && genreCount > 0) {
						genreDone = true;
					} else if (genreName.isEmpty()) {
						System.out.println("Book must have at least one genre");
					} else {
						List<Genre> genreSearch = gdao.searchGenre(genreName);
						if(genreSearch.size() == 0) {
							System.out.println("No genre found");
						} else {
							Integer genreId = genreSearch.get(0).getGenreId();
							if(!gdao.findBookGenrePair(book.getBookId(), genreId)) {
								/* pair author to book in bookAuthor table */
								if(duplicateTest.containsKey(genreId)) {
									System.out.println("Book already has this genre");
								} else {
									duplicateTest.put(genreId, 1);
									gdao.addBookGenrePair(book.getBookId(), genreId);
									genreCount++;
								}
							} else {
								System.out.println("Genre already associated with book");
							}
						}
					}
				} catch(InputMismatchException | SQLException e) {
					System.out.println("Invalid Genre");
					scanner.nextLine();
				}
			}
		/* commit all changes */
		conn.commit();
		System.out.println("Added book succesfully");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Book updateBook() throws ClassNotFoundException, SQLException {
		int count;
		try (Connection conn = conUtil.getConnection()){
			PublisherDAO pdao = new PublisherDAO(conn);
			Book book = readBook(chooseBook());
			System.out.println("\nWhat do you want to change about the book? ");
			System.out.println("1) Title");
			System.out.println("2) Publisher");
			System.out.println("3) Author(s)");
			System.out.println("4) Genre(s)");
			System.out.println("5) return to previous menu");
			try (Connection conn2 = conUtil.getConnection()){
				System.out.print("input your choice: ");
				int option = scanner.nextInt();
				scanner.nextLine();
				switch(option) {
				/* UPDATING BOOK TITLE */
					case(1):
						String title = "";
						/* resets scanner */
						while(title.isEmpty() ||  title.length() > 45) {
							System.out.print("New Title: ");
							title = scanner.nextLine();
						}
						book.setTitle(title);
						BookDAO bdao2 = new BookDAO(conn2);
						bdao2.updateBook(book);
						System.out.println("Book title updated\n");
						return book;
					/* UPDATING BOOK PUBLISHER */
					case(2):
						try {
							int publisherId = 0;
							while(publisherId == 0) {
								System.out.print("Publisher Name: ");
								String publisherName = scanner.nextLine();
								try (Connection conn3 = conUtil.getConnection()){
									List<Publisher> temp = pdao.getPublishersByName(publisherName);
									if(temp.size() == 0) {
										System.out.println("No publisher found");
									} else if (temp.size() == 1) {
										System.out.println("1 publisher found");
										publisherId = temp.get(0).getPublisherId();
										book.setPubId(publisherId);
										book.setPublisher(temp.get(0));
										BookDAO bdao3 = new BookDAO(conn3);
										bdao3.updateBookPublisher(book);
										System.out.println("Publisher updated");
									} else {
										System.out.println("Choose from the list");
										int counter = 1;
										for(Publisher p : temp) {
											System.out.println(counter + ") " + p.toString() + "\n");
											counter++;
										}
										try {
											System.out.print("Input: ");
											int input = scanner.nextInt();
											scanner.nextLine();
											if(input > 0 && input < counter) {
												Publisher tempPublisher = temp.get(input-1);
												book.setPubId(tempPublisher.getPublisherId());
												book.setPublisher(tempPublisher);
												BookDAO bdao3 = new BookDAO(conn3);
												bdao3.updateBookPublisher(book);
												System.out.println("Publisher updated");
												return readBook(book);
											} else {
												System.out.println("Please input a positive number in the choice range");
											}
										} catch(NumberFormatException | InputMismatchException e) {
											System.out.println("Invalid input: please input an integer");
											scanner.nextLine();
										}
									}
								} catch(NumberFormatException | InputMismatchException e) {
									System.out.println("Invalid input: please input an integer");
									scanner.nextLine();
								}
							}
						} catch(InputMismatchException | SQLException e) {
							System.out.println("ERROR");
							e.printStackTrace();
						}
					return book;
					case(3):
						System.out.println("Author Options:");
						System.out.println("1) Add author");
						System.out.println("2) Delete author");
						System.out.println("3) return");
						int authorOption = 0;
						while (authorOption < 1 || authorOption > 3) {
							try {
								System.out.print("Input: ");
								authorOption = scanner.nextInt();
								scanner.nextLine();
								if(authorOption < 1 || authorOption > 3) {
									System.out.println("Input out of bounds.");
								}
							} catch(InputMismatchException e) {
								System.out.println("Input an integer");
								scanner.nextLine();
							}
						}
						System.out.println("Authors: ");
						count = 1;
						for(Author a: book.getAuthors()) {
							System.out.println(count + ") " + a.getAuthorName());
							count++;
						}
						switch(authorOption) {
						case(1):
							/* find set difference of all authors and book's authors */
							try (Connection conn4 = conUtil.getConnection()){
								AuthorDAO adao2 = new AuthorDAO(conn4);
								List<Author> remainingAuthors = adao2.readAllAuthors();
								remainingAuthors.removeAll(book.getAuthors());
								System.out.println("Choose an Author to add:");
								count = 1;
								for(Author a: remainingAuthors) {
									System.out.println(count + ") " + a.getAuthorName());
									count++;
								}
								System.out.print("Choose an author to add to book (from the integer list): ");
								authorOption = 0;
								while(authorOption < 1 || authorOption >= count) {
									try {
										authorOption = scanner.nextInt();
										scanner.nextLine();
										if(authorOption < 1 || authorOption >= count) {
											System.out.println("Out of Bounds");
										} else {
											int authorId = remainingAuthors.get(authorOption-1).getAuthorId();
											adao2.addBookAuthorPair(book.getBookId(), authorId);
											System.out.println("Author added to book");
											return readBook(book);
										}
									} catch(InputMismatchException e) {
										System.out.println("ERROR: integer required");
										scanner.nextLine();
									}
								}
							} catch (ClassNotFoundException | SQLException e) {
								e.printStackTrace();
							}							

						case(2):
							if(book.getAuthors().size() <= 1) {
								System.out.println("Cannot remove the remaining author of a book");
								return book;
							} 	else {
								System.out.print("Choose an author to remove from book(from the integer list): ");
								authorOption = 0;
								while(authorOption < 1 || authorOption >= count) {
									try {
										authorOption = scanner.nextInt();
										scanner.nextLine();
										if(authorOption < 1 || authorOption >= count) {
											System.out.println("Out of Bounds");
										} else {
											try (Connection conn5 = conUtil.getConnection()){
												AuthorDAO adao3 = new AuthorDAO(conn5);
												adao3.deleteBookAuthorPair(book.getBookId(), book.getAuthors().get(authorOption-1).getAuthorId());
												System.out.println("Author deleted from book");
												return readBook(book);
											} catch (ClassNotFoundException | SQLException e) {
												e.printStackTrace();
											}

										}
									} catch(InputMismatchException e) {
										System.out.println("ERROR: integer required");
										scanner.nextLine();
									}
								}
							}
						case(3): return book;
						}
					case(4):
						System.out.println("Genre Options:");
						System.out.println("1) Add Genre");
						System.out.println("2) Delete Genre");
						System.out.println("3) return");
						int genreOption = 0;
						while (genreOption < 1 || genreOption > 3) {
							try {
								System.out.print("Input: ");
								genreOption = scanner.nextInt();
								scanner.nextLine();
								if(genreOption < 1 || genreOption > 3) {
									System.out.println("Input out of bounds.");
								}
							} catch(InputMismatchException e) {
								System.out.println("Input an Integer ");
								scanner.nextLine();
							}
						}
						System.out.println("Authors: ");
						count = 1;
						for(Genre g: book.getGenres()) {
							System.out.println(count + ") " + g.getGenreName());
							count++;
						}
						switch(genreOption) {
						case(1):
							/* find set difference of all genres and book's genres */
							try (Connection conn3 = conUtil.getConnection()){
								GenreDAO gdao2 = new GenreDAO(conn);
								List<Genre> remainingGenres = gdao2.readAllGenres();
								List<Genre> bookGenres = book.getGenres();
								/* set difference between all genres and the current book's genres */
								for(int i = 0; i < bookGenres.size(); i++) {
									for(int j = 0; j < remainingGenres.size(); j++) {
										if(bookGenres.get(i).getGenreId() == remainingGenres.get(j).getGenreId()) {
											remainingGenres.remove(j);
										}
									}
								}
								
								System.out.println("Choose a Genre to add to book:");
								count = 1;
								for(Genre g: remainingGenres) {
									System.out.println(count + ") " + g.getGenreName());
									count++;
								}
								System.out.print("Choose a genre to remove from book (from the integer list): ");
								genreOption = 0;
								while(genreOption < 1 || genreOption >= count) {
									try {
										genreOption = scanner.nextInt();
										scanner.nextLine();
										if(genreOption < 1 || genreOption >= count) {
											System.out.println("Out of Bounds");
										} else {
											int genreId = remainingGenres.get(genreOption-1).getGenreId();
											gdao2.addBookGenrePair(book.getBookId(), genreId);
											System.out.println("Genre added to book");
											return readBook(book);
										}
									} catch(InputMismatchException e) {
										System.out.println("ERROR: integer required");
										genreOption = 0;
										scanner.nextLine();
									}
								}
							} catch (ClassNotFoundException | SQLException e) {
								e.printStackTrace();
							}
						case(2):
							if(book.getGenres().size() <= 1) {
								System.out.println("Cannot remove the remaining genre of a book");
								return book;
							} 	else {
								System.out.print("Choose a genre to remove (from the integer list): ");
								genreOption = 0;
								while(genreOption < 1 || genreOption >= count) {
									try {
										genreOption = scanner.nextInt();
										scanner.nextLine();
										if(genreOption < 1 || genreOption >= count) {
											System.out.println("Out of Bounds");
										} else {
											try (Connection conn4 = conUtil.getConnection()){
												GenreDAO gdao2 = new GenreDAO(conn4);
												gdao2.deleteBookGenrePair(book.getBookId(), book.getGenres().get(genreOption-1).getGenreId());
												System.out.println("Genre deleted from book");
												return readBook(book);
											} catch (ClassNotFoundException | SQLException e) {
												e.printStackTrace();
											}

										}
									} catch(InputMismatchException e) {
										System.out.println("ERROR: integer required");
										scanner.nextLine();
									}
								}
							}
						case(3): return book;
						}
					case(5):
						return book;
					default:
						System.out.println("Invalid Input");
				}
			} catch(InputMismatchException e) {
				System.out.println("ERROR: enter an integer from 1 to 5");
				scanner.nextLine();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		System.out.println("ERROR; Program should never arrive here");
		return null;
	}
	/* propagates book data */
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
	
	/* delete book */
	public void deleteBook() throws ClassNotFoundException {
		Book book = readBook(chooseBook());
		try (Connection conn = conUtil.getConnection()) {
			BookDAO bdao = new BookDAO(conn);
			int bookLoaned = bdao.bookIsLoaned(book.getBookId());
			if(bookLoaned == 0) {
				bdao.deleteBook(book);
				System.out.println("Book Deleted");
			} else {
				System.out.println("There are " + bookLoaned + "copies of this book loaned out");
				System.out.println("Cannot delete a book that has copies loaned out");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println("Delete ERROR: could not delete book");
		}
	}
	/* BOOK HELPER FUNCTIONS */
	/* get book or get all books */
	public List<Book> getBooks(String searchString) {
		try(Connection conn = conUtil.getConnection()) {
			BookDAO bdao = new BookDAO(conn);
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
	
	/* AUTHOR FUNCTIONS */
	public void addAuthor() {
		boolean unique = false;
   		try (Connection conn = conUtil.getConnection()){
   			AuthorDAO adao = new AuthorDAO(conn);
   			String input = "";
   			while(!unique) {
   	   			System.out.println("New author name");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			} else {
   	   				unique = !adao.searchAuthorBoolean(input);
   	   				if(!unique) {
   	   					System.out.println("Author name already exists");
   	   				}
   	   			}
   			}
   			adao.addAuthor(input);
   			System.out.println("Author added");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			System.out.println("Could not add author");
 			return;
 		}
	}
	public void updateAuthor() {
		boolean unique = false;
   		try (Connection conn = conUtil.getConnection()){
   			AuthorDAO adao = new AuthorDAO(conn);
   			List<Author> Authors;
   			Author author = null;
   			String input = "";
	   		int count = 1;
	   		/* get authorId */
   			while(author == null) {
   	   			System.out.println("Choose Author by keyword (leave blank to see a list of all authors)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Authors = adao.readAllAuthors();
   	   			} else {
   	   				Authors = adao.readAllAuthorsByName(input);
   	   			}
   	   			/* create author list to choose from */
   	   			if(Authors.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Authors.size() == 1) {
   	   				author = Authors.get(0);
   	   				System.out.println("Author name = " + author.getAuthorName());
   	   			} else {
					System.out.println("Choose an Author to update:");
					count = 1;
					for(Author a: Authors) {
						System.out.println(count + ") " + a.getAuthorName());
						count++;
					}
					System.out.print("Choose an author to add to book (from the integer list): ");
					int authorOption = 0;
					while(authorOption < 1 || authorOption >= count) {
						try {
							authorOption = scanner.nextInt();
							scanner.nextLine();
							if(authorOption < 1 || authorOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								author = Authors.get(authorOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
   			}
   			/* update Author Name using authorId and authorName */
   			while(!unique) {
   	   			System.out.println("New Author Name");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			} else {
   	   				unique = !adao.searchAuthorBoolean(input);
   	   				if(!unique) {
   	   					System.out.print("Author with this name already exists");
   	   				}
   	   			}
   			}
   			author.setAuthorName(input);
   			adao.updateAuthor(author);
   			System.out.println("Author updated");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			e.printStackTrace();
 			System.out.println("Could not add author");
 			return;
 		}
	}
	
	public void deleteAuthor( ){
   		try (Connection conn = conUtil.getConnection()){
   			AuthorDAO adao = new AuthorDAO(conn);
   			List<Author> Authors;
   			Author author = null;
   			int count = 0;
   			String input;
			while(author == null) {
   	   			System.out.println("Choose Author by keyword (leave blank to see a list of all authors)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Authors = adao.readAllAuthors();
   	   			} else {
   	   				Authors = adao.readAllAuthorsByName(input);
   	   			}
   	   			/* create author list to choose from */
   	   			if(Authors.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Authors.size() == 1) {
   	   				author = Authors.get(0);
   	   			} else {
					System.out.println("Choose an Author to update:");
					count = 1;
					for(Author a: Authors) {
						System.out.println(count + ") " + a.getAuthorName());
						count++;
					}
					System.out.print("Choose an author to delete (from the integer list): ");
					int authorOption = 0;
					while(authorOption < 1 || authorOption >= count) {
						try {
							authorOption = scanner.nextInt();
							scanner.nextLine();
							if(authorOption < 1 || authorOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								author = Authors.get(authorOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
			}
   	   		/* check to see if author has dependencies */
			BookDAO bdao = new BookDAO(conn);
			/* probably doesn't work */
			int dependencies = bdao.checkBookSingleAuthor(author);
			if(dependencies == 0) {
				adao.deleteAuthor(author);
				System.out.println("Author Deleted");
				return;
			} else {
				System.out.println("ERROR: Author has " + dependencies + " dependencies");
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void readAuthors() {
   		try (Connection conn = conUtil.getConnection()){
   			AuthorDAO adao = new AuthorDAO(conn);
   			List<Author> Authors = adao.readAllAuthors();
   			for(Author a: Authors) {
   				a.adminPrint();;
   			}
   		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
   		}
   	}
	
	/* GENRE FUNCTIONS */
	public void addGenre() {
		boolean unique = false;
   		try (Connection conn = conUtil.getConnection()){
   			GenreDAO gdao = new GenreDAO(conn);
   			String input = "";
   			while(!unique) {
   	   			System.out.println("New genre name");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			} else {
   	   				unique = !gdao.searchGenreBoolean(input);
   	   				if(!unique) {
   	   					System.out.println("Genre name already exists");
   	   				}
   	   			}
   			}
   			gdao.addGenre(input);
   			System.out.println("Genre added");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			System.out.println("Could not add genre");
 			return;
 		}
	}
	public void updateGenre() {
		boolean unique = false;
   		try (Connection conn = conUtil.getConnection()){
   			GenreDAO gdao = new GenreDAO(conn);
   			List<Genre> Genres;
   			Genre genre = null;
   			String input = "";
	   		int count = 1;
	   		/* get genreId */
   			while(genre == null) {
   	   			System.out.println("Choose Genre by keyword (leave blank to see a list of all genres)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Genres = gdao.readAllGenres();
   	   			} else {
   	   				Genres = gdao.readAllGenresByName(input);
   	   			}
   	   			/* create genre list to choose from */
   	   			if(Genres.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Genres.size() == 1) {
   	   				genre = Genres.get(0);
   	   				System.out.println("Genre name = " + genre.getGenreName());
   	   			} else {
					System.out.println("Choose a Genre to update:");
					count = 1;
					for(Genre b: Genres) {
						System.out.println(count + ") " + b.getGenreName());
						count++;
					}
					System.out.print("Choose a genre to add to book (from the integer list): ");
					int genreOption = 0;
					while(genreOption < 1 || genreOption >= count) {
						try {
							genreOption = scanner.nextInt();
							scanner.nextLine();
							if(genreOption < 1 || genreOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								genre = Genres.get(genreOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
   			}
   			/* update Genre Name using genreId and genreName */
   			while(!unique) {
   	   			System.out.println("New Genre Name");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			} else {
   	   				unique = !gdao.searchGenreBoolean(input);
   	   				if(!unique) {
   	   					System.out.print("Genre with this name already exists");
   	   				}
   	   			}
   			}
   			genre.setGenreName(input);
   			gdao.updateGenre(genre);
   			System.out.println("Genre updated");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			e.printStackTrace();
 			System.out.println("Could not add genre");
 			return;
 		}
	}
	
	public void deleteGenre( ){
   		try (Connection conn = conUtil.getConnection()){
   			GenreDAO gdao = new GenreDAO(conn);
   			List<Genre> Genres;
   			Genre genre = null;
   			int count = 0;
   			String input;
			while(genre == null) {
   	   			System.out.println("Choose Genre by keyword (leave blank to see a list of all genres)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Genres = gdao.readAllGenres();
   	   			} else {
   	   				Genres = gdao.readAllGenresByName(input);
   	   			}
   	   			/* create genre list to choose from */
   	   			if(Genres.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Genres.size() == 1) {
   	   				genre = Genres.get(0);
   	   			} else {
					System.out.println("Choose an Genre to update:");
					count = 1;
					for(Genre a: Genres) {
						System.out.println(count + ") " + a.getGenreName());
						count++;
					}
					System.out.print("Choose an genre to delete (from the integer list): ");
					int genreOption = 0;
					while(genreOption < 1 || genreOption >= count) {
						try {
							genreOption = scanner.nextInt();
							scanner.nextLine();
							if(genreOption < 1 || genreOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								genre = Genres.get(genreOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
			}
   	   		/* check to see if genre has dependencies */
			BookDAO bdao = new BookDAO(conn);
			/* probably doesn't work */
			int dependencies = bdao.checkBookSingleGenre(genre);
			if(dependencies == 0) {
				gdao.removeGenre(genre);
				System.out.println("Genre Deleted");
				return;
			} else {
				System.out.println("ERROR: Genre has " + dependencies + " dependencies");
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void readGenres() {
   		try (Connection conn = conUtil.getConnection()){
   			GenreDAO gdao = new GenreDAO(conn);
   			List<Genre> Genres = gdao.readAllGenres();
   			for(Genre g: Genres) {
   				g.adminPrint();;
   			}
   		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
   		}
   	}
	
	/* PUBLISHER FUNCTIONS */
	public void addPublisher() {
		boolean unique = false;
		String publisherName = "";
		String publisherAddress = "";
		String publisherPhone = "";
   		try (Connection conn = conUtil.getConnection()){
   			PublisherDAO pdao = new PublisherDAO(conn);
   			/* get publisher name */
   			while(!unique) {
   	   			System.out.println("New publisher name: ");
   	   			publisherName = scanner.nextLine();
   	   			if(publisherName.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			} else {
   	   				unique = !pdao.searchPublisherBoolean(publisherName);
   	   				if(!unique) {
   	   					System.out.println("Publisher name already exists");
   	   				}
   	   			}
   			}
   			/* get publisher address */
   			while(publisherAddress.isEmpty()) {
   	   			System.out.print("New publisher address:");
   	   			publisherAddress = scanner.nextLine();
   	   			if(publisherAddress.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			}
   	   		}
   			/* get publisher phone */
   			unique = false;
   			while(publisherPhone.isEmpty()) {
   	   			System.out.print("New publisher phone:");
   	   			publisherPhone = scanner.nextLine();
   	   			if(publisherPhone.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			}
   	   		}
   			pdao.addPublisher(publisherName, publisherAddress, publisherPhone);
   			System.out.println("Publisher added");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			System.out.println("Could not add publisher");
 			return;
 		}
	}
	public void updatePublisher() {
		boolean unique = false;
   		try (Connection conn = conUtil.getConnection()){
   			PublisherDAO pdao = new PublisherDAO(conn);
   			List<Publisher> Publishers;
   			Publisher publisher = null;
   			String input = "";
	   		int count = 1;
	   		/* get publisherId */
   			while(publisher == null) {
   	   			System.out.println("Choose Publisher by keyword (leave blank to see a list of all publishers)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Publishers = pdao.getPublishers();
   	   			} else {
   	   				Publishers = pdao.getPublishersByName(input);
   	   			}
   	   			/* create publisher list to choose from */
   	   			if(Publishers.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Publishers.size() == 1) {
   	   				publisher = Publishers.get(0);
   	   				System.out.println("Publisher name = " + publisher.getPublisherName() + ", "
   	   						+ publisher.getPublisherAddress() + ": " + publisher.getPublisherPhone());
   	   			} else {
					System.out.println("Publishers:");
					count = 1;
					for(Publisher p: Publishers) {
						System.out.println(count + ") " + p.getPublisherName() + ", " + p.getPublisherAddress() + ": " + p.getPublisherPhone());
						count++;
					}
					System.out.print("Choose a publisher to update (from the integer list): ");
					int publisherOption = 0;
					while(publisherOption < 1 || publisherOption >= count) {
						try {
							publisherOption = scanner.nextInt();
							scanner.nextLine();
							if(publisherOption < 1 || publisherOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								publisher = Publishers.get(publisherOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
   			}
   			/* update Publisher Name using publisherId and publisherName */
   			while(!unique) {
   	   			System.out.println("New Publisher Name: (leave blank if no change)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty() || input.equalsIgnoreCase(publisher.getPublisherName())) {
   	   				System.out.println("Continuing...");
   	   				unique = true;
   	   			} else {
   	   				unique = !pdao.searchPublisherBoolean(input);
   	   				if(!unique) {
   	   					System.out.print("Publisher with this name already exists");
   	   				}
   	   			}
   			}
   			publisher.setPublisherName(input);
   			/* update Publisher address */
	   		System.out.println("New Publisher Address: (leave blank if no change)");
	   		input = scanner.nextLine();
	   		if(input.isEmpty() || input.equalsIgnoreCase(publisher.getPublisherAddress())) {
	   			System.out.println("Continuing...");
	   		} else if(input.length() <= 45) {
	   			publisher.setPublisherAddress(input);
	   		} else {
	   			System.out.println("Address is too long... skipping");
	   		}
   			/* update Publisher phone */
	   		System.out.println("New Publisher Phone: (leave blank if no change)");
	   		input = scanner.nextLine();
	   		if(input.isEmpty() || input.equalsIgnoreCase(publisher.getPublisherPhone())) {
	   			System.out.println("Continuing...");
	   		} else if(input.length() <= 45) {
	   			publisher.setPublisherPhone(input);
	   		} else {
	   			System.out.println("Phone Number is too long... skipping");
	   		}
   			pdao.updatePublisher(publisher.getPublisherId(), publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone());
   			System.out.println("Publisher updated");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			e.printStackTrace();
 			System.out.println("Could not add publisher");
 			return;
 		}
	}
	
	public void deletePublisher( ){
   		try (Connection conn = conUtil.getConnection()){
   			PublisherDAO pdao = new PublisherDAO(conn);
   			List<Publisher> Publishers;
   			Publisher publisher = null;
   			int count = 0;
   			String input;
			while(publisher == null) {
   	   			System.out.println("Choose Publisher by keyword (leave blank to see a list of all publishers)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Publishers = pdao.getPublishers();
   	   			} else {
   	   				Publishers = pdao.getPublishersByName(input);
   	   			}
   	   			/* create publisher list to choose from */
   	   			if(Publishers.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Publishers.size() == 1) {
   	   				publisher = Publishers.get(0);
   	   			} else {
					System.out.println("Choose an Publisher to update:");
					count = 1;
					for(Publisher p: Publishers) {
						System.out.println(count + ") " + p.getPublisherName() + ", " + p.getPublisherAddress() + ": " + p.getPublisherPhone());
						count++;
					}
					System.out.print("Choose an publisher to delete (from the integer list): ");
					int publisherOption = 0;
					while(publisherOption < 1 || publisherOption >= count) {
						try {
							publisherOption = scanner.nextInt();
							scanner.nextLine();
							if(publisherOption < 1 || publisherOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								publisher = Publishers.get(publisherOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
			}
   	   		/* check to see if publisher has dependencies */
			BookDAO bdao = new BookDAO(conn);
			/* probably doesn't work */
			int dependencies = bdao.checkBookPublisherDependency(publisher);
			if(dependencies == 0) {
				pdao.deletePublisher(publisher);
				System.out.println("Publisher Deleted");
				return;
			} else {
				System.out.println("ERROR: Publisher has " + dependencies + " dependencies");
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void readPublishers() {
   		try (Connection conn = conUtil.getConnection()){
   			PublisherDAO pdao = new PublisherDAO(conn);
   			List<Publisher> Publishers = pdao.getPublishers();
   			for(Publisher p: Publishers) {
   				p.adminPrint();;
   			}
   		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
   		}
   	}
	/* BRANCH FUNCTIONS */
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
	/* BORROWER FUNCTIONS */
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
	/* OVERRIDE DUE DATE */
	public void overrideDueDate() {
		System.out.println("Printing all loans currently out");
   		try (Connection conn = conUtil.getConnection()){
   			LoanDAO ldao = new LoanDAO(conn);
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
   			   			LoanDAO ldao2 = new LoanDAO(conn);
   						ldao2.extendLoan(currentLoan);
   						System.out.println("Loan due date extended by 7 days");
   					}
   				}
   			}
   		} catch (ClassNotFoundException | SQLException| InputMismatchException e) {
			scanner.nextLine();
			return;
   		}
	}
	/*
	   		try (Connection conn = conUtil.getConnection()){
	  		//
	 			} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	*/

}