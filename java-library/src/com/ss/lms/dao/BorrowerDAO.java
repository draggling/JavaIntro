/**
 * 
 */
package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.Borrower;

/**
 * @author danwoo
 *
 */
public class BorrowerDAO extends BaseDAO<Borrower>{
	public BorrowerDAO(Connection conn) {
		super(conn);
	}
	
	public Borrower getBorrower(Integer cardNo) throws ClassNotFoundException, SQLException {
		return read("SELECT * from tbl_borrower WHERE cardNo = ?", new Object[] { cardNo }).get(0);
	}
	
	public void addBorrower(String borrowerName, String borrowerAddress, String borrowerPhone) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_borrower (name, address, phone) VALUES (?,?,?)", new Object[] { borrowerName, borrowerAddress, borrowerPhone});
	}
	
	public void updateBorrower(Borrower Borrower) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_borrower SET name = (?), address = (?) WHERE cardno = ?",
				new Object[] { Borrower.getName(), Borrower.getAddress(), Borrower.getCardNo()});
	}

	public void deleteBorrower(Borrower Borrower) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_borrower WHERE cardNo = ?", new Object[] { Borrower.getCardNo() });
	}

	public List<Borrower> readAllBorrowers() throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_borrower", null);
	}

	public List<Borrower> searchBorrower(Integer cardNo) throws SQLException, ClassNotFoundException {
		return read("SELECT * FROM tbl_borrower WHERE cardno = ?", new Object[] {cardNo});
	}
	
	public List<Borrower> readAllBorrowersByName(String borrowerName) throws SQLException, ClassNotFoundException {
		String searchString = "%" + borrowerName + "%";
		return read("SELECT * FROM tbl_borrower WHERE name LIKE (?)", new Object[] {searchString});
	}
	
	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Borrower> Borrowers = new ArrayList<>();
		while (rs.next()) {
			//	public Borrower(Integer cardNo, String name, String address, String phone) {
			Borrower b = new Borrower(rs.getInt("cardNo"), rs.getString("name"), rs.getString("address"), rs.getString("phone"));
			Borrowers.add(b);
		}
		return Borrowers;
	}
	



}
