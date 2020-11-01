package com.ss.lms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer>{
	
	@Query(" FROM Author where name =:name")
	public List<Author> readAuthorsByName(@Param("name") String name);

	@Query( "FROM Author")
	public List<Author> readAuthors();
	
	@Query(" FROM Author WHERE authorId = :authorId")
	public List<Author> bookHasAuthorId(@Param("authorId") Integer authorId);
	
	@Query(" FROM Author WHERE authorId = :authorId")
	public List<Author> authorIdExists(@Param("authorId") Integer authorId);

	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO tbl_author (authorName) values (:authorName)", nativeQuery = true)
	public void addAuthor(@Param("authorName") String authorName);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE tbl_author SET authorName = :authorName WHERE authorId = :authorId", nativeQuery = true)
	public void updateAuthor(@Param("authorId") Integer authorId, @Param("authorName") String authorName);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE tbl_author WHERE authorId = :authorId", nativeQuery = true)
	public void deleteAuthor(@Param("authorId") Integer authorId);
}
