package com.ss.lms.id;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public
class BookAuthorsId implements Serializable {
	

	@Column(name = "bookId", unique = true)
	private Integer bookId;
	
	@Column(name = "authorId", unique = true)
	private Integer authorId;
	
	public BookAuthorsId() {
		super();
	}
	public BookAuthorsId (Integer bookId, Integer authorId) {
		super();
		this.bookId = bookId;
		this.authorId = authorId;
	}
	
	private static final long serialVersionUID = -5724065748333913037L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookAuthorsId that = (BookAuthorsId) o;

        if (!bookId.equals(that.bookId)) return false;
        return authorId.equals(that.authorId);
    }

    @Override
    public int hashCode() {
        int result = bookId.hashCode();
        result = 31 * result + authorId.hashCode();
        return result;
    }

	
    public Integer getBookId() {
    	return bookId;
    }
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}
}
