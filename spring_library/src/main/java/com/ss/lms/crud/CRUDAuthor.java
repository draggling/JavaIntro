package com.ss.lms.crud;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.entity.Author;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public class CRUDAuthor {
	@Autowired
	public AuthorDAO adao;
	
	@Autowired
	public BookDAO bdao;
	Scanner scanner = new Scanner(System.in);
	
	@Transactional
	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addAuthor() {
		boolean unique = false;
   		try {;
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
   		try {
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
   		try {
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
			/* probably doesn't work */
			int dependencies = bdao.checkBookSingleAuthor(author);
			if(dependencies == 0) {
				adao.deleteAuthor(author);
				System.out.println("Author Deleted");
				return;
			} else {
				System.out.println("ERROR: Author has " + dependencies + " dependency/dependencies");
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void readAuthors() {
   		try {
   			List<Author> Authors = adao.readAllAuthors();
   			for(Author a: Authors) {
   				a.adminPrint();
   			}
   		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
   		}
   	}
}
