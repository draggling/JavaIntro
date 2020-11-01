package com.ss.lms.id;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public
class LoanId implements Serializable {
	

	@Column(name = "bookId", unique = true)
	private Integer bookId;
	
	@Column(name = "branchId", unique = true)
	private Integer branchId;
	
	@Column(name = "cardNo", unique = true)
	private Integer cardNo;
	
	public LoanId() {
		super();
	}
	public LoanId (Integer bookId, Integer branchId, Integer cardNo) {
		super();
		this.bookId = bookId;
		this.branchId = branchId;
		this.cardNo = cardNo;
	}
	
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public Integer getCardNo() {
		return cardNo;
	}
	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}

	private static final long serialVersionUID = -5724065748333913037L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoanId that = (LoanId) o;

        if (!bookId.equals(that.bookId)) return false;
        return branchId.equals(that.branchId);
    }

    @Override
    public int hashCode() {
        int result = bookId.hashCode();
        result = 31 * result + branchId.hashCode();
        return result;
    }


}
