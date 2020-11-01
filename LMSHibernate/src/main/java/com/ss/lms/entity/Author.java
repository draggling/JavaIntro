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

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_author")
public class Author implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3076778140388491367L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "authorId")
	private Integer authorId;
	
	@Column(name = "authorName")
	private String authorName;
	
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
	
	public Integer getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/*
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	*/
}
