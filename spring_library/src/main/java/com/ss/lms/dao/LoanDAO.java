/**
 * 
 */
package com.ss.lms.dao;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import com.ss.lms.entity.Loan;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.Branch;

/**
 * @author danwoo
 *
 */

@Repository
public class LoanDAO extends BaseDAO<Loan> implements ResultSetExtractor<List<Loan>>{
	
	public Loan getLoan(Integer bookId, Integer branchId, Integer cardNo) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * from tbl_book_loans WHERE bookId = ? and branchId = ? and cardNo = ?", 
				new Object[] { bookId, branchId, cardNo }, this).get(0);
	}
	
	/* check out a book, assuming book is available to check out */
	public void checkOutBook(Loan loan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) VALUES (?,?,?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY))", 
				new Object[] { loan.getBookId(), loan.getBranchId(), loan.getCardNo()});
	}
	
	/* return a book */
	public void checkInBook(Loan Loan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book_loans SET dateIn = NOW() WHERE bookId = (?) and branchId = (?) and cardNo = ?",
				new Object[] { Loan.getBookId(), Loan.getBranchId(), Loan.getCardNo()});
	}
	
	/* extend loan by 1 week */
	public void extendLoan(Loan loan) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book_loans SET dueDate = date_add(dueDate, INTERVAL 7 DAY) WHERE bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { loan.getBookId(), loan.getBranchId(), loan.getCardNo()});
	}
	
	public void extendLoan(Loan loan, Integer days) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book_loans SET dueDate = date_add(dueDate, INTERVAL ? DAY) WHERE bookId = ? and branchId = ? and cardNo = ?",
				new Object[] {days, loan.getBookId(), loan.getBranchId(), loan.getCardNo()});
	}
	
	/* all active loans in a specific branch */
	public List<Loan> readAllActiveBranchLoans(Branch branch) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans WHERE branchNo = ? and dateIn IS NULL", 
				new Object[] {branch.getBranchId()}, this);
	}

	/* all active loans for a specific borrower */
	public List<Loan> readAllActiveBorrowerLoans(Integer cardNo) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans WHERE cardNo = ? and dateIn IS NULL", 
				new Object[] {cardNo}, this);
	}
	
	public boolean expiredLoanExists(Integer cardNo, Integer branchId, Integer bookId) throws SQLException, ClassNotFoundException {
		if(jdbcTemplate.query("SELECT * FROM tbl_book_loans WHERE cardNo = ? and branchId = ? and bookId = ?", 
				new Object[] {cardNo, branchId, bookId}, this).size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public void updateExpiredLoan(Integer cardNo, Integer branchId, Integer bookId) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_book_loans SET dateOut = NOW(), dueDate = (DATE_ADD(NOW(), INTERVAL 7 DAY)), dateIn = NULL "
				+ "WHERE cardNo = ? and branchId = ? and bookId = ? ", new Object[] {cardNo, branchId, bookId}); 
	}
	
	/* all active loans */
	public List<Loan> readAllActiveLoans() throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans WHERE dateIn IS NULL", this);
	}
	
	public int checkBranchLoanDependency(Branch branch) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans WHERE branchId = ? and DateIn IS NULL",
				new Object[] {branch.getBranchId()}, this).size();
	}
	
	public int checkBorrowerLoanDependency(Borrower borrower) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans WHERE cardNo = ? and DateIn IS NULL",
				new Object[] {(borrower.getCardNo())}, this).size();
	}
	
	public int getBranchBookLoans(Branch branch, Book book) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_book_loans WHERE branchId = ? and bookId = ? and DateIn IS NULL",
				new Object[] {branch.getBranchId(), book.getBookId()}, this).size(); 
	}
	
	@Override
	public List<Loan> extractData(ResultSet rs) throws SQLException {
		List<Loan> Loans = new ArrayList<>();
		while (rs.next()) {
			//	public Loan(Integer cardNo, String name, String address, String phone) {
			Loan b = new Loan(rs.getInt("bookId"), rs.getInt("branchId"), rs.getInt("cardNo"), rs.getDate("dateOut"), rs.getDate("dueDate"), rs.getDate("dateIn"));
			Loans.add(b);
		}
		return Loans;
	}
	



}
