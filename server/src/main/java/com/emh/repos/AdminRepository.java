package com.emh.repos;

import com.emh.entity.Admin;
import com.emh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AdminRepository extends JpaRepository<Admin, Integer>, JpaSpecificationExecutor<Admin>
{

	Admin findFirstByUser(User user);

}
