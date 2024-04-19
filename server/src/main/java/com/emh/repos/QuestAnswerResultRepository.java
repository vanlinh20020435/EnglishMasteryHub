package com.emh.repos;

import com.emh.entity.QuestAnswerResult;
import com.emh.entity.StudentTestResult;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestAnswerResultRepository extends JpaRepository<QuestAnswerResult, Long>
{
	void deleteAllByStudentTestResult(StudentTestResult studentTestResult);
}
