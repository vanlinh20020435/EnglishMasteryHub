package com.emh.repos;

import com.emh.entity.QuestOfTest;
import com.emh.entity.Questions;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestOfTestRepository extends JpaRepository<QuestOfTest, Long> {

    QuestOfTest findFirstByQuestion(Questions questions);

    QuestOfTest findFirstByTestCode(Tests tests);

}
