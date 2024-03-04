package com.emh.repos;

import com.emh.entity.Classes;
import com.emh.entity.Student;
import com.emh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student>
{

	Student findFirstByClasss(Classes classes);

	Student findFirstByUser(User user);

}
