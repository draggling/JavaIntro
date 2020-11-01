package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.BookCopies;

@Repository
public interface BookCopiesRepo extends JpaRepository<BookCopies, Id> {
	/* read all rows in BookCopies table */
	@Query( "FROM BookCopies ")
	public List<BookCopies> readBookCopies();
	
	/* get BookCopies for a book-branch pair */
	@Query( "FROM BookCopies WHERE bookId = :bookId and branchId = :branchId")
	public List<BookCopies> bookCopiesExist(@Param("bookId") Integer bookId, @Param("branchId") Integer branchId);
	
	/* add new BookCopies row using book and branch pair */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) values (:bookId, :branchId, :noOfCopies)", nativeQuery = true)
	public void addNewBookCopies(@Param("bookId") Integer bookId, @Param("branchId") Integer branchId, @Param("noOfCopies") Integer noOfCopies);
	
	/* update prexisting row */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_book_copies SET noOfCopies = noOfCopies + :noOfCopies WHERE bookId = :bookId and branchId = :branchId", nativeQuery = true)
	public void addExistingBookCopies(@Param("bookId") Integer bookId, @Param("branchId") Integer branchId, @Param("noOfCopies") Integer noOfCopies);
}
