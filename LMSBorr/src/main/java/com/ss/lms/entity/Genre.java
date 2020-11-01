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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_genre")
public class Genre implements Serializable {

	private static final long serialVersionUID = -7923887367449256296L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NonNull
	@Column(name = "genreId", unique = true, nullable = false)
	private Integer genreId;

	@Column(name = "genreName", unique = true, nullable = false)
	@NonNull
	private String genreName;
	
	/*
	@OneToMany
	@JoinTable
	(name = "tbl_book_genres", 
	joinColumns = {@JoinColumn(name="bookId") }, 
	inverseJoinColumns = {@JoinColumn(name="genreId") } )
	private List<Genre> genres;
	*/
	
	/*
	@OneToMany(mappedBy = "genre", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<BookAuthorGenreLink> bookAuthorGenreLinks;
	*/

	/*
	@ManyToMany(fetch = FetchType.LAZY,mappedBy = "genres")
	@JsonBackReference
	private List<Book> books;
	*/

	public Integer getGenreId() {
		return genreId;
	}

	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

}
