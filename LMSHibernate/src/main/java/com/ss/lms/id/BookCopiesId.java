package com.ss.lms.id;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public
class BookCopiesId implements Serializable {
	
	private static final long serialVersionUID = -2966019683537103350L;

	@Column(name = "bookId", unique = true)
	private Integer bookId;
	
	@Column(name = "branchId", unique = true)
	private Integer branchId;
	
	public BookCopiesId() {
		super();
	}
	public BookCopiesId (Integer bookId, Integer branchId) {
		super();
		this.bookId = bookId;
		this.branchId = branchId;
	}
	
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookCopiesId that = (BookCopiesId) o;

        if (!bookId.equals(that.bookId)) return false;
        return branchId.equals(that.branchId);
    }

    @Override
    public int hashCode() {
        int result = bookId.hashCode();
        result = 31 * result + branchId.hashCode();
        return result;
    }

	
    public Integer getBookId() {
    	return bookId;
    }
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getGenreId() {
		return branchId;
	}

	public void setGenreId(Integer branchId) {
		this.branchId = branchId;
	}
}
