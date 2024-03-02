package com.emh.service;

import com.emh.entity.*;
import com.emh.payload.response.*;
import com.emh.repos.ClassesRepository;
import com.emh.repos.TestClassRepository;
import com.emh.repos.TestsRepository;
import com.emh.util.MapperUtils;
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

	public List<TestsResponse> findAllByClass(Integer classId)
	{
		final Classes classs = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		return classs.getTestClasses().stream()
				.map(testClass -> exportTest(testClass.getTests()))
				.toList();
	}

	public void create(Integer classId, Integer testId)
	{
		TestClass testClass = new TestClass();
		Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		testClass.setTests(tests);
		testClass.setClasss(classes);
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


	private TestsResponse exportTest(Tests tests)
	{
		TestsResponse response = new TestsResponse();
		MapperUtils.testMapToResponse(tests, response);
		exportQuestions(tests, response);
		return response;
	}

	private void exportQuestions(Tests tests, TestsResponse testsResponse)
	{
		if(tests.getQuestions() == null)
			return;
		List<QuestionsResponse> responses = new ArrayList<>();
		for (Questions questions : tests.getQuestions())
		{
			if (questions.getParentQuestion() != null)
				continue;
			QuestionsResponse questionsResponse = new QuestionsResponse();
			MapperUtils.questionMapToResponse(questions, questionsResponse);
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
			MapperUtils.questionAnswerMapToResponse(questAnswer, answer);
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
			MapperUtils.questionOptionMapToResponse(questOption, option);
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
			MapperUtils.questionFileMapToResponse(questFile, file);
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
			MapperUtils.questionMapToResponse(subQuestions, questionsResponse);
			exportQuestionAnswers(subQuestions, questionsResponse);
			exportQuestionOptions(subQuestions, questionsResponse);
			exportQuestionFiles(subQuestions, questionsResponse);
			subQuest.add(questionsResponse);
		}
		parentQuestions.setSubQuestions(subQuest);
	}
}
