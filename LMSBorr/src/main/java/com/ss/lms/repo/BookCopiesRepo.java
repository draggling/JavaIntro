package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ss.lms.entity.BookCopies;

@Repository
public interface BookCopiesRepo extends JpaRepository<BookCopies, Id> {
	/* read all rows in BookCopies table */
	@Query( "FROM BookCopies ")
	public List<BookCopies> readBookCopies();
	
	/* get BookCopies for a book-branch pair */
	@Query( "FROM BookCopies WHERE bookId = :bookId and branchId = :branchId")
	public List<BookCopies> bookCopiesExist(@Param("bookId") Integer bookId, @Param("branchId") Integer branchId);
	
}
