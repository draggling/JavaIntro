/**
 * 
 */
package com.ss.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.ss.lms.entity.Genre;

/**
 * @author danwoo
 *
 */

@Repository
public class GenreDAO extends BaseDAO<Genre> implements ResultSetExtractor<List<Genre>> {

	
	public Genre getGenre(Integer genreId) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_genre WHERE genreId = ?", new Object[] { genreId }, this).get(0);
	}
	
	public void addGenre(String genreName) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_genre (genreName) VALUES (?)",
				new Object[] {genreName});
	}	
	
	public void removeGenre(Genre genre) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_genre WHERE genreId = ?", new Object[] { genre.getGenreId() });
	}
	public List<Genre> getGenresByName(String genreName) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_genre WHERE genreName = (?)", new Object[] {genreName}, this);
	}
	
	public void updateGenre(Genre genre) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_genre SET genreName = (?) WHERE genreId = ?",
				new Object[] {genre.getGenreName(), genre.getGenreId()});
	}
	
	public List<Genre> readAllGenres() throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_genre", this);
	}
	public List<Genre> readAllGenresByName(String searchString) throws SQLException, ClassNotFoundException {
		searchString = "%"+searchString+"%";
		return jdbcTemplate.query("SELECT * FROM tbl_genre WHERE genreName LIKE (?)", 
				new Object[] {searchString}, this);
	}
	public List<Genre> searchGenre(String searchString) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_genre WHERE genreName = ?",
				new Object[] {searchString}, this);
	}
	
	public boolean searchGenreBoolean(String genreName) throws SQLException, ClassNotFoundException {
		if(jdbcTemplate.query("SELECT * FROM tbl_genre WHERE genreName = (?)", 
				new Object[] {genreName}, this).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean findBookGenrePair(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException {
		if(jdbcTemplate.query("SELECT * FROM tbl_book_genres WHERE bookId = ? and genreId = ?", 
				new Object[] {bookId, genreId}, this).size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addBookGenrePair(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_book_genres (bookId, genreId) VALUES (?, ?)", new Object[] { bookId, genreId });
	}
	
	public void deleteBookGenrePair(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_book_genres WHERE bookID = ? and genreId = ?", new Object[] { bookId, genreId });
	}
	
	public List<Genre> findBookGenres(Integer bookId) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_genre g WHERE "
				+ "EXISTS (SELECT * FROM tbl_book_genres bg WHERE bg.genreId = g.genreId and bg.bookId = ?)",
				new Object[] {bookId}, this);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		List<Genre> Genres = new ArrayList<>();
		while (rs.next()) {
			Genre p = new Genre(rs.getInt("genreId"), rs.getString("genreName"));
			Genres.add(p);
		}
		return Genres;
	}
}
