package com.emh.repos;

import com.emh.entity.Classes;
import com.emh.entity.Student;
import com.emh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Student, Integer>
{

	Student findFirstByClasss(Classes classes);

	Student findFirstByUser(User user);

}
