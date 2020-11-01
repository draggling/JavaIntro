package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.Branch;

@Repository
public interface BranchRepo extends JpaRepository<Branch, Integer>{
	
	@Query("FROM Branch WHERE branchId = :branchId")
	public List<Branch> branchExists(@Param("branchId") Integer branchId);
	

	/* add branch */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO tbl_branch (branchName, branchAddress) values (:branchName, :branchAddress)", nativeQuery = true)
	public void addBranch(@Param("branchName") String branchName, @Param("branchAddress") String branchAddress);
	
	/* read branches */
	@Query(value = "FROM Branch")
	public List<Branch> readBranches();
	
	/* update branch name */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_branch SET branchName = :branchName, branchAddress = :branchAddress "
			+ "WHERE branchId = :branchId", nativeQuery = true)
	public void updateBranch(@Param("branchId") Integer branchId, @Param("branchName") String branchName,
			@Param("branchAddress") String branchAddress);
	
	/* delete branch */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM tbl_branch WHERE branchId = :branchId", nativeQuery = true)
	public void deleteBranch(@Param("branchId") Integer branchId);
}