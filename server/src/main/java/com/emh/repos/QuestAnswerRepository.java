package com.emh.repos;

import com.emh.entity.QuestAnswer;
import com.emh.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestAnswerRepository extends JpaRepository<QuestAnswer, Long> {

    QuestAnswer findFirstByQuestion(Questions questions);

}
