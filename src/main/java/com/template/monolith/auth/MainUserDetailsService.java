package com.template.monolith.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.template.monolith.model.User;
import com.template.monolith.repository.UserRepository;

@Service
public class MainUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("user does not exist");
		}
		new AccountStatusUserDetailsChecker().check(user);
		return user;
	}

}
