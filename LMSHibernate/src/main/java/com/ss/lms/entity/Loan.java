package com.ss.lms.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ss.lms.id.LoanId;


/**
 * @author dwoo
 *
 */
@Entity
@Table(name = "tbl_book_loans")
public class Loan implements Serializable {
	
	public Loan() {
		super();
	}
	
	
	public Loan(LoanId id) {
		super();
		this.id = id;
	}

	private static final long serialVersionUID = 974712334907940808L;

	@EmbeddedId
	private LoanId id;
	
	@Column(name = "dateOut")
	private Date dateOut;
	
	@Column(name = "dueDate")
	private Date dueDate;
	
	@Column(name = "dateIn")
	private Date dateIn;

	public LoanId getId() {
		return id;
	}

	public void setId(LoanId id) {
		this.id = id;
	}
	
	public Date getDateOut() {
		return dateOut;
	}


	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}


	public Date getDueDate() {
		return dueDate;
	}


	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}


	public Date getDateIn() {
		return dateIn;
	}


	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}
	
}
	
	
	
	
	
	
	
	
	