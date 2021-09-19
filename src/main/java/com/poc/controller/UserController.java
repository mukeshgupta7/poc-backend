package com.poc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.poc.config.MyUserDetailsService;
import com.poc.config.filters.JwtRequestFilter;
import com.poc.config.models.AuthenticationRequest;
import com.poc.config.models.AuthenticationResponse;
import com.poc.config.util.JwtUtil;
import com.poc.model.User;
import com.poc.repo.UserRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "users")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;


	@Autowired
	private UserRepository userRepository;


	@GetMapping("/get")
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public void createUser(@RequestBody User user) {
		userRepository.save(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = { "/{id}" })
	public User deleteUser(@PathVariable("id") long id) {
		User user = userRepository.getOne(id);
		userRepository.deleteById(id);
		return user;
	}



	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws Exception {

		System.out.println("login.................");
		System.out.println(user.getName() + user.getPassword());

		try {
			System.out.println("authenticating.................");

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword())
					);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(user.getName());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		String role = userRepository.findByName(user.getName()).getType();
		JSONObject resp = new JSONObject();
		resp.put("role", role);
		resp.put("jwt", jwt);
		resp.put("status", 200);
		resp.put("username", user.getName());


		return ResponseEntity.status(HttpStatus.OK)
				.body(resp.toString());
	}



	//////////////////////////////////////////////////////////////////////////////


	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User user) {

		System.out.println("-------------------------------------------------------------");
		System.out.println(user.getName()+" "+user.getPassword()+" "+user.getType());
		System.out.println("-------------------------------------------------------------");
		System.out.println("Signin...............");

		if(userRepository.findByName(user.getName()) != null)
			return new ResponseEntity<>(HttpStatus.CONFLICT);

		userRepository.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
		//final UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), new ArrayList<>());
		//String jwt = jwtTokenUtil.generateToken(userDetails);
		//return ResponseEntity.ok(new AuthenticationResponse(jwt));	

	}

}

