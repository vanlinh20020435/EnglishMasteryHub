package com.emh.repos;

import com.emh.entity.StudentTestResult;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentTestResultRepository extends JpaRepository<StudentTestResult, Integer>
{

	StudentTestResult findFirstByTests(Tests tests);

}
