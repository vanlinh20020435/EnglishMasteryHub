package com.emh.repos;

import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TestsRepository extends JpaRepository<Tests, Integer>, JpaSpecificationExecutor<Tests>
{
}
