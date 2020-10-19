/**
 * 
 */
package com.ss.lms.entity;

/**
 * @author danwoo
 *
 */
public class Publisher {
	private Integer publisherId;
	private String publisherName;
	private String publisherAddress;
	private String publisherPhone;

	public Publisher(Integer publisherId, String publisherName, String publisherAddress, String publisherPhone) {
		super();
		this.publisherId = publisherId;
		this.publisherName = publisherName;
		this.publisherAddress = publisherAddress;
		this.publisherPhone = publisherPhone;
	}
	public Integer getPublisherId() {
		return this.publisherId;
	}
	public void setpublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}
	public String getPublisherName() {
		return this.publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public String getPublisherAddress() {
		return this.publisherAddress;
	}
	public void setPublisherAddress(String publisherAddress) {
		this.publisherAddress = publisherAddress;
	}
	public String getPublisherPhone() {
		return this.publisherAddress;
	}
	public void setPublisherPhone(String publisherPhone) {
		this.publisherAddress = publisherPhone;
	}
	
	public String toString() {
		String output = "";
		output += this.publisherName + ", " + this.publisherAddress + " --- " + this.publisherPhone + "\n";
		return output;
	}
	
	public void adminPrint() {
		System.out.println("PublisherId: " + publisherId  + ", name = " + this.publisherName + ", address = " + this.publisherAddress + ", phone = " + this.publisherPhone);
	}
}

