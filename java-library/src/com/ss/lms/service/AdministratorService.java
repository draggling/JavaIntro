package com.ss.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BranchDAO;
import com.ss.lms.dao.GenreDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.dao.BookDAO;

import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;

public class AdministratorService {

	public ConnectionUtil conUtil = new ConnectionUtil();
	Scanner scanner = new Scanner(System.in);

	/* BOOK FUNCTIONS */
	public void addBook() throws ClassNotFoundException, SQLException {
		String title = "";
		Integer publisherId = 0;
		String publisherName = "";
		List<Author> authors = new ArrayList<>();
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
						}
					} else {
						System.out.println("ERROR - Exiting");
						return;
					}
				}
			} catch(InputMismatchException | SQLException e) {
				System.out.println("Invalid input. Please choose a number from 1 to 5");
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
			BookDAO bdao = new BookDAO(conn);
			PublisherDAO pdao = new PublisherDAO(conn);
			AuthorDAO adao = new AuthorDAO(conn);
			GenreDAO gdao = new GenreDAO(conn);
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
									PublisherDAO pdao2 = new PublisherDAO(conn3);
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
										}
									}
								} catch(NumberFormatException | InputMismatchException e) {
									e.printStackTrace();
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
								System.out.println("Input a ");
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
								System.out.println("Input a ");
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
								BookDAO bdao3 = new BookDAO(conn);
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
			BookDAO bdao = new BookDAO(conn);
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
	/* GENRE FUNCTIONS */
	/* PUBLISHER FUNCTIONS */
	/* BRANCH FUNCTIONS */
	/* BOOK COPY FUNCTIONS */
	/* BORROWER FUNCTIONS */
	/* OVERRIDE DUE DATE */

}