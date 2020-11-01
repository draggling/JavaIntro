package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.Borrower;

@Repository
public interface BorrowerRepo extends JpaRepository<Borrower, Integer>{
	
	@Query("FROM Borrower WHERE cardNo = :cardNo")
	public List<Borrower> borrowerExists(@Param("cardNo") Integer cardNo);
	

	/* add borrower */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO tbl_borrower (name, address, phone) values (:name, :address, :phone)", nativeQuery = true)
	public void addBorrower(@Param("name") String name, @Param("address") String address, @Param("phone") String phone);
	
	/* read borrowers */
	@Query(value = "FROM Borrower")
	public List<Borrower> readBorrowers();
	
	/* update borrower name */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_borrower SET name = :name, address = :address, phone = :phone "
			+ "WHERE cardNo = :cardNo", nativeQuery = true)
	public void updateBorrower(@Param("cardNo") Integer cardNo, @Param("name") String name,
			@Param("address") String address, @Param("phone") String phone);
	
	/* delete borrower */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM tbl_borrower WHERE cardNo = :cardNo", nativeQuery = true)
	public void deleteBorrower(@Param("cardNo") Integer cardNo);
}