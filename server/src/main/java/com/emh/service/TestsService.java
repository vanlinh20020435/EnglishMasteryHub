package com.emh.service;

import com.emh.entity.*;
import com.emh.payload.request.*;
import com.emh.payload.response.TestsResponse;
import com.emh.repos.*;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class TestsService
{

	private final TestsRepository testsRepository;
	private final QuestionsRepository questionsRepository;
	private final ScoresRepository scoresRepository;
	private final StudentTestDetailRepository studentTestDetailRepository;
	private final QuestAnswerRepository questAnswerRepository;
	private final QuestOptionRepository questOptionRepository;
	private final QuestFileRepository questFileRepository;

	public TestsService(final TestsRepository testsRepository,
						final QuestionsRepository questionsRepository,
						final ScoresRepository scoresRepository,
						final StudentTestDetailRepository studentTestDetailRepository,
						final QuestAnswerRepository questAnswerRepository,
						final QuestOptionRepository questOptionRepository,
						final QuestFileRepository questFileRepository)
	{
		this.testsRepository = testsRepository;
		this.questionsRepository = questionsRepository;
		this.scoresRepository = scoresRepository;
		this.studentTestDetailRepository = studentTestDetailRepository;
		this.questAnswerRepository = questAnswerRepository;
		this.questOptionRepository = questOptionRepository;
		this.questFileRepository = questFileRepository;
	}

	public List<TestsResponse> findAll()
	{
		final List<Tests> testses = testsRepository.findAll(Sort.by("testId"));
		return testses.stream()
				.map(tests -> MapperUtils.testMapToResponse(tests, new TestsResponse()))
				.toList();
	}

	public TestsResponse get(final Integer testId)
	{
		return testsRepository.findById(testId)
				.map(tests -> MapperUtils.testMapToResponse(tests, new TestsResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final TestsRequest testsRequest) throws IOException
	{
		Tests tests = new Tests();
		MapperUtils.testMapToEntity(testsRequest, tests);
		tests = testsRepository.save(tests);
		saveQuestions(tests, testsRequest);
		return tests.getTestId();
	}

	public void update(final Integer testId, final TestsRequest testsDTO)
	{
		final Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		MapperUtils.testMapToEntity(testsDTO, tests);
		testsRepository.save(tests);
	}

	public void delete(final Integer testId)
	{
		testsRepository.deleteById(testId);
	}

	public ReferencedWarning getReferencedWarning(final Integer testId)
	{
		final ReferencedWarning referencedWarning = new ReferencedWarning();
		final Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		final Questions questions = questionsRepository.findFirstByTests(tests);
		if (questions != null)
		{
			referencedWarning.setKey("tests.questOfTest.testId.referenced");
			referencedWarning.addParam(questions.getQuestionId());
			return referencedWarning;
		}
		final Scores testCodeScores = scoresRepository.findFirstByTests(tests);
		if (testCodeScores != null)
		{
			referencedWarning.setKey("tests.scores.testId.referenced");
			referencedWarning.addParam(testCodeScores.getScoreNumber());
			return referencedWarning;
		}
		final StudentTestDetail testCodeStudentTestDetail = studentTestDetailRepository.findFirstByTests(tests);
		if (testCodeStudentTestDetail != null)
		{
			referencedWarning.setKey("tests.studentTestDetail.testId.referenced");
			referencedWarning.addParam(testCodeStudentTestDetail.getId());
			return referencedWarning;
		}
		return null;
	}

	private void saveQuestions(Tests tests, TestsRequest testsRequest) throws IOException
	{
		for (QuestionsRequest questionsRequest : testsRequest.getQuestions())
		{
			Questions questions = new Questions();
			MapperUtils.questionMapToEntity(questionsRequest, questions, tests);
			questionsRepository.save(questions);
			saveQuestionAnswers(questions, questionsRequest);
			saveQuestionOptions(questions, questionsRequest);
			saveQuestionFiles(questions, questionsRequest);
			saveSubQuestion(questions, questionsRequest, tests);
		}
	}

	private void saveQuestionAnswers(Questions questions, QuestionsRequest questionsRequest)
	{
		for (QuestAnswerRequest questAnswerRequest : questionsRequest.getAnswers())
		{
			QuestAnswer answer = new QuestAnswer();
			MapperUtils.questionAnswerMapToEntity(questAnswerRequest, answer, questions);
			questAnswerRepository.save(answer);
		}
	}

	private void saveQuestionOptions(Questions questions, QuestionsRequest questionsRequest)
	{
		for (QuestOptionRequest questOptionRequest : questionsRequest.getOptions())
		{
			QuestOption option = new QuestOption();
			MapperUtils.questionOptionMapToEntity(questOptionRequest, option, questions);
			questOptionRepository.save(option);
		}
	}

	private void saveQuestionFiles(Questions questions, QuestionsRequest questionsRequest) throws IOException
	{
		for (QuestFileRequest questFileRequest : questionsRequest.getFiles())
		{
			QuestFile file = new QuestFile();
			MapperUtils.questionFileMapToEntity(questFileRequest, file, questions);
			questFileRepository.save(file);
		}
	}

	private void saveSubQuestion(Questions parentQuestions, QuestionsRequest questionsRequest, Tests tests) throws IOException
	{
		if(questionsRequest.getSubQuestions() == null)
			return;
		for (QuestionsRequest subQuestionsRequest : questionsRequest.getSubQuestions())
		{
			Questions questions = new Questions();
			MapperUtils.questionMapToEntity(subQuestionsRequest, questions, tests, parentQuestions);
			questionsRepository.save(questions);
			saveQuestionAnswers(questions, subQuestionsRequest);
			saveQuestionOptions(questions, subQuestionsRequest);
			saveQuestionFiles(questions, subQuestionsRequest);
		}
	}
}
