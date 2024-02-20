package com.emh.repos;

import com.emh.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScoreRepository extends JpaRepository<Score, String> {
}
