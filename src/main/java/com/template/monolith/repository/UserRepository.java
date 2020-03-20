package com.template.monolith.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.template.monolith.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Query("FROM User WHERE username=?1 OR email=?2")
	List<User> getUserByUsernameAndEmail(String username, String email);
	
	User findUserByUsername(String username);
}
