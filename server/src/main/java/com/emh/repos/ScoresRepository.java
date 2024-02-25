package com.emh.repos;

import com.emh.entity.Scores;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScoresRepository extends JpaRepository<Scores, String>
{

	Scores findFirstByTests(Tests tests);

}
