/**
 * 
 */
package com.ss.lms.entity;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.ss.lms.service.EntityGetter;

/**
 * @author danwoo
 *
 */
public class Loan {
	private Integer bookId;
	private Integer branchId;
	private Integer cardNo;
	private List<Loan> loans;
	private Date dateOut;
	private Date dueDate;
	private Date dateIn;
	EntityGetter EG = new EntityGetter();
	//private List<Author> authors;
	/* used for branch inventories */
//	private List<Genre> genres;
//	private List<Branch> branches;
//	private Publisher publisher;
	
	
	public Loan(Integer bookId, Integer branchId, Integer cardNo) {
		super();
		this.bookId = bookId;
		this.branchId = branchId;
		this.cardNo = cardNo;
	}

	public Loan(Integer bookId, Integer branchId, Integer cardNo, Date dateOut, Date dueDate, Date dateIn) {
		super();
		this.bookId = bookId;
		this.branchId = branchId;
		this.cardNo = cardNo;
		this.dateOut = dateOut;
		this.dueDate = dueDate;
		if(dateIn != null) this.dateIn = dateIn;
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
	
	
	public Integer getCardNo() {
		return this.cardNo;
	}
	public void setcardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}
	
	public Date getDueDate() {
		if(this.dueDate == null ) {
			return null;
		} else {
			return this.dueDate;
		}
	}
	public Date getDateOut() {
		if(this.dateOut == null ) {
			return null;
		} else {
			return this.dateOut;
		}
	}
	public Date getDateIn() {
		if(this.dateIn == null ) {
			return null;
		} else {
			return this.dateIn;
		}
	}
	
	public void update() {
		Loan temp = EG.findActiveLoan(this.bookId, this.branchId, this.cardNo);
		if(temp == null) {
			System.out.println("cannot update");
		} else {
			this.dueDate = getDueDate();
			this.dateOut = getDateOut();
			this.dateIn = getDateIn();
		}
	}
	
	public String toString() {
		String output = "";
		//String output = ("Borrower Info: " + EG.findBorrower(this.cardNo).toString() + "\n");
		output += ("Book Info: " + EG.findBook(this.bookId).toString() + "\n");
		output += ("Branch Info: " + EG.findBranch(this.branchId).toString() + "\n");
		output += ("Date Out: " + this.dateOut + "\n");
		output += ("Due Date " + this.dueDate + "\n");
		return output;
	}
	
}

