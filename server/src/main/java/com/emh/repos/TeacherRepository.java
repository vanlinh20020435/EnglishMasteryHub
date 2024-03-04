package com.emh.repos;

import com.emh.entity.Teacher;
import com.emh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TeacherRepository extends JpaRepository<Teacher, Integer>, JpaSpecificationExecutor<Teacher>
{

	Teacher findFirstByUser(User user);

}
