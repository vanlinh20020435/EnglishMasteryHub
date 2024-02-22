package com.emh.repos;

import com.emh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
	User findOneByUsername(String username);
}
