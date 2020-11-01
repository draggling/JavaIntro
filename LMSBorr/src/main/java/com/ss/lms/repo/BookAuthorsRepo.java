package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.BookAuthors;

/* WIP */
public interface BookAuthorsRepo extends JpaRepository<BookAuthors, Id> {
	/* read all rows in BookAuthor table */
	@Query( "FROM BookAuthors ")
	public List<BookAuthors> readBookAuthors();
	
	/* check if bookId and authorId pair is in the table */
	@Query( "FROM BookAuthors WHERE bookId = :bookId and authorId = :authorId")
	public List<BookAuthors> readBookAuthorPair(@Param("bookId") Integer bookId, @Param("authorId") Integer authorId);
	
	/* add book author by id */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value ="INSERT INTO tbl_book_authors (bookId, authorId) values (:bookId, :authorId)", nativeQuery = true)
	public void addBookAuthor(@Param("bookId") Integer bookId, @Param("authorId") Integer authorId);
	
	/* delete book author by id */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM tbl_book_authors WHERE bookId = :bookId and authorId = :authorId", nativeQuery = true)	/* add book author by id */
	public void deleteBookAuthor(@Param("bookId") Integer bookId, @Param("authorId") Integer authorId);
}
