/**
 * 
 */
package com.ss.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.ss.lms.entity.Borrower;

/**
 * @author danwoo
 *
 */
public class BorrowerDAO extends BaseDAO<Borrower>  implements ResultSetExtractor<List<Borrower>>{
	
	public Borrower getBorrower(Integer cardNo) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * from tbl_borrower WHERE cardNo = ?", new Object[] { cardNo }, this).get(0);
	}
	
	public void addBorrower(String borrowerName, String borrowerAddress, String borrowerPhone) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_borrower (name, address, phone) VALUES (?,?,?)", new Object[] { borrowerName, borrowerAddress, borrowerPhone});
	}
	
	public void updateBorrower(Borrower Borrower) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_borrower SET name = (?), address = (?) WHERE cardno = ?",
				new Object[] { Borrower.getName(), Borrower.getAddress(), Borrower.getCardNo()});
	}

	public void deleteBorrower(Borrower Borrower) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_borrower WHERE cardNo = ?", new Object[] { Borrower.getCardNo() });
	}

	public List<Borrower> readAllBorrowers() throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_borrower", this);
	}

	public List<Borrower> searchBorrower(Integer cardNo) throws SQLException, ClassNotFoundException {
		return jdbcTemplate.query("SELECT * FROM tbl_borrower WHERE cardno = ?", new Object[] {cardNo}, this);
	}
	
	public List<Borrower> readAllBorrowersByName(String borrowerName) throws SQLException, ClassNotFoundException {
		String searchString = "%" + borrowerName + "%";
		return jdbcTemplate.query("SELECT * FROM tbl_borrower WHERE name LIKE (?)", new Object[] {searchString}, this);
	}
	
	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> Borrowers = new ArrayList<>();
		while (rs.next()) {
			//	public Borrower(Integer cardNo, String name, String address, String phone) {
			Borrower b = new Borrower(rs.getInt("cardNo"), rs.getString("name"), rs.getString("address"), rs.getString("phone"));
			Borrowers.add(b);
		}
		return Borrowers;
	}
	



}
