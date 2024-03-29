package com.emh.service;

import com.emh.entity.*;
import com.emh.model.Role;
import com.emh.payload.request.*;
import com.emh.payload.response.*;
import com.emh.repos.*;
import com.emh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class TestsService
{

	private final TestsRepository testsRepository;
	private final QuestionsRepository questionsRepository;
	private final StudentTestResultRepository studentTestResultRepository;
	private final QuestAnswerRepository questAnswerRepository;
	private final QuestOptionRepository questOptionRepository;
	private final QuestFileRepository questFileRepository;
	private final CustomUserDetailsService customUserDetailsService;
	private final UserRepository userRepository;

	public TestsService(final TestsRepository testsRepository,
						final QuestionsRepository questionsRepository,
						final StudentTestResultRepository studentTestResultRepository,
						final QuestAnswerRepository questAnswerRepository,
						final QuestOptionRepository questOptionRepository,
						final QuestFileRepository questFileRepository,
						final CustomUserDetailsService customUserDetailsService,
						UserRepository userRepository)
	{
		this.testsRepository = testsRepository;
		this.questionsRepository = questionsRepository;
		this.studentTestResultRepository = studentTestResultRepository;
		this.questAnswerRepository = questAnswerRepository;
		this.questOptionRepository = questOptionRepository;
		this.questFileRepository = questFileRepository;
		this.customUserDetailsService = customUserDetailsService;
		this.userRepository = userRepository;
	}

	public List<TestsResponse> findAll()
	{
		final List<Tests> testses = testsRepository.findAll(Sort.by("testId"));
		return testses.stream()
				.map(this::exportTest)
				.toList();
	}

	public TestsResponse get(final Integer testId)
	{
		return testsRepository.findById(testId)
				.map(this::exportTest)
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final TestsRequest testsRequest) throws IOException
	{
		Tests tests = new Tests();
		EntityMapper.testMapToEntity(testsRequest, tests);
		tests = testsRepository.save(tests);
		saveQuestions(tests, testsRequest);
		return tests.getTestId();
	}

	public void update(final Integer testId, final TestsRequest testsDTO)
	{
		final Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		if (!checkPermission(getCreator(tests.getCreatedBy())))
			throw new ForbiddenException();
		EntityMapper.testMapToEntity(testsDTO, tests);
		testsRepository.save(tests);
	}

	public void delete(final Integer testId)
	{
		final Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		if (!checkPermission(getCreator(tests.getCreatedBy())))
			throw new ForbiddenException();
		testsRepository.deleteById(testId);
	}

	public ReferencedWarning getReferencedWarning(final Integer testId)
	{
		return null;
	}

	private void saveQuestions(Tests tests, TestsRequest testsRequest) throws IOException
	{
		for (QuestionsRequest questionsRequest : testsRequest.getQuestions())
		{
			Questions questions = new Questions();
			EntityMapper.questionMapToEntity(questionsRequest, questions, tests);
			questionsRepository.save(questions);
			saveQuestionAnswers(questions, questionsRequest);
			saveQuestionOptions(questions, questionsRequest);
			saveQuestionFiles(questions, questionsRequest);
			saveSubQuestion(questions, questionsRequest, tests);
		}
	}

	private void saveQuestionAnswers(Questions questions, QuestionsRequest questionsRequest)
	{
		if (questionsRequest.getAnswers() == null)
			return;
		for (QuestAnswerRequest questAnswerRequest : questionsRequest.getAnswers())
		{
			QuestAnswer answer = new QuestAnswer();
			EntityMapper.questionAnswerMapToEntity(questAnswerRequest, answer, questions);
			questAnswerRepository.save(answer);
		}
	}

	private void saveQuestionOptions(Questions questions, QuestionsRequest questionsRequest)
	{
		if (questionsRequest.getOptions() == null)
			return;
		for (QuestOptionRequest questOptionRequest : questionsRequest.getOptions())
		{
			QuestOption option = new QuestOption();
			EntityMapper.questionOptionMapToEntity(questOptionRequest, option, questions);
			questOptionRepository.save(option);
		}
	}

	private void saveQuestionFiles(Questions questions, QuestionsRequest questionsRequest) throws IOException
	{
		if (questionsRequest.getFiles() == null)
			return;
		for (QuestFileRequest questFileRequest : questionsRequest.getFiles())
		{
			QuestFile file = new QuestFile();
			EntityMapper.questionFileMapToEntity(questFileRequest, file, questions);
			questFileRepository.save(file);
		}
	}

	private void saveSubQuestion(Questions parentQuestions, QuestionsRequest questionsRequest, Tests tests) throws IOException
	{
		if (questionsRequest.getSubQuestions() == null)
			return;
		for (QuestionsRequest subQuestionsRequest : questionsRequest.getSubQuestions())
		{
			Questions questions = new Questions();
			EntityMapper.questionMapToEntity(subQuestionsRequest, questions, tests, parentQuestions);
			questionsRepository.save(questions);
			saveQuestionAnswers(questions, subQuestionsRequest);
			saveQuestionOptions(questions, subQuestionsRequest);
			saveQuestionFiles(questions, subQuestionsRequest);
		}
	}

	private TestsResponse exportTest(Tests tests)
	{
		User creator = getCreator(tests.getCreatedBy());
		Boolean permission = checkPermission(creator);
		TestsResponse response = new TestsResponse();
		EntityMapper.testMapToResponse(tests, response);
		exportQuestions(tests, response);
		response.setCreator(creator.getName());
		response.setHavePermission(permission);
		return response;
	}

	private void exportQuestions(Tests tests, TestsResponse testsResponse)
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

	public List<TestsResponse> findAllByUserId(Integer userId)
	{
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		final List<Tests> testses = testsRepository.findAllByCreator(user.getUsername());
		return testses.stream()
				.map(this::exportTest)
				.toList();
	}

	public Boolean checkPassword(Integer testId, String password)
	{
		Tests tests = testsRepository.findById(testId)
				.orElseThrow(NotFoundException::new);
		if (StringUtils.isBlank(tests.getPassword()))
			return true;
		return tests.getPassword().equals(password);
	}
}
