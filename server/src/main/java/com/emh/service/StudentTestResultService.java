package com.emh.service;

import com.emh.entity.*;
import com.emh.payload.request.QuestAnswerResultRequest;
import com.emh.payload.request.StudentTestResultRequest;
import com.emh.payload.response.QuestAnswerResultResponse;
import com.emh.payload.response.StudentTestResultResponse;
import com.emh.repos.*;
import com.emh.util.EntityMapper;
import com.emh.util.NotFoundException;
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
	private final ClassesRepository classesRepository;

	public StudentTestResultService(final TestsRepository testsRepository,
									final QuestionsRepository questionsRepository,
									final StudentTestResultRepository studentTestResultRepository,
									final QuestAnswerRepository questAnswerRepository,
									final QuestOptionRepository questOptionRepository,
									final QuestFileRepository questFileRepository,
									final CustomUserDetailsService customUserDetailsService,
									UserRepository userRepository,
									QuestAnswerResultRepository questAnswerResultRepository,
									StudentRepository studentRepository,
									ClassesRepository classesRepository)
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
		this.classesRepository = classesRepository;
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
		Classes classs = classesRepository.findById(studentTestResultRequest.getClassId())
				.orElseThrow(NotFoundException::new);
		studentTestResult.setTests(tests);
		studentTestResult.setStudent(student);
		studentTestResult.setClasss(classs);
		EntityMapper.testResultMapToEntity(studentTestResultRequest, studentTestResult);
		studentTestResult = studentTestResultRepository.save(studentTestResult);
		saveAnswersResult(studentTestResult, studentTestResultRequest);
		return tests.getTestId();
	}

	public void update(final Integer resultId, final StudentTestResultRequest studentTestResultRequest) throws IOException
	{
		StudentTestResult studentTestResult = studentTestResultRepository.findById(resultId)
				.orElseThrow(NotFoundException::new);
		Tests tests = testsRepository.findById(studentTestResultRequest.getTestId())
				.orElseThrow(NotFoundException::new);
		Student student = studentRepository.findById(studentTestResultRequest.getStudentId())
				.orElseThrow(NotFoundException::new);
		Classes classs = classesRepository.findById(studentTestResultRequest.getClassId())
				.orElseThrow(NotFoundException::new);
		studentTestResult.setTests(tests);
		studentTestResult.setStudent(student);
		studentTestResult.setClasss(classs);
		EntityMapper.testResultMapToEntity(studentTestResultRequest, studentTestResult);
		studentTestResult = studentTestResultRepository.save(studentTestResult);
		questAnswerResultRepository.deleteAllByStudentTestResult(studentTestResult);
		saveAnswersResult(studentTestResult, studentTestResultRequest);
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
