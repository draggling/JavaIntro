package com.ss.lms.id;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public
class BookGenresId implements Serializable {
	
	@Column(name = "bookId", unique = true)
	private Integer bookId;
	
	@Column(name = "genreId", unique = true)
	private Integer genreId;
	
	public BookGenresId() {
		super();
	}
	public BookGenresId (Integer bookId, Integer genreId) {
		super();
		this.bookId = bookId;
		this.genreId = genreId;
	}
	
	private static final long serialVersionUID = 6998948892051705591L;
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookGenresId that = (BookGenresId) o;

        if (!bookId.equals(that.bookId)) return false;
        return genreId.equals(that.genreId);
    }

    @Override
    public int hashCode() {
        int result = bookId.hashCode();
        result = 31 * result + genreId.hashCode();
        return result;
    }

	
    public Integer getBookId() {
    	return bookId;
    }
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getGenreId() {
		return genreId;
	}

	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}
}
