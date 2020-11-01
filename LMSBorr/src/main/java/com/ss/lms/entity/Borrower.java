/**
 * 
 */
package com.ss.lms.entity;

import java.io.Serializable;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_borrower")
public class Borrower implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3076778140388491367L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cardNo")
	private Integer cardNo;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "address")
	private String address;

	public Integer getCardNo() {
		return cardNo;
	}

	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	/*
	@ManyToMany
	@JoinTable
	(name = "tbl_book_authors", 
	joinColumns = {@JoinColumn(name="authorId") }, 
	inverseJoinColumns = {@JoinColumn(name="bookId") } )
	private List<Book> books;
	*/

	/*
	@OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<BookAuthorGenreLink> bookAuthorGenreLinks;
	*/
	

	/*
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	*/
}
