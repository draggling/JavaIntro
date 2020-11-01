package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.Genre;

@Repository
public interface GenreRepo extends JpaRepository<Genre, Integer>{
	
	@Query( "FROM Genre WHERE genreName = :genreName")
	public List<Genre> readGenreByNamee(@Param("genreName") String genreName);

	@Query( "FROM BookGenres WHERE genreId = :genreId")
	public List<Genre> readGenreById(@Param("genreId") Integer genreId);
	
	
	/* add genre */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO tbl_genre (genreName) values (:genreName)", nativeQuery = true)
	public void addGenre(@Param("genreName") String genreName);
	
	/* read genres */
	@Query(value = "FROM Genre WHERE genreName = genreName")
	public List<Genre> readGenres();
	
	/* update genre name */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_genre SET genreName = :genreName WHERE genreId = :genreId", nativeQuery = true)
	public void updateGenre(@Param("genreId") Integer genreId, @Param("genreName") String genreName);
	
	/* delete genre */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM tbl_genre WHERE genreId = :genreId", nativeQuery = true)
	public void deleteGenre(@Param("genreId") Integer genreId);
}
