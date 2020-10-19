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
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Publisher;

/**
 * @author danwoo
 *
 */
public class PublisherDAO extends BaseDAO<Publisher>{
	public PublisherDAO(Connection conn) {
		super(conn);
	}
	
	public Publisher getPublisher(Integer publisherId) throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM tbl_publisher WHERE publisherId = ?", new Object[] { publisherId }).get(0);
	}
	
	public void addPublisher(String publisherName, String publisherAddress, String publisherPhone) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES (?,?,?)",
				new Object[] {publisherName, publisherAddress, publisherPhone});
	}	
	
	public void updatePublisher(Integer publisherId, String publisherName, String publisherAddress, String publisherPhone) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_publisher SET publisherName = (?) , publisherAddress = (?) , publisherPhone = (?) WHERE publisherId = ?",
				new Object[] {publisherName, publisherAddress, publisherPhone, publisherId});
	}
	public List<Publisher> getPublishersByName(String publisherName) throws ClassNotFoundException, SQLException {
		String searchString = "%"+publisherName+"%";
		return read("SELECT * FROM tbl_publisher WHERE publisherName LIKE (?)", new Object[] {searchString});
	}
	
	public List<Publisher> getPublishersByBook(Integer bookId) throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM tbl_publisher p WHERE EXISTS ("
				+ "SELECT * FROM tbl_book b WHERE b.pubId = p.publisherId and b.bookId = ?)", 
				new Object[] {bookId});
	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Publisher> Publishers = new ArrayList<>();
		while (rs.next()) {
			Publisher p = new Publisher(rs.getInt("publisherId"), rs.getString("publisherName"), rs.getString("publisherAddress"), rs.getString("publisherPhone"));
			Publishers.add(p);
		}
		return Publishers;
	}
}
