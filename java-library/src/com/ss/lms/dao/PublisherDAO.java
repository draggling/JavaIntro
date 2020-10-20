/**
 * 
 */
package com.ss.lms.dao;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	
	public boolean searchPublisherBoolean(String publisherName) throws SQLException, ClassNotFoundException {
		if(read("SELECT * FROM tbl_publisher WHERE publisherId = (?)", new Object[] {publisherName}).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addPublisher(String publisherName, String publisherAddress, String publisherPhone) throws ClassNotFoundException, SQLException {
		save("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES (?,?,?)",
				new Object[] {publisherName, publisherAddress, publisherPhone});
	}	
	
	public void updatePublisher(Integer publisherId, String publisherName, String publisherAddress, String publisherPhone) throws ClassNotFoundException, SQLException {
		save("UPDATE tbl_publisher SET publisherName = (?) , publisherAddress = (?) , publisherPhone = (?) WHERE publisherId = ?",
				new Object[] {publisherName, publisherAddress, publisherPhone, publisherId});
	}
	
	public void deletePublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		save("DELETE FROM tbl_publisher WHERE publisherId = ?", new Object[] { publisher.getPublisherId() });
	}
	
	public List<Publisher> getPublishers() throws ClassNotFoundException, SQLException {
		return read("SELECT * FROM tbl_publisher", null);
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
