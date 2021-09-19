package com.poc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poc.model.User;
import com.poc.repo.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

		System.out.println("Quering db for authentication...............");

		User user = userRepo.findByName(s);

		if (user == null) {
			throw new UsernameNotFoundException(s);
		}
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
				getAuthority(user));
	}

	public Set<SimpleGrantedAuthority> getAuthority(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getType().toUpperCase()));
		//System.out.println(authorities);

		return authorities;
	}
}