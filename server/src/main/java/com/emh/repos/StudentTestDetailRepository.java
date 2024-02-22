package com.emh.repos;

import com.emh.entity.Questions;
import com.emh.entity.Student;
import com.emh.entity.StudentTestDetail;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentTestDetailRepository extends JpaRepository<StudentTestDetail, Integer> {

    StudentTestDetail findFirstByTestCode(Tests tests);

    StudentTestDetail findFirstByQuestion(Questions questions);

    StudentTestDetail findFirstByStudent(Student student);

}
