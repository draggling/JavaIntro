/**
 * 
 */
package com.ss.lms.entity;

import java.util.List;

import com.ss.lms.service.EntityGetter;

/**
 * @author danwoo
 *
 */
public class Borrower {
	private Integer cardNo;
	private String name;
	private String phone;
	private String address;
	private List<Loan> loans;
	EntityGetter EG = new EntityGetter();

	public Borrower(Integer cardNo, String name, String address, String phone) {
		super();
		this.cardNo = cardNo;
		this.name = name;
		this.address = address;
		this.phone = phone;
	}
	
	/* card setter/getter */
	public Integer getCardNo() {
		return this.cardNo;
	}
	public void setcardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}
	
	/* name setter/getter */
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/* address setter/getter */
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	/* phone setter/getter */
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	public List<Loan> getLoans() {
		return loans;
	}
	public void setLoans() {
		this.loans = EG.findLoans(this.cardNo);
	}
	public String toString() {
		String output = "";
		if(this.name == null) {
			output += "NULL NAME";
		} else {
			output += "name: " + this.name + "\n";
		}
		output += "cardNo: " + this.cardNo + "\n";
		if(this.address == null) {
			output += "NULL ADDRESS";
		} else {
			output += "address: " + this.address + "\n";
		}
		if(this.phone == null) {
			output += "NULL PHONE";
		} else {
			output += "phone: " + this.phone + "\n";
		}
		if(this.loans != null) {
			for(Loan a : this.loans) {
				output += a.toString();
			}
		}
		output += "------------\n";
		return output;
	}
	
	public void adminPrint() {
		System.out.println("cardNo : " + cardNo + ", name = " + name + ", address = " + address + ", phone " + phone);
	}}

