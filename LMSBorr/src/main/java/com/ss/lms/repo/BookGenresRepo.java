package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.BookGenres;

/* WIP */
public interface BookGenresRepo extends JpaRepository<BookGenres, Id> {
	/* read all rows in BookGenre table */
	@Query( "FROM BookGenres ")
	public List<BookGenres> readBookGenres();
	
	/* check if bookId and genreId pair is in the table */
	@Query( "FROM BookGenres WHERE bookId = :bookId and genreId = :genreId")
	public List<BookGenres> readBookGenrePair(@Param("bookId") Integer bookId, @Param("genreId") Integer genreId);
	
	/* add book genre by id */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value ="INSERT INTO tbl_book_genres (bookId, genreId) values (:bookId, :genreId)", nativeQuery = true)
	public void addBookGenre(@Param("bookId") Integer bookId, @Param("genreId") Integer genreId);
	
	/* delete book genre by id */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM tbl_book_genres WHERE bookId = :bookId and genreId = :genreId", nativeQuery = true)	/* add book author by id */
	public void deleteBookGenre(@Param("bookId") Integer bookId, @Param("genreId") Integer genreId);
}
