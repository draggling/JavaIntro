package com.ss.lms.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ss.lms.id.BookCopiesId;


/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_book_copies")
public class BookCopies implements Serializable {
	

	private static final long serialVersionUID = 3410831443135131978L;

	public BookCopies() {
		super();
	}
	
	
	public BookCopies(BookCopiesId id) {
		super();
		this.id = id;
	}


	@EmbeddedId
	private BookCopiesId id;
	
	@Column(name = "noOfCopies")
	private Integer noOfCopies;
	
	public BookCopiesId getId() {
		return id;
	}

	public void setId(BookCopiesId id) {
		this.id = id;
	}


	public Integer getNoOfCopies() {
		return noOfCopies;
	}


	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}


}
	
	
	
	
	
	
	
	
	