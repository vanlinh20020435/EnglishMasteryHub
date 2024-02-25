package com.emh.repos;

import com.emh.entity.Questions;
import com.emh.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionsRepository extends JpaRepository<Questions, Integer>
{

	Questions findFirstByParentQuestionAndQuestionIdNot(Questions questions,
														final Integer questionId);

    Questions findFirstByTests(Tests tests);
}
