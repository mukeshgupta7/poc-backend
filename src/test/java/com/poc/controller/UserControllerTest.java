package com.poc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.poc.model.User;
import com.poc.repo.UserRepository;


//@WebMvcTest(controllers = UserController.class)
//@ActiveProfiles("test")

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired                           
	private MockMvc mockMvc; 

	@Mock
	private UserRepository userRepo;

	@InjectMocks
	private UserController userController = new UserController();


	private List<User> userList;       

	@BeforeEach                           
	void setUp() { 

		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		MockitoAnnotations.initMocks(this);
		this.userList = new ArrayList<>();                                   
		this.userList.add(new User(1L, "user1", "pwd1","User"));                               
		this.userList.add(new User(2L, "user2", "pwd2","Admin"));                               
		this.userList.add(new User(3L, "user3", "pwd3","User"));                                                       

	}

	@Test
	public void shouldGetAllUsers() throws Exception {

		when(userRepo.findAll()).thenReturn(userList);

		mockMvc.perform(MockMvcRequestBuilders.get("/users/get")).
		andExpect(MockMvcResultMatchers.status().isOk()).
		andExpect(content().string(containsString("{\"id\":1,\"name\":\"user1\",\"password\":\"pwd1\",\"type\":\"User\"}")));

		verify(userRepo).findAll();

	}
}