package com.poc.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.MultipartFile;

import com.poc.config.filters.JwtRequestFilter;
import com.poc.model.Book;
import com.poc.repo.BookRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "books")
public class BookController {

	private byte[] bytes;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/get")
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}


	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
		this.bytes = file.getBytes();
		return ResponseEntity.ok(null);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public void createBook(@RequestBody Book book) throws IOException {
		book.setPicByte(this.bytes);
		bookRepository.save(book);
		this.bytes = null;
	}

	@DeleteMapping(path = { "/{id}" })
	public Book deleteBook(@PathVariable("id") long id) {
		Book book = bookRepository.getOne(id);
		bookRepository.deleteById(id);
		return book;
	}

	@PutMapping("/update")
	public void updateBook(@RequestBody Book book) {
		bookRepository.save(book);
	}
	/**********************************************************************************/	
	@PutMapping("/cart/add")
	public void addToCart(@RequestBody Book book) {
		System.out.println("API called");
		book.setId(null);
		bookRepository.save(book);
	}

	@GetMapping(path = { "/cart/get/{id}" })
	public List<Book> getBooksByUserName(@PathVariable("id") String uid) {
		return bookRepository.findAllByCurrentUser(uid);
	}

	@PutMapping("/cart")
	public void removeFromCart(@RequestBody String id) {
		bookRepository.deleteByUid(id);
	}


}