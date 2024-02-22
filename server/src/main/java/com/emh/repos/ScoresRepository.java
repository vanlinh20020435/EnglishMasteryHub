package com.emh.repos;

import com.emh.entity.Scores;
import com.emh.entity.Student;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScoresRepository extends JpaRepository<Scores, String> {

    Scores findFirstByStudent(Student student);

    Scores findFirstByTestCode(Tests tests);

}
