package com.emh.repos;

import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TestsRepository extends JpaRepository<Tests, Integer>, JpaSpecificationExecutor<Tests>
{
	@Query("select t from Tests t where t.createdBy = ?1")
	List<Tests> findAllByCreator(String creator);
}
