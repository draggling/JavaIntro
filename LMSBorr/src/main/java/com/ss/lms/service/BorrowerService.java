package com.ss.lms.service;

import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookGenres;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.Branch;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;
import com.ss.lms.repo.AuthorRepo;
import com.ss.lms.repo.BookAuthorsRepo;
import com.ss.lms.repo.BookCopiesRepo;
import com.ss.lms.repo.BookGenresRepo;
import com.ss.lms.repo.BookRepo;
import com.ss.lms.repo.BorrowerRepo;
import com.ss.lms.repo.BranchRepo;
import com.ss.lms.repo.GenreRepo;
import com.ss.lms.repo.LoanRepo;
import com.ss.lms.repo.PublisherRepo;

@RestController
public class BorrowerService {

	@Autowired
	BookRepo brepo;
	
	@Autowired
	AuthorRepo arepo;
	
	@Autowired
	PublisherRepo prepo;	
	
	@Autowired
	GenreRepo grepo;
	
	@Autowired
	BookGenresRepo bgrepo;
	
	@Autowired
	BookAuthorsRepo barepo;
	
	@Autowired
	BranchRepo brrepo;
	
	@Autowired
	BorrowerRepo borepo;
	
	@Autowired
	LoanRepo lrepo;
	
	@Autowired
	BookCopiesRepo bcrepo;
	 
	/* borrower functions */
	
	/* check out a book */
	@Transactional
	@RequestMapping(value = "/checkOutBook", method = RequestMethod.PUT)
	public String checkOutBook(@RequestParam Integer cardNo, @RequestParam Integer bookId,
			@RequestParam Integer branchId) throws SQLException {
		if (bcrepo.bookCopiesExist(bookId, branchId).get(0).getNoOfCopies() == 0) {
			return "there are no copies of book " + bookId + "in branch " + branchId;
		}else if(lrepo.loanExists(cardNo, bookId, branchId).size() != 0) {
			return "loan for cardNo: " + cardNo + " in branch: " + branchId + " alread exists for book: " + bookId;	
		} else if(bcrepo.bookCopiesExist(bookId, branchId).get(0).getNoOfCopies() <= lrepo.bookBranchLoans(bookId, branchId).size()) {
			return "all copies of book + " +bookId+ " in branch " +branchId+ " are currently checked out";
		} else if(lrepo.loanExpired(cardNo, bookId, branchId).size() == 0){
			/* row already exists but it is expired */
			lrepo.addLoan(cardNo, bookId, branchId);
			return "book checked out";
		} else {
			lrepo.updateLoan(cardNo, bookId, branchId);
			return "old loan exists: loan updated and book checked out again";
		}
	}
	
	/* check in a book */
	@Transactional
	@RequestMapping(value = "/checkInBook", method = RequestMethod.PUT)
	public String checkInBook(@RequestParam Integer cardNo, @RequestParam Integer bookId,
			@RequestParam Integer branchId) throws SQLException {
		if (bcrepo.bookCopiesExist(bookId, branchId).get(0).getNoOfCopies() == 0) {
			return "there are no loans of book " + bookId + "in branch " + branchId;
		}else if(lrepo.loanExpired(cardNo, bookId, branchId).size() != 0) {
			lrepo.updateLoan(cardNo, bookId, branchId);
			return "loan for cardNo: " + cardNo + " in branch: " + branchId + " already checked in for book: " + bookId;	
		} else {
			lrepo.checkInBook(cardNo, bookId, branchId);
			return "book checked in";
		}
	}
}
