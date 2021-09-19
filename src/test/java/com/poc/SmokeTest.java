package com.poc;



import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.poc.controller.BookController;
import com.poc.controller.UserController;

@SpringBootTest
public class SmokeTest {

//	@Autowired
//	private BookController bookController;

	@Autowired
	private UserController userController;
	
	

	@Test
	public void contextLoads() throws Exception {
		//		assertThat(bookController).isNotNull();
		assertThat(userController).isNotNull();
	}

}