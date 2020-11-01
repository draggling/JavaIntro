package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.Loan;

public interface LoanRepo extends JpaRepository<Loan, Id> {
	/* read all rows in Loan table */
	@Query( "FROM Loan ")
	public List<Loan> readLoan();
	
	/* check if loan exists */
	@Query( "FROM Loan WHERE cardNo = :cardNo and bookId = :bookId and branchId = :branchId")
	public List<Loan> loanExists(@Param("cardNo") Integer cardNo, @Param("bookId") Integer bookId, @Param("branchId") Integer branchId);
	
	/* check if loan is currently checked out */
	@Query( "FROM Loan WHERE cardNo = :cardNo and branchId = :branchId and bookId = :bookId and dateIn IS NULL")
	public List<Loan> bookCheckedOut(@Param("cardNo") Integer cardNo, @Param("bookId") Integer bookId, @Param("branchId") Integer branchId);
	
	/* extend loan due date by X days */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_book_loans SET dueDate = date_add(dueDate, INTERVAL :days DAY) WHERE cardNo = :cardNo and bookId = :bookId and branchId = :branchId", nativeQuery = true)	/* add book author by id */
	public void overrideDueDate(@Param("cardNo") Integer cardNo, @Param("bookId") Integer bookId, @Param("branchId") Integer branchId, @Param("days") Integer days);
	
	/* check out new loan - due date is in a week*/
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value ="INSERT INTO tbl_book_loans (cardNo, bookId, branchId, dateOut, dueDate) "
			+ "values (:cardNo, :bookId, :branchId,  NOW(), )", nativeQuery = true)
	public void addLoan(@Param("cardNo") Integer cardNo, @Param("bookId") Integer bookId, @Param("branchId") Integer branchId);
	
	/* update pre-existing loan - dateOut = now, dateIn = null, dueDate is in a week */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_book_loans SET dateOut = NOW(), dueDate = DATE_ADD(NOW(), INTERVAL 7 DAY, dateIn = NULL "
			+ "WHERE cardNo = :cardNo and bookId = :bookId and branchId = :branchId", nativeQuery = true)	/* add book author by id */
	public void updateLoan(@Param("cardNo") Integer cardNo, @Param("bookId") Integer bookId, @Param("branchId") Integer branchId);
	
	/* check in book */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_book_loans SET dateIn = NOW() WHERE cardNo = :cardNo and bookId = :bookId and branchId = :branchId", nativeQuery = true)
	public void checkInBook(@Param("cardNo") Integer cardNo, @Param("bookId") Integer bookId, @Param("branchId") Integer branchId);
}
