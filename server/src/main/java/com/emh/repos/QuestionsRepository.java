package com.emh.repos;

import com.emh.entity.QuestType;
import com.emh.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionsRepository extends JpaRepository<Questions, Integer> {

    Questions findFirstByType(QuestType questType);

}
