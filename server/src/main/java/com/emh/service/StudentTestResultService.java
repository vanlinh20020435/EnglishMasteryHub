package com.emh.service;

import com.emh.entity.*;
import com.emh.model.Role;
import com.emh.payload.request.QuestAnswerResultRequest;
import com.emh.payload.request.StudentTestResultRequest;
import com.emh.payload.response.*;
import com.emh.repos.*;
import com.emh.specifications.FilterOperation;
import com.emh.specifications.SearchForeignCriteria;
import com.emh.specifications.SpecificationsBuilder;
import com.emh.util.EntityMapper;
import com.emh.util.NotFoundException;
import com.emh.util.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
public class StudentTestResultService
{
	private final JdbcTemplate jdbcTemplate;
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
									ClassesRepository classesRepository,
									JdbcTemplate jdbcTemplate)
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
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<StudentTestResultResponse> findAll()
	{
		final List<StudentTestResult> testResults = studentTestResultRepository.findAll(Sort.by("id"));
		return testResults.stream()
				.map(this::exportResult)
				.toList();
	}

	public List<StudentTestResultResponse> findAll(Integer classId, Integer testId, Integer studentId) throws Exception
	{
		SpecificationsBuilder<StudentTestResult> spec = new SpecificationsBuilder<>();
		if (ObjectUtils.defaultIfNull(classId, 0) != 0)
			spec.with(new SearchForeignCriteria("classs", "classId", FilterOperation.FOREIGN_KEY.toString(), classId, false));
		if (ObjectUtils.defaultIfNull(testId, 0) != 0)
			spec.with(new SearchForeignCriteria("tests", "testId", FilterOperation.FOREIGN_KEY.toString(), testId, false));
		if (ObjectUtils.defaultIfNull(studentId, 0) != 0)
			spec.with(new SearchForeignCriteria("student", "studentId", FilterOperation.FOREIGN_KEY.toString(), studentId, false));
		final List<StudentTestResult> testResults = studentTestResultRepository.findAll(spec.build(), Sort.by("id"));
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
		studentTestResult.setRequiresGrading(tests.getRequiresGrading());
		studentTestResult.setStudent(student);
		studentTestResult.setClasss(classs);
		EntityMapper.testResultMapToEntity(studentTestResultRequest, studentTestResult);
		studentTestResult = studentTestResultRepository.save(studentTestResult);
		saveAnswersResult(studentTestResult, studentTestResultRequest);
		return tests.getTestId();
	}

	@Transactional
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
		studentTestResult.setRequiresGrading(false);
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
		response.setTest(exportTest(studentTestResult.getTests(), studentTestResult));
		response.setStudent(EntityMapper.studentMapToResponse(studentTestResult.getStudent(), new StudentResponse()));
		return response;
	}

	public TestResultStatisticResponse statistic(Integer classId, Integer testId)
	{
		long now = System.currentTimeMillis();
		TestResultStatisticResponse response = new TestResultStatisticResponse();
		List<StudentSummaryResponse> studentSummaryResponses = studentTestResultRepository.statistic(classId, testId).stream()
				.map(this::mapSummaryResponses).toList();
		System.out.println(System.currentTimeMillis() - now);
		long completed = studentSummaryResponses.stream().filter(result -> ObjectUtils.allNotNull(result.getResult())).count();
		response.setStatistic(studentSummaryResponses);
		response.setCompleted(completed);
		response.setIncomplete(studentSummaryResponses.size() - completed);
		return response;
	}

	public List<StudentDetailSummaryResponse> statistic(Integer classId, Integer testId, Integer studentId)
	{
		return studentTestResultRepository.statistic(classId, testId, studentId).stream()
				.map(this::mapStudentDetailSummaryResponses).toList();
	}

	public StudentSummaryResponse mapSummaryResponses(IStudentSummaryResponse response) {
		StudentSummaryResponse mapResult = new StudentSummaryResponse();
		if(response.getTestResultId() != null)
		{
			StudentTestResult studentTestResult = studentTestResultRepository.findById(response.getTestResultId())
					.orElseThrow(NotFoundException::new);
			mapResult.setResult(new StudentSummaryResponse.Result(studentTestResult));
		}
		if(response.getStudentId() != null)
		{
			Student student = studentRepository.findById(response.getStudentId())
					.orElseThrow(NotFoundException::new);
			mapResult.setStudent(new StudentSummaryResponse.StudentInfo(student));
		}
		return mapResult;
	}

	public StudentDetailSummaryResponse mapStudentDetailSummaryResponses(IStudentSummaryResponse response) {
		StudentDetailSummaryResponse mapResult = new StudentDetailSummaryResponse();
		if(response.getTestResultId() != null)
		{
			StudentTestResult studentTestResult = studentTestResultRepository.findById(response.getTestResultId())
					.orElseThrow(NotFoundException::new);
			mapResult.setResult(new StudentDetailSummaryResponse.Result(studentTestResult));
		}
		return mapResult;
	}

	private TestResultResponse exportTest(Tests tests, StudentTestResult studentTestResult)
	{
		User creator = getCreator(tests.getCreatedBy());
		Boolean permission = checkPermission(creator);
		TestResultResponse response = new TestResultResponse();
		EntityMapper.testResultMapToResponse(tests, response);
		exportQuestions(tests, response, studentTestResult);
		response.setCreator(creator.getName());
		response.setHavePermission(permission);
		return response;
	}

	private void exportQuestions(Tests tests, TestResultResponse testResultResponse, StudentTestResult studentTestResult)
	{
		if (tests.getQuestions() == null)
			return;
		List<QuestResultResponse> responses = new ArrayList<>();
		for (Questions questions : tests.getQuestions())
		{
			if (questions.getParentQuestion() != null)
				continue;
			QuestResultResponse questResultResponse = new QuestResultResponse();
			EntityMapper.questionResultMapToResponse(questions, questResultResponse);
			exportQuestionAnswers(questions, questResultResponse);
			exportQuestionOptions(questions, questResultResponse);
			exportQuestionFiles(questions, questResultResponse);
			exportSubQuestion(questions, questResultResponse, studentTestResult);
			mapResultToQuestion(questResultResponse, studentTestResult);
			responses.add(questResultResponse);
		}
		responses.sort((q1, q2) -> q1.getQuestionId().compareTo(q2.getQuestionId()));
		testResultResponse.setQuestions(responses);
	}

	private void mapResultToQuestion(QuestResultResponse questResultResponse, StudentTestResult studentTestResult)
	{
		for (QuestAnswerResult questAnswerResult : studentTestResult.getQuestAnswerResults())
		{
			if(questAnswerResult.getQuestion().getQuestionId().equals(questResultResponse.getQuestionId()))
			{
				questResultResponse.setStudentResult(EntityMapper.questAnswerResultMapToResponse(questAnswerResult, new QuestAnswerResultResponse()));
				return;
			}
		}
	}

	private void exportQuestionAnswers(Questions questions, QuestResultResponse questResultResponse)
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
		questResultResponse.setAnswers(responses);
	}

	private void exportQuestionOptions(Questions questions, QuestResultResponse questResultResponse)
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
		questResultResponse.setOptions(responses);
	}

	private void exportQuestionFiles(Questions questions, QuestResultResponse QuestResultResponse)
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
		QuestResultResponse.setFiles(responses);
	}

	private void exportSubQuestion(Questions questions, QuestResultResponse parentQuestions, StudentTestResult studentTestResult)
	{
		if (questions.getQuestions() == null)
			return;
		List<QuestResultResponse> subQuest = new ArrayList<>();
		for (Questions subQuestions : questions.getQuestions())
		{
			QuestResultResponse questResultResponse = new QuestResultResponse();
			EntityMapper.questionResultMapToResponse(subQuestions, questResultResponse);
			exportQuestionAnswers(subQuestions, questResultResponse);
			exportQuestionOptions(subQuestions, questResultResponse);
			exportQuestionFiles(subQuestions, questResultResponse);
			mapResultToQuestion(questResultResponse, studentTestResult);
			subQuest.add(questResultResponse);
		}
		subQuest.sort((q1, q2) -> q1.getQuestionId().compareTo(q2.getQuestionId()));
		parentQuestions.setSubQuestions(subQuest);
	}

	private User getCreator(String creator)
	{
		return userRepository.findOneByUsername(creator);
	}

	private boolean checkPermission(User creator)
	{
		UserDetailsImpl userDetails = SecurityUtils.getPrincipal();
		User currentUser = userRepository.findById(userDetails.getId())
				.orElseThrow(NotFoundException::new);
		return checkPermission(currentUser, creator);
	}

	private boolean checkPermission(User curentUser, User targetUser)
	{
		if (curentUser.getRole().equalsIgnoreCase(Role.ADMIN.toString()))
			return true;
		return Objects.equals(curentUser.getUserId(), targetUser.getUserId());
	}
}
