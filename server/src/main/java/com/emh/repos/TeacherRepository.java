package com.emh.repos;

import com.emh.entity.Teacher;
import com.emh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Teacher findFirstByUser(User user);

}
