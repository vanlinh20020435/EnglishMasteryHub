package com.emh.service;

import com.emh.entity.*;
import com.emh.payload.request.*;
import com.emh.payload.response.*;
import com.emh.repos.*;
import com.emh.util.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class StudentTestResultService
{

	private final TestsRepository testsRepository;
	private final QuestionsRepository questionsRepository;
	private final StudentTestResultRepository studentTestResultRepository;
	private final QuestAnswerRepository questAnswerRepository;
	private final QuestOptionRepository questOptionRepository;
	private final QuestFileRepository questFileRepository;
	private final CustomUserDetailsService customUserDetailsService;
	private final UserRepository userRepository;
	private final QuestAnswerResultRepository questAnswerResultRepository;
	private final StudentRepository studentRepository;

	public StudentTestResultService(final TestsRepository testsRepository,
									final QuestionsRepository questionsRepository,
									final StudentTestResultRepository studentTestResultRepository,
									final QuestAnswerRepository questAnswerRepository,
									final QuestOptionRepository questOptionRepository,
									final QuestFileRepository questFileRepository,
									final CustomUserDetailsService customUserDetailsService,
									UserRepository userRepository,
									QuestAnswerResultRepository questAnswerResultRepository,
									StudentRepository studentRepository)
	{
		this.testsRepository = testsRepository;
		this.questionsRepository = questionsRepository;
		this.studentTestResultRepository = studentTestResultRepository;
		this.questAnswerRepository = questAnswerRepository;
		this.questOptionRepository = questOptionRepository;
		this.questFileRepository = questFileRepository;
		this.customUserDetailsService = customUserDetailsService;
		this.userRepository = userRepository;
		this.questAnswerResultRepository = questAnswerResultRepository;
		this.studentRepository = studentRepository;
	}

	public List<StudentTestResultResponse> findAll()
	{
		final List<StudentTestResult> testResults = studentTestResultRepository.findAll(Sort.by("id"));
		return testResults.stream()
				.map(this::exportResult)
				.toList();
	}

	public StudentTestResultResponse get(final Integer resultId)
	{
		return studentTestResultRepository.findById(resultId)
				.map(this::exportResult)
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final StudentTestResultRequest studentTestResultRequest) throws IOException
	{
		StudentTestResult studentTestResult = new StudentTestResult();
		Tests tests = testsRepository.findById(studentTestResultRequest.getTestId())
				.orElseThrow(NotFoundException::new);
		Student student = studentRepository.findById(studentTestResultRequest.getStudentId())
				.orElseThrow(NotFoundException::new);
		studentTestResult.setTests(tests);
		studentTestResult.setStudent(student);
		EntityMapper.testResultMapToEntity(studentTestResultRequest, studentTestResult, tests, student);
		studentTestResult = studentTestResultRepository.save(studentTestResult);
		saveAnswersResult(studentTestResult, studentTestResultRequest);
		return tests.getTestId();
	}

	public void update(final Integer testId, final TestsRequest testsDTO)
	{
		final Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		EntityMapper.testMapToEntity(testsDTO, tests);
		testsRepository.save(tests);
	}

	public void delete(final Integer testId)
	{
		final Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		testsRepository.deleteById(testId);
	}

	private void saveAnswersResult(StudentTestResult studentTestResult, StudentTestResultRequest studentTestResultRequest) throws IOException
	{
		for (QuestAnswerResultRequest questAnswerResultRequest : studentTestResultRequest.getQuestionResults())
		{
			QuestAnswerResult answerResult = new QuestAnswerResult();
			Questions questions = questionsRepository.findById(questAnswerResultRequest.getQuestionId())
								.orElseThrow(NotFoundException::new);
			EntityMapper.questAnswerResultMapToEntity(questAnswerResultRequest, answerResult, questions, studentTestResult);
			questAnswerResultRepository.save(answerResult);
		}
	}

	private StudentTestResultResponse exportResult(StudentTestResult studentTestResult)
	{
		StudentTestResultResponse response = new StudentTestResultResponse();
		EntityMapper.testResultMapToResponse(studentTestResult, response);
		exportQuestAnswer(studentTestResult, response);
		return response;
	}

	private void exportQuestAnswer(StudentTestResult studentTestResult, StudentTestResultResponse studentTestResultResponse)
	{
		if (studentTestResult.getQuestAnswerResults() == null)
			return;
		List<QuestAnswerResultResponse> responses = new ArrayList<>();
		for (QuestAnswerResult questAnswerResult : studentTestResult.getQuestAnswerResults())
		{
			QuestAnswerResultResponse response = new QuestAnswerResultResponse();
			EntityMapper.questAnswerResultMapToResponse(questAnswerResult, response);
			responses.add(response);
		}
		studentTestResultResponse.setQuestionResults(responses);
	}
}
