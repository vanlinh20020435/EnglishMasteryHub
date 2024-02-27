package com.emh.repos;

import com.emh.entity.QuestFile;
import com.emh.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestFileRepository extends JpaRepository<QuestFile, String>
{

	QuestFile findFirstByQuestion(Questions questions);

	List<QuestFile> findAllByQuestion(Questions questions);
}
