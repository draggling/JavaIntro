/**
 * 
 */
package com.ss.lms.entity;

/**
 * @author danwoo
 *
 */
public class Branch {
	private Integer branchId;
	private String branchName;
	private String branchAddress;
//	private String title;
//	private List<Author> authors;
//	private List<Genre> genres;
//	private List<Branch> branches;
//	private Publisher publisher;

	public Branch(Integer branchId, String branchName, String branchAddress) {
		super();
		this.branchId = branchId;
		this.branchName = branchName;
		this.branchAddress = branchAddress;
	}
	public Integer getBranchId() {
		return this.branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return this.branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchAddress() {
		return this.branchAddress;
	}
	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}
	
	public String toString() {
		String output = "";
		output += this.branchName + ", " + this.branchAddress + "\n";
		return output;
	}
	
	public void adminPrint() {
		System.out.println("branchId = " + branchId  + ": " + this.branchName + ", " + this.branchAddress);
	}
}

