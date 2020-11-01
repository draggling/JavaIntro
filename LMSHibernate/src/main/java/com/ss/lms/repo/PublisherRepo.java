package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.Publisher;

@Repository
public interface PublisherRepo extends JpaRepository<Publisher, Integer>{
	
	@Query("FROM Publisher WHERE publisherId = :pubId")
	public List<Publisher> publisherExists(@Param("pubId") Integer publisherId);
	

	/* add publisher */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) values (:publisherName, :publisherAddress, :publisherPhone)", nativeQuery = true)
	public void addPublisher(@Param("publisherName") String publisherName, @Param("publisherAddress") String publisherAddress, @Param("publisherPhone") String publisherPhone);
	
	/* read publishers */
	@Query(value = "FROM Publisher")
	public List<Publisher> readPublishers();
	
	/* update publisher name */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_publisher SET publisherName = :publisherName, publisherAddress = :publisherAddress,"
			+ "publisherPhone = :publisherPhone WHERE publisherId = :publisherId", nativeQuery = true)
	public void updatePublisher(@Param("publisherId") Integer publisherId, @Param("publisherName") String publisherName,
			@Param("publisherAddress") String publisherAddress, @Param("publisherPhone") String publisherPhone);
	
	/* delete publisher */
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM tbl_publisher WHERE publisherId = :publisherId", nativeQuery = true)
	public void deletePublisher(@Param("publisherId") Integer publisherId);
}
