package com.emh.service;

import com.emh.entity.*;
import com.emh.payload.request.TestClassRequest;
import com.emh.payload.response.*;
import com.emh.repos.ClassesRepository;
import com.emh.repos.TestClassRepository;
import com.emh.repos.TestsRepository;
import com.emh.util.AppException;
import com.emh.util.EntityMapper;
import com.emh.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TestClassService
{

	private final TestClassRepository testClassRepository;
	private final ClassesRepository classesRepository;
	private final TestsRepository testsRepository;

	public TestClassService(final TestClassRepository testClassRepository,
							final ClassesRepository classesRepository,
							final TestsRepository testsRepository)
	{
		this.testClassRepository = testClassRepository;
		this.classesRepository = classesRepository;
		this.testsRepository = testsRepository;
	}

	public List<TestClassResponse> findAllByClass(Integer classId)
	{
		final Classes classs = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		List<TestClassResponse> responses = new ArrayList<>(classs.getTestClasses().stream()
				.map(this::exportTestClass)
				.toList());
		responses.sort((o1, o2) -> o1.getTestId().compareTo(o2.getTestId()));
		return responses;
	}

	public void create(Integer classId, Integer testId, TestClassRequest testClassRequest)
	{
		TestClass testClass = new TestClass();
		Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		TestClass old = testClassRepository.findOneByClasssAndTests(classes, tests);
		if (old != null)
			throw new AppException("Test already exists");
		testClass.setTests(tests);
		testClass.setClasss(classes);
		if (testClassRequest.getPassword() != null)
			testClass.setPassword(testClassRequest.getPassword());
		if (testClassRequest.getStartDate() != null)
			testClass.setStartDate(testClassRequest.getStartDate());
		if (testClassRequest.getEndDate() != null)
			testClass.setEndDate(testClassRequest.getEndDate());
		testClassRepository.save(testClass);
	}

	public void delete(Integer classId, Integer testId)
	{
		Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		TestClass testClass = testClassRepository.findOneByClasssAndTests(classes, tests);
		testClassRepository.delete(testClass);
	}


	private TestClassResponse exportTestClass(TestClass testClass)
	{
		Tests tests = testClass.getTests();
		TestClassResponse response = new TestClassResponse();
		EntityMapper.testClassMapToResponse(tests, response);
		if (testClass.getStartDate() != null)
			response.setStartDate(testClass.getStartDate());
		;
		if (testClass.getEndDate() != null)
			response.setEndDate(testClass.getEndDate());
		exportQuestions(tests, response);
		return response;
	}

	private void exportQuestions(Tests tests, TestClassResponse testsResponse)
	{
		if (tests.getQuestions() == null)
			return;
		List<QuestionsResponse> responses = new ArrayList<>();
		for (Questions questions : tests.getQuestions())
		{
			if (questions.getParentQuestion() != null)
				continue;
			QuestionsResponse questionsResponse = new QuestionsResponse();
			EntityMapper.questionMapToResponse(questions, questionsResponse);
			exportQuestionAnswers(questions, questionsResponse);
			exportQuestionOptions(questions, questionsResponse);
			exportQuestionFiles(questions, questionsResponse);
			exportSubQuestion(questions, questionsResponse);
			responses.add(questionsResponse);
		}
		testsResponse.setQuestions(responses);
	}

	private void exportQuestionAnswers(Questions questions, QuestionsResponse questionsResponse)
	{
		if (questions.getQuestionQuestAnswers() == null)
			return;
		List<QuestAnswerResponse> responses = new ArrayList<>();
		for (QuestAnswer questAnswer : questions.getQuestionQuestAnswers())
		{
			QuestAnswerResponse answer = new QuestAnswerResponse();
			EntityMapper.questionAnswerMapToResponse(questAnswer, answer);
			responses.add(answer);
		}
		questionsResponse.setAnswers(responses);
	}

	private void exportQuestionOptions(Questions questions, QuestionsResponse questionsResponse)
	{
		if (questions.getQuestOptions() == null)
			return;
		List<QuestOptionResponse> responses = new ArrayList<>();
		for (QuestOption questOption : questions.getQuestOptions())
		{
			QuestOptionResponse option = new QuestOptionResponse();
			EntityMapper.questionOptionMapToResponse(questOption, option);
			responses.add(option);
		}
		questionsResponse.setOptions(responses);
	}

	private void exportQuestionFiles(Questions questions, QuestionsResponse questionsResponse)
	{
		if (questions.getQuestFiles() == null)
			return;
		List<QuestFileResponse> responses = new ArrayList<>();
		for (QuestFile questFile : questions.getQuestFiles())
		{
			QuestFileResponse file = new QuestFileResponse();
			EntityMapper.questionFileMapToResponse(questFile, file);
			responses.add(file);
		}
		questionsResponse.setFiles(responses);
	}

	private void exportSubQuestion(Questions questions, QuestionsResponse parentQuestions)
	{
		if (questions.getQuestions() == null)
			return;
		List<QuestionsResponse> subQuest = new ArrayList<>();
		for (Questions subQuestions : questions.getQuestions())
		{
			QuestionsResponse questionsResponse = new QuestionsResponse();
			EntityMapper.questionMapToResponse(subQuestions, questionsResponse);
			exportQuestionAnswers(subQuestions, questionsResponse);
			exportQuestionOptions(subQuestions, questionsResponse);
			exportQuestionFiles(subQuestions, questionsResponse);
			subQuest.add(questionsResponse);
		}
		parentQuestions.setSubQuestions(subQuest);
	}

	public List<TestClassInfoResponse> findAllInfoByClass(Integer classId)
	{
		final Classes classs = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		List<TestClassInfoResponse> responses = new ArrayList<>(classs.getTestClasses().stream()
				.map(testClass -> EntityMapper.testInfoMapToResponse(testClass, new TestClassInfoResponse()))
				.toList());
		responses.sort((o1, o2) -> o1.getTestId().compareTo(o2.getTestId()));
		return responses;
	}

	public TestClassInfoResponse getTestInfo(Integer classId, Integer testId)
	{
		Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		TestClass testClass = testClassRepository.findOneByClasssAndTests(classes, tests);
		if (testClass == null)
			throw new NotFoundException();
		return EntityMapper.testInfoMapToResponse(testClass, new TestClassInfoResponse());
	}
}
