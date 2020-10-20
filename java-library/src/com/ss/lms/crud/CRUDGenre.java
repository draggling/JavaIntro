package com.ss.lms.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.GenreDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.entity.Genre;
import com.ss.lms.service.ConnectionUtil;

public class CRUDGenre {
	public ConnectionUtil conUtil = new ConnectionUtil();
	Scanner scanner = new Scanner(System.in);
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
}
