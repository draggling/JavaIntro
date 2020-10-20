package com.ss.lms.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.entity.Publisher;
import com.ss.lms.service.ConnectionUtil;

public class CRUDPublisher {
	public ConnectionUtil conUtil = new ConnectionUtil();
	Scanner scanner = new Scanner(System.in);
	public void addPublisher() {
		boolean unique = false;
		String publisherName = "";
		String publisherAddress = "";
		String publisherPhone = "";
   		try (Connection conn = conUtil.getConnection()){
   			PublisherDAO pdao = new PublisherDAO(conn);
   			/* get publisher name */
   			while(!unique) {
   	   			System.out.println("New publisher name: ");
   	   			publisherName = scanner.nextLine();
   	   			if(publisherName.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			} else {
   	   				unique = !pdao.searchPublisherBoolean(publisherName);
   	   				if(!unique) {
   	   					System.out.println("Publisher name already exists");
   	   				}
   	   			}
   			}
   			/* get publisher address */
   			while(publisherAddress.isEmpty()) {
   	   			System.out.print("New publisher address:");
   	   			publisherAddress = scanner.nextLine();
   	   			if(publisherAddress.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			}
   	   		}
   			/* get publisher phone */
   			unique = false;
   			while(publisherPhone.isEmpty()) {
   	   			System.out.print("New publisher phone:");
   	   			publisherPhone = scanner.nextLine();
   	   			if(publisherPhone.isEmpty()) {
   	   				System.out.println("Cannot be blank");
   	   			}
   	   		}
   			pdao.addPublisher(publisherName, publisherAddress, publisherPhone);
   			System.out.println("Publisher added");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			System.out.println("Could not add publisher");
 			return;
 		}
	}
	public void updatePublisher() {
		boolean unique = false;
   		try (Connection conn = conUtil.getConnection()){
   			PublisherDAO pdao = new PublisherDAO(conn);
   			List<Publisher> Publishers;
   			Publisher publisher = null;
   			String input = "";
	   		int count = 1;
	   		/* get publisherId */
   			while(publisher == null) {
   	   			System.out.println("Choose Publisher by keyword (leave blank to see a list of all publishers)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Publishers = pdao.getPublishers();
   	   			} else {
   	   				Publishers = pdao.getPublishersByName(input);
   	   			}
   	   			/* create publisher list to choose from */
   	   			if(Publishers.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Publishers.size() == 1) {
   	   				publisher = Publishers.get(0);
   	   				System.out.println("Publisher name = " + publisher.getPublisherName() + ", "
   	   						+ publisher.getPublisherAddress() + ": " + publisher.getPublisherPhone());
   	   			} else {
					System.out.println("Publishers:");
					count = 1;
					for(Publisher p: Publishers) {
						System.out.println(count + ") " + p.getPublisherName() + ", " + p.getPublisherAddress() + ": " + p.getPublisherPhone());
						count++;
					}
					System.out.print("Choose a publisher to update (from the integer list): ");
					int publisherOption = 0;
					while(publisherOption < 1 || publisherOption >= count) {
						try {
							publisherOption = scanner.nextInt();
							scanner.nextLine();
							if(publisherOption < 1 || publisherOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								publisher = Publishers.get(publisherOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
   			}
   			/* update Publisher Name using publisherId and publisherName */
   			while(!unique) {
   	   			System.out.println("New Publisher Name: (leave blank if no change)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty() || input.equalsIgnoreCase(publisher.getPublisherName())) {
   	   				System.out.println("Continuing...");
   	   				unique = true;
   	   			} else {
   	   				unique = !pdao.searchPublisherBoolean(input);
   	   				if(!unique) {
   	   					System.out.print("Publisher with this name already exists");
   	   				}
   	   			}
   			}
   			publisher.setPublisherName(input);
   			/* update Publisher address */
	   		System.out.println("New Publisher Address: (leave blank if no change)");
	   		input = scanner.nextLine();
	   		if(input.isEmpty() || input.equalsIgnoreCase(publisher.getPublisherAddress())) {
	   			System.out.println("Continuing...");
	   		} else if(input.length() <= 45) {
	   			publisher.setPublisherAddress(input);
	   		} else {
	   			System.out.println("Address is too long... skipping");
	   		}
   			/* update Publisher phone */
	   		System.out.println("New Publisher Phone: (leave blank if no change)");
	   		input = scanner.nextLine();
	   		if(input.isEmpty() || input.equalsIgnoreCase(publisher.getPublisherPhone())) {
	   			System.out.println("Continuing...");
	   		} else if(input.length() <= 45) {
	   			publisher.setPublisherPhone(input);
	   		} else {
	   			System.out.println("Phone Number is too long... skipping");
	   		}
   			pdao.updatePublisher(publisher.getPublisherId(), publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone());
   			System.out.println("Publisher updated");
   			return;
 		} catch (ClassNotFoundException | SQLException e) {
 			e.printStackTrace();
 			System.out.println("Could not add publisher");
 			return;
 		}
	}
	
	public void deletePublisher( ){
   		try (Connection conn = conUtil.getConnection()){
   			PublisherDAO pdao = new PublisherDAO(conn);
   			List<Publisher> Publishers;
   			Publisher publisher = null;
   			int count = 0;
   			String input;
			while(publisher == null) {
   	   			System.out.println("Choose Publisher by keyword (leave blank to see a list of all publishers)");
   	   			input = scanner.nextLine();
   	   			if(input.isEmpty()) {
   	   				Publishers = pdao.getPublishers();
   	   			} else {
   	   				Publishers = pdao.getPublishersByName(input);
   	   			}
   	   			/* create publisher list to choose from */
   	   			if(Publishers.size() == 0) {
   	   				System.out.println("Invalid search. Try again.");
   	   			} else if(Publishers.size() == 1) {
   	   				publisher = Publishers.get(0);
   	   			} else {
					System.out.println("Choose an Publisher to update:");
					count = 1;
					for(Publisher p: Publishers) {
						System.out.println(count + ") " + p.getPublisherName() + ", " + p.getPublisherAddress() + ": " + p.getPublisherPhone());
						count++;
					}
					System.out.print("Choose an publisher to delete (from the integer list): ");
					int publisherOption = 0;
					while(publisherOption < 1 || publisherOption >= count) {
						try {
							publisherOption = scanner.nextInt();
							scanner.nextLine();
							if(publisherOption < 1 || publisherOption >= count) {
								System.out.println("Out of Bounds");
							} else {
								publisher = Publishers.get(publisherOption-1);
							}
						} catch(InputMismatchException e) {
							System.out.println("ERROR: integer required");
							scanner.nextLine();
						}
					}
   	   			}
			}
   	   		/* check to see if publisher has dependencies */
			BookDAO bdao = new BookDAO(conn);
			/* probably doesn't work */
			int dependencies = bdao.checkBookPublisherDependency(publisher);
			if(dependencies == 0) {
				pdao.deletePublisher(publisher);
				System.out.println("Publisher Deleted");
				return;
			} else {
				System.out.println("ERROR: Publisher has " + dependencies + " dependencies");
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void readPublishers() {
   		try (Connection conn = conUtil.getConnection()){
   			PublisherDAO pdao = new PublisherDAO(conn);
   			List<Publisher> Publishers = pdao.getPublishers();
   			for(Publisher p: Publishers) {
   				p.adminPrint();;
   			}
   		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
   		}
   	}
}
