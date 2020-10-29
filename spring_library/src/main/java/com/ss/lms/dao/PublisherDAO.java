/**
 * 
 */
package com.ss.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.ss.lms.entity.Publisher;

/**
 * @author danwoo
 *
 */
public class PublisherDAO extends BaseDAO<Publisher> implements ResultSetExtractor<List<Publisher>>{
	
	public Publisher getPublisher(Integer publisherId) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_publisher WHERE publisherId = ?", new Object[] { publisherId }, this).get(0);
	}
	
	
	public boolean searchPublisherBoolean(String publisherName) throws SQLException, ClassNotFoundException {
		if(jdbcTemplate.query("SELECT * FROM tbl_publisher WHERE publisherId = (?)", new Object[] {publisherName}, this).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addPublisher(String publisherName, String publisherAddress, String publisherPhone) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES (?,?,?)",
				new Object[] {publisherName, publisherAddress, publisherPhone});
	}	
	
	public void updatePublisher(Integer publisherId, String publisherName, String publisherAddress, String publisherPhone) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("UPDATE tbl_publisher SET publisherName = (?) , publisherAddress = (?) , publisherPhone = (?) WHERE publisherId = ?",
				new Object[] {publisherName, publisherAddress, publisherPhone, publisherId});
	}
	
	public void deletePublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("DELETE FROM tbl_publisher WHERE publisherId = ?", new Object[] { publisher.getPublisherId() });
	}
	
	public List<Publisher> getPublishers() throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_publisher", this);
	}
	
	public List<Publisher> getPublishersByName(String publisherName) throws ClassNotFoundException, SQLException {
		String searchString = "%"+publisherName+"%";
		return jdbcTemplate.query("SELECT * FROM tbl_publisher WHERE publisherName LIKE (?)", new Object[] {searchString}, this);
	}
	
	public List<Publisher> getPublishersByBook(Integer bookId) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("SELECT * FROM tbl_publisher p WHERE EXISTS ("
				+ "SELECT * FROM tbl_book b WHERE b.pubId = p.publisherId and b.bookId = ?)", 
				new Object[] {bookId}, this);
	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> Publishers = new ArrayList<>();
		while (rs.next()) {
			Publisher p = new Publisher(rs.getInt("publisherId"), rs.getString("publisherName"), rs.getString("publisherAddress"), rs.getString("publisherPhone"));
			Publishers.add(p);
		}
		return Publishers;
	}
}
