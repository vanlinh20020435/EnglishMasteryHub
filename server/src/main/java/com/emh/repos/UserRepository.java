package com.emh.repos;

import com.emh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, Integer>
{
	@Query(value = "select u from User u where u.username = ?1 and u.status = 1")
	User findOneByUsernameAndActive(String username);

	@Query(value = "select u from User u where u.username = ?1")
	User findOneByUsername(String username);
}
