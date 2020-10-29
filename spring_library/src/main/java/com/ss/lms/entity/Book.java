/**
 * 
 */
package com.ss.lms.entity;

import java.util.List;

/**
 * @author danwoo
 *
 */
public class Book {
	private Integer bookId;
	private String title;
	private Integer pubId;
	private List<Author> authors;
	private List<Genre> genres;
	private Publisher publisher;
	public Integer getBookId() {
		return bookId;
	}
	public Book(Integer bookId, String title, Integer pubId) {
		super();
		this.bookId = bookId;
		this.title = title;
		this.pubId = pubId;
	}
	
	public Book(String title, Integer pubId) {
		super();
		this.title = title;
		this.pubId = pubId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Integer getPubId() {
		return this.pubId;
	}
	
	public void setPubId(Integer pubId) {
		this.pubId = pubId;
	}
	
	public Publisher getPublisher() {
		return publisher;
	}
	
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
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
	
	public String toString() {
		String output = (this.title + "\n");
		if(authors != null && authors.size() != 0) {
			output += "Authors: ";
			for(Author a : this.authors) {
				output += (" " + a.getAuthorName());
			}
		}
		if(genres != null && genres.size() != 0) {
			output += "\nGenres:";
			for(Genre g : genres) {
				output += " " + g.getGenreName();
			}
		}
		if(publisher != null) {
			output += "\nPublisher: " + publisher.getPublisherName() + ", " + publisher.getPublisherAddress() + ": " + publisher.getPublisherPhone();
		}
		return output;
	}
}

