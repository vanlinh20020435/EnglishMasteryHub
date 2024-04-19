package com.emh.repos;

import com.emh.entity.StudentTestResult;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StudentTestResultRepository extends JpaRepository<StudentTestResult, Integer>, JpaSpecificationExecutor<StudentTestResult>
{

	StudentTestResult findFirstByTests(Tests tests);

}
