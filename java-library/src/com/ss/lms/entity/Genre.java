/**
 * 
 */
package com.ss.lms.entity;

/**
 * @author danwoo
 *
 */
public class Genre {
	private Integer genreId;
	private String genreName;


	public Genre(Integer genreId, String genreName) {
		super();
		this.genreId = genreId;
		this.genreName = genreName;

	}
	public Integer getGenreId() {
		return this.genreId;
	}
	
	public String getGenreName() {
		return this.genreName;
	}
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	
	public String toString() {
		return this.genreName + "\n";
	}
}

