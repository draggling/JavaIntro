/**
 * 
 */
package com.ss.lms.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_publisher")
public class Publisher implements Serializable {


	private static final long serialVersionUID = -5870792368178062925L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "publisherId", unique = true, nullable = false)
	private Integer publisherId;

	@Column(name = "publisherName")
	@NonNull
	private String publisherName;
	
	@Column(name = "publisherAddress")
	@NonNull
	private String publisherAddress;
	
	@Column(name = "publisherPhone")
	@NonNull
	private String publisherPhone;
	
	/*
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
	private List<Book> books;
	
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	*/
	public String getPublisherAddress() {
		return publisherAddress;
	}

	public void setPublisherAddress(String publisherAddress) {
		this.publisherAddress = publisherAddress;
	}

	public String getPublisherPhone() {
		return publisherPhone;
	}

	public void setPublisherPhone(String publisherPhone) {
		this.publisherPhone = publisherPhone;
	}

	public Integer getpublisherId() {
		return publisherId;
	}

	public void setpublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

}
