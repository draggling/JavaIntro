package com.ss.lms.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ss.lms.id.BookAuthorsId;


/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_book_authors")
public class BookAuthors implements Serializable {

	private static final long serialVersionUID = 974712334907940808L;

	@EmbeddedId
	private BookAuthorsId id;
	
	public BookAuthors() {
		super();
	}
	
	public BookAuthors(BookAuthorsId id) {
		super();
		this.id = id;
	}

	
	public BookAuthorsId getId() {
		return id;
	}

	public void setId(BookAuthorsId id) {
		this.id = id;
	}
	
}
	
	
	
	
	
	
	
	
	