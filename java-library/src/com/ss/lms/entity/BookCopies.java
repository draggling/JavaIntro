/**
 * 
 */
package com.ss.lms.entity;

/**
 * @author danwoo
 *
 */
public class BookCopies {
	private Integer bookId;
	private Integer branchId;
	private Integer noOfCopies;
	

	public BookCopies(Integer bookId, Integer branchId, Integer noOfCopies) {
		super();
		this.bookId = bookId;
		this.branchId = branchId;
		this.noOfCopies = noOfCopies;
	}
	
	public Integer getBookId() {
		return this.bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	
	public Integer getBranchId() {
		return this.branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	
	
	public Integer getNoOfCopies() {
		return this.noOfCopies;
	}
	public void setnoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	
	
}

