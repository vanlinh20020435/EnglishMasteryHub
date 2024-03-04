package com.emh.repos;

import com.emh.entity.QuestAnswer;
import com.emh.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestAnswerRepository extends JpaRepository<QuestAnswer, Long>
{

	QuestAnswer findFirstByQuestion(Questions questions);

	List<QuestAnswer> findAllByQuestion(Questions questions);
}
