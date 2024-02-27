package com.emh.repos;

import com.emh.entity.StudentTestDetail;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentTestDetailRepository extends JpaRepository<StudentTestDetail, Integer>
{

	StudentTestDetail findFirstByTests(Tests tests);

}
