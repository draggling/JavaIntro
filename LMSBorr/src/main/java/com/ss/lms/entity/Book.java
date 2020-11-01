/**
 * 
 */
package com.ss.lms.entity;

import java.io.Serializable;
import java.lang.annotation.Repeatable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import com.sun.istack.NotNull;

/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_book")
public class Book implements Serializable {

	private static final long serialVersionUID = 6864940182095937249L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NonNull
	@Column(name = "bookId", unique = true)
	private Integer bookId;

	@Column(name = "title", nullable = false)
	@NonNull
	private String title;
	
	@Column(name = "pubId", nullable = false)
	@NonNull
	private Integer pubId;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pubId", referencedColumnName = "publisherId", insertable=false, updatable=false)
	private Publisher publisher;

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	@ManyToMany
	@JoinTable
	(name = "tbl_book_authors", 
	joinColumns = {@JoinColumn(name="bookId") }, 
	inverseJoinColumns = {@JoinColumn(name="authorId") } )
	private List<Author> authors;
	
	@ManyToMany
	@JoinTable
	(name = "tbl_book_genres",
	joinColumns = {@JoinColumn(name="bookId") },
	inverseJoinColumns = {@JoinColumn(name="genreId") } )
	private List<Genre> genres;

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPubId() {
		return pubId;
	}

	public void setPubId(Integer pubId) {
		this.pubId = pubId;
	}
	
	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	
	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	
}
