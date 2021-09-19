package com.poc.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poc.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	//@Query("FROM Book b where b.name = :username")
	
	@Query(value = "SELECT * FROM book WHERE book.uid = :username", nativeQuery = true)
    List<Book> findAllByCurrentUser(@Param("username") String userId);
	
	@Query(value = "SELECT * FROM book GROUP BY book.name", nativeQuery = true)
	List<Book> findAll();
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE book SET book.uid = null WHERE book.uid = :username", nativeQuery = true)
	void deleteByUid(@Param("username") String uid);
}