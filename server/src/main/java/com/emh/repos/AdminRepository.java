package com.emh.repos;

import com.emh.entity.Admin;
import com.emh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Integer>
{

	Admin findFirstByUser(User user);

}
