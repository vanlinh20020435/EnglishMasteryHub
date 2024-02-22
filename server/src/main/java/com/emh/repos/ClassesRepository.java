package com.emh.repos;

import com.emh.entity.Classes;
import com.emh.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClassesRepository extends JpaRepository<Classes, Integer> {

    Classes findFirstByTeacher(Teacher teacher);

}
