package com.emh.repos;

import com.emh.entity.Classes;
import com.emh.entity.TestClass;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TestClassRepository extends JpaRepository<TestClass, Integer>
{
	TestClass findOneByClasssAndTests(Classes classs, Tests tests);

	void deleteAllByTests(Tests tests);
}
