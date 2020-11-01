package com.ss.lms.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ss.lms.id.BookGenresId;


/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_book_genres")
public class BookGenres implements Serializable {

	@EmbeddedId
	private BookGenresId id;
	
	private static final long serialVersionUID = -8140007807142492108L;
	public BookGenres() {
		super();
	}
	
	public BookGenres(BookGenresId id) {
		super();
		this.id = id;
	}

	
	public BookGenresId getId() {
		return id;
	}

	public void setId(BookGenresId id) {
		this.id = id;
	}
	
}
	
	
	
	
	
	
	
	
	