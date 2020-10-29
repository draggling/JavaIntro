/**
 * 
 */
package com.ss.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.ss.lms.entity.Branch;
//import com.ss.lms.entity.Book;

/**
 * @Branch danwoo
 *
 */
public class BranchDAO extends BaseDAO<Branch> implements ResultSetExtractor<List<Branch>>{

	public Branch getBranch(Integer branchId) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * from tbl_branch WHERE branchId = ?", 
				new Object[] { branchId },this).get(0);
	}
	
	public boolean searchBranchBoolean(String branchName) throws SQLException, ClassNotFoundException {
		if(jdbcTemplate.query("SELECT * FROM tbl_branch WHERE branchName = (?)", 
				new Object[] {branchName}, this).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addBranch(Branch Branch) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_branch (branchName, branchAddress) VALUES (?,?)", new Object[] { Branch.getBranchName(), Branch.getBranchAddress() });
	}
	
	public void addBranch(String branchName, String branchAddress) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_branch (branchName, branchAddress) VALUES (?,?)", new Object[] { branchName, branchAddress });
	}
	
	/* adds book copies to branch */
	/*
	public Integer addBookWithPk(Branch Branch) throws ClassNotFoundException, SQLException {
		return saveWithPk("INSERT INTO tbl_branch (branchName, branchAddress) VALUES (?)", new Object[] { Branch.getBranchName(), Branch.getBranchAddress() });
	}
	*/

	public void updateBranch(Branch Branch) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_branch SET branchName = (?), branchAddress = (?) WHERE BranchId = ?",
				new Object[] { Branch.getBranchName(), Branch.getBranchAddress(), Branch.getBranchId() });
	}

	public void deleteBranch(Branch Branch) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_branch WHERE BranchId = ?", new Object[] { Branch.getBranchId() });
	}

	public List<Branch> readAllBranches() throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_branch", this);
	}

	public List<Branch> readAllBranchesByName(String searchString) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_branch WHERE title = ?", 
				new Object[] {searchString}, this);
	}

	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException {
		List<Branch> Branches = new ArrayList<>();
		while (rs.next()) {
			Branch b = new Branch(rs.getInt("branchId"), rs.getString("branchName"), rs.getString("branchAddress"));
			Branches.add(b);
		}
		return Branches;
	}
}