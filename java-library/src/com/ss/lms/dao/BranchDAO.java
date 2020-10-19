/**
 * 
 */
package com.ss.lms.dao;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Branch;
//import com.ss.lms.entity.Book;

/**
 * @Branch danwoo
 *
 */
public class BranchDAO extends BaseDAO<Branch>{

	public BranchDAO(Connection conn) {
		super(conn);
	}

	public Branch getBranch(Integer branchId) throws ClassNotFoundException, SQLException {
		return read("SELECT * from tbl_branch WHERE branchId = ?", new Object[] { branchId }).get(0);
	}
	
	public boolean searchBranchBoolean(String branchName) throws SQLException, ClassNotFoundException {
		if(read("SELECT * FROM tbl_branch WHERE branchName = (?)", new Object[] {branchName}).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addBranch(Branch Branch) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_branch (branchName, branchAddress) VALUES (?,?)", new Object[] { Branch.getBranchName(), Branch.getBranchAddress() });
	}
	
	public void addBranch(String branchName, String branchAddress) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_branch (branchName, branchAddress) VALUES (?,?)", new Object[] { branchName, branchAddress });
	}
	
	/* adds book copies to branch */
	public Integer addBookWithPk(Branch Branch) throws ClassNotFoundException, SQLException {
		return saveWithPk("INSERT INTO tbl_branch (branchName, branchAddress) VALUES (?)", new Object[] { Branch.getBranchName(), Branch.getBranchAddress() });
	}

	public void updateBranch(Branch Branch) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_branch SET branchName = (?), branchAddress = (?) WHERE BranchId = ?",
				new Object[] { Branch.getBranchName(), Branch.getBranchAddress(), Branch.getBranchId() });
	}

	public void deleteBranch(Branch Branch) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_branch WHERE BranchId = ?", new Object[] { Branch.getBranchId() });
	}

	public List<Branch> readAllBranches() throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_branch", null);
	}

	public List<Branch> readAllBranchesByName(String searchString) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_branch WHERE title = ?", new Object[] {searchString});
	}

	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Branch> Branches = new ArrayList<>();
		while (rs.next()) {
			Branch b = new Branch(rs.getInt("branchId"), rs.getString("branchName"), rs.getString("branchAddress"));
			Branches.add(b);
		}
		return Branches;
	}
}