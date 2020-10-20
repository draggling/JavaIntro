package com.ss.lms.service;

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
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopies;
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;

public class LibrarianService {
	Scanner scanner = new Scanner(System.in);
	public ConnectionUtil conUtil = new ConnectionUtil();

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
	public List<Branch> getBranches() {
		try(Connection conn = conUtil.getConnection()) {
			BranchDAO brdao = new BranchDAO(conn);
				return brdao.readAllBranches();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public boolean UpdateLibrary(Branch branch) {
		System.out.print("New Library Name (leave blank for no change): ");
		String name = scanner.nextLine();
		if(!name.isEmpty() && name.length() <= 45) {
				branch.setBranchName(name);
		}
		System.out.print("New Library Address (leave blank for no change): ");
		String address = scanner.nextLine();
		if(!address.isEmpty() && address.length() <= 45) {
			branch.setBranchAddress(address);
		}
		/* commit update to database */
		try(Connection conn = conUtil.getConnection()) {
			BranchDAO brdao = new BranchDAO(conn);
			brdao.updateBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println("error: branch could not be updated");
			return false;
		}
		System.out.println("Library branch successfully changed!");
		return true;
	}
	
	/* librarian add book copies option */
	public Boolean AddBookCopies(Branch branch) {
		int counter = 1;
		System.out.println("Pick the Book you want to add copies of to your branch:");
		List<Book> branchBooks = new ArrayList<>();
		try(Connection conn = conUtil.getConnection()) {
			BookDAO bdao = new BookDAO(conn);
			branchBooks = bdao.readAllBooks();
			//branchBooks = bdao.readAllBranchBooks(branch);
			for(Book b : branchBooks) {
				b = readBook(b);
				System.out.println(counter + ") " + b.toString());
				counter++;
			}
			System.out.println(counter + ") " + "Return to Main Menu");
			while(true) {
				try {
					int input = scanner.nextInt();
					if(input == counter) {
						return true;
					} else if(input > 0 && input < counter) {
						Book b = branchBooks.get(input - 1);
						boolean newRow = true;
						if(bdao.checkIfBookInBranch(branch, b)) {
							/* update row if false, insert row if true */
							newRow = false;
						}
						if(newRow) {
							System.out.println("No are currently no copies of this book");
						} else {
							BookCopiesDAO bcdao = new BookCopiesDAO(conn);
							List<BookCopies> tempCopies = bcdao.findBookCopiesByBranch(b, branch);
							if(tempCopies.size() == 0) {
								System.out.println("ERROR with copies retrieval: no copies found... exiting");
								return false;
							} else if(tempCopies.size() == 1) {
								System.out.println("There are  " + tempCopies.get(0).getNoOfCopies() + " copies of this book");
							} else {
								System.out.println("ERROR with copies retrieval: multiple copy records found... exiting");
							}
						}
						System.out.print("How many copies do you want to add? ");
						try {
							while(true) {
								int copies = scanner.nextInt();
								if(copies > 0) {
									if(!newRow) {
										bdao.addBookCopies(b, branch.getBranchId(), copies);
									} else {
										bdao.addNewBookCopies(b, branch.getBranchId(), copies);
									}
									System.out.println("Copies added");
									conn.commit();
									return true;
								} else if(copies == 0) {
									System.out.println("No copies added");
									return true;
								} else {
									System.out.print("Invalid input. Please choose a positive number or 0 ");
								}
							}
						} catch(NumberFormatException | InputMismatchException e) {
							System.out.print("Invalid input. Please choose a positive number or 0 ");
						}
							/* add extra copies */
					}
				} catch(NumberFormatException e) {
					System.out.println("Invalid input. Please choose a number from 1 to " + counter);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println("error: branch inventory could not be retrieved");
			return false;
		}
	}
}