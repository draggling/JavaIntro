package com.ss.lms.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer>{
	
	/* read book by title */
	@Query( "FROM Book WHERE title LIKE CONCAT('%',:title,'%')")
	public List<Book> readBooksByTitle(@Param("title") String title);
	
	/* read book by id */
	@Query( "FROM Book WHERE bookId = :bookId")
	public List<Book> readBooksByBookId(@Param("bookId") Integer bookId);
	
	/* add book */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value ="INSERT INTO tbl_book (title, pubId) values (:title, :pubId)", nativeQuery = true)
	public void addBook(@Param("title") String title, @Param("pubId") Integer publisherId);
	
	/* update book title */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE Book SET title = :newTitle WHERE bookId = :bookId")
	public void updateBookTitle(@Param("bookId") Integer bookId, @Param("newTitle") String newTitle);
	
	/* delete book by id*/
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM Book WHERE bookId = :bookId")	/* add book author by id */
	public void deleteBook(@Param("bookId") Integer bookId);
	
	
	/* update book publisher id */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE Book SET pubId = :pubId WHERE bookId = :bookId")	/* add book author by id */
	public void updateBookPublisher(@Param("bookId") Integer bookId, @Param("pubId") Integer publisherId);

	/* add book author by id */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value ="INSERT INTO tbl_book_authors (bookId, authorId) values (:bookId, :authorId)", nativeQuery = true)
	public void addBookAuthor(@Param("bookId") Integer bookId, @Param("authorId") Integer authorId);

	/* delete book author by id */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM BookAuthors WHERE bookId = :bookId and authorId = :authorId")	/* add book author by id */
	public void deleteBookAuthor(@Param("bookId") Integer bookId, @Param("authorId") Integer authorId);
	

}
