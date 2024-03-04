package com.emh.repos;

import com.emh.entity.Classes;
import com.emh.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ClassesRepository extends JpaRepository<Classes, Integer>, JpaSpecificationExecutor<Classes>
{

	Classes findFirstByTeacher(Teacher teacher);

}
