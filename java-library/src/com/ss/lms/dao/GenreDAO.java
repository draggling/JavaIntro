/**
 * 
 */
package com.ss.lms.dao;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Genre;

/**
 * @author danwoo
 *
 */
public class GenreDAO extends BaseDAO<Genre>{
	public GenreDAO(Connection conn) {
		super(conn);
	}
	
	public Genre getGenre(Integer genreId) throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM tbl_genre WHERE genreId = ?", new Object[] { genreId }).get(0);
	}
	
	public void addGenre(String genreName) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_genre (genreName) VALUES (?)",
				new Object[] {genreName});
	}	
	
	public void removeGenre(Genre genre) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_genre WHERE genreId = ?", new Object[] { genre.getGenreId() });
	}
	public List<Genre> getGenresByName(String genreName) throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM tbl_genre WHERE genreName = (?)", new Object[] {genreName});
	}
	
	public void updateGenre(Genre genre) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_genre SET genreName = (?) WHERE genreId = ?",
				new Object[] {genre.getGenreName(), genre.getGenreId()});
	}
	
	public List<Genre> readAllGenres() throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_genre", null);
	}
	public List<Genre> readAllGenresByName(String searchString) throws SQLException, ClassNotFoundException {
		searchString = "%"+searchString+"%";
		return read("SELECT * FROM tbl_genre WHERE genreName LIKE (?)", new Object[] {searchString});
	}
	public List<Genre> searchGenre(String searchString) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_genre WHERE genreName = ?", new Object[] {searchString});
	}
	
	public boolean searchGenreBoolean(String genreName) throws SQLException, ClassNotFoundException {
		if(read("SELECT * FROM tbl_genre WHERE genreName = (?)", new Object[] {genreName}).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean findBookGenrePair(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException {
		if(read("SELECT * FROM tbl_book_genres WHERE bookId = ? and genreId = ?", new Object[] {bookId, genreId}).size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addBookGenrePair(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_book_genres (bookId, genreId) VALUES (?, ?)", new Object[] { bookId, genreId });
	}
	
	public void deleteBookGenrePair(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_book_genres WHERE bookID = ? and genreId = ?", new Object[] { bookId, genreId });
	}
	
	public List<Genre> findBookGenres(Integer bookId) throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM tbl_genre g WHERE "
				+ "EXISTS (SELECT * FROM tbl_book_genres bg WHERE bg.genreId = g.genreId and bg.bookId = ?)",
				new Object[] {bookId});
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Genre> Genres = new ArrayList<>();
		while (rs.next()) {
			Genre p = new Genre(rs.getInt("genreId"), rs.getString("genreName"));
			Genres.add(p);
		}
		return Genres;
	}
}
