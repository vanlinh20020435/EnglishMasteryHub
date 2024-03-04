package com.emh.repos;

import com.emh.entity.QuestOption;
import com.emh.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestOptionRepository extends JpaRepository<QuestOption, Long>
{

	QuestOption findFirstByQuestion(Questions questions);

	List<QuestOption> findAllByQuestion(Questions questions);
}
