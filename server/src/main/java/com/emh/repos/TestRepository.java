package com.emh.repos;

import com.emh.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TestRepository extends JpaRepository<Test, Integer> {
}
