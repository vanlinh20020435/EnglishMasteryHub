package com.emh.util;

import com.emh.entity.*;
import com.emh.payload.request.*;
import com.emh.payload.response.*;
import com.emh.service.FilesStorageService;

import java.io.IOException;

public class EntityMapper
{

	public static AdminResponse adminMapToResponse(final Admin admin, final AdminResponse adminResponse)
	{
		adminResponse.setAdminId(admin.getAdminId());
		adminResponse.setUsername(admin.getUsername());
		adminResponse.setEmail(admin.getEmail());
		adminResponse.setName(admin.getName());
		adminResponse.setGender(admin.getGender());
		adminResponse.setStatus(admin.getStatus());
		adminResponse.setAvatar(admin.getAvatar());
		adminResponse.setBirthday(admin.getBirthday());
		return adminResponse;
	}

	public static Admin adminMapToEntity(final AdminRequest adminRequest, final Admin admin, final User user)
	{
		admin.setUsername(adminRequest.getUsername());
		admin.setEmail(adminRequest.getEmail());
		admin.setPassword(adminRequest.getPassword());
		admin.setName(adminRequest.getName());
		admin.setGender(adminRequest.getGender());
		admin.setStatus(1);
		admin.setAvatar(adminRequest.getAvatar());
		admin.setBirthday(adminRequest.getBirthday());
		if (user == null)
			throw new NotFoundException("user not found");
		admin.setUser(user);
		return admin;
	}

	public static TeacherResponse teacherMapToResponse(final Teacher teacher, final TeacherResponse teacherResponse)
	{
		teacherResponse.setTeacherId(teacher.getTeacherId());
		teacherResponse.setUsername(teacher.getUsername());
		teacherResponse.setEmail(teacher.getEmail());
		teacherResponse.setName(teacher.getName());
		teacherResponse.setGender(teacher.getGender());
		teacherResponse.setAvatar(teacher.getAvatar());
		teacherResponse.setStatus(teacher.getStatus());
		teacherResponse.setBirthday(teacher.getBirthday());
		return teacherResponse;
	}

	public static Teacher teacherMapToEntity(final TeacherRequest teacherRequest, final Teacher teacher, final User user)
	{
		teacher.setUsername(teacherRequest.getUsername());
		teacher.setEmail(teacherRequest.getEmail());
		teacher.setPassword(teacherRequest.getPassword());
		teacher.setName(teacherRequest.getName());
		teacher.setGender(teacherRequest.getGender());
		teacher.setAvatar(teacherRequest.getAvatar());
		teacher.setBirthday(teacherRequest.getBirthday());
		if (user == null)
			throw new NotFoundException("user not found");
		teacher.setUser(user);
		return teacher;
	}

	public static StudentResponse studentMapToResponse(final Student student, final StudentResponse studentRequest)
	{
		studentRequest.setStudentId(student.getStudentId());
		studentRequest.setUsername(student.getUsername());
		studentRequest.setEmail(student.getEmail());
		studentRequest.setName(student.getName());
		studentRequest.setGender(student.getGender());
		studentRequest.setStatus(student.getStatus());
		studentRequest.setAvatar(student.getAvatar());
		studentRequest.setBirthday(student.getBirthday());
		studentRequest.setClasss(classMapToResponse(student.getClasss(), new ClassesResponse()));
		return studentRequest;
	}

	public static Student studentMapToEntity(final StudentRequest studentRequest, final Student student, final User user, final Classes classs)
	{
		student.setUsername(studentRequest.getUsername());
		student.setEmail(studentRequest.getEmail());
		student.setPassword(studentRequest.getPassword());
		student.setName(studentRequest.getName());
		student.setGender(studentRequest.getGender());
		student.setStatus(1);
		student.setAvatar(studentRequest.getAvatar());
		student.setBirthday(studentRequest.getBirthday());
		if (classs == null)
			throw new NotFoundException("class not found");
		student.setClasss(classs);
		if (user == null)
			throw new NotFoundException("user not found");
		student.setUser(user);
		return student;
	}

	public static ClassesResponse classMapToResponse(final Classes classes, final ClassesResponse classesResponse)
	{
		classesResponse.setClassId(classes.getClassId());
		classesResponse.setClassName(classes.getClassName());
		classesResponse.setTeacher(classes.getTeacher() == null ? null : teacherMapToResponse(classes.getTeacher(), new TeacherResponse()));
		classesResponse.setAvatar(classes.getAvatar());
		classesResponse.setDescription(classes.getDescription());
		classesResponse.setStartDate(classes.getStartDate());
		classesResponse.setEndDate(classes.getEndDate());
		classesResponse.setTotalStudent(classes.getClassStudents().size());
		return classesResponse;
	}

	public static Classes classMapToEntity(final ClassesRequest classesRequest, final Classes classes, final Teacher teacher)
	{
		classes.setClassName(classesRequest.getClassName());
		classes.setAvatar(classesRequest.getAvatar());
		classes.setDescription(classesRequest.getDescription());
		classes.setStartDate(classesRequest.getStartDate());
		classes.setEndDate(classesRequest.getEndDate());
		if (teacher == null)
			throw new NotFoundException("teacher not found");
		classes.setTeacher(teacher);
		return classes;
	}

	public static TestsResponse testMapToResponse(final Tests tests, final TestsResponse testsResponse)
	{
		testsResponse.setTestId(tests.getTestId());
		testsResponse.setTestName(tests.getTestName());
		testsResponse.setPassword(tests.getPassword());
		testsResponse.setTotalQuestions(tests.getTotalQuestions());
		testsResponse.setTime(tests.getTime());
		testsResponse.setDescription(tests.getDescription());
		testsResponse.setStatus(tests.getStatus());
		testsResponse.setCreatedDate(tests.getCreatedDate());
		return testsResponse;
	}

	public static TestClassResponse testClassMapToResponse(final Tests tests, final TestClassResponse testsResponse)
	{
		testsResponse.setTestId(tests.getTestId());
		testsResponse.setTestName(tests.getTestName());
		testsResponse.setPassword(tests.getPassword());
		testsResponse.setTotalQuestions(tests.getTotalQuestions());
		testsResponse.setTime(tests.getTime());
		testsResponse.setDescription(tests.getDescription());
		testsResponse.setStatus(tests.getStatus());
		testsResponse.setCreatedDate(tests.getCreatedDate());
		return testsResponse;
	}

	public static TestClassInfoResponse testInfoMapToResponse(final TestClass testClass, final TestClassInfoResponse testInfoResponse)
	{
		Tests tests = testClass.getTests();
		testInfoResponse.setTestId(tests.getTestId());
		testInfoResponse.setTestName(tests.getTestName());
		testInfoResponse.setTotalQuestions(tests.getTotalQuestions());
		testInfoResponse.setTime(tests.getTime());
		testInfoResponse.setDescription(tests.getDescription());
		testInfoResponse.setStatus(tests.getStatus());
		testInfoResponse.setCreatedDate(tests.getCreatedDate());
		if(testClass.getStartDate() != null)
			testInfoResponse.setStartDate(testClass.getStartDate());;
		if(testClass.getEndDate() != null)
			testInfoResponse.setEndDate(testClass.getEndDate());
		return testInfoResponse;
	}

	public static Tests testMapToEntity(final TestsRequest testsRequest, final Tests tests)
	{
		tests.setTestName(testsRequest.getTestName());
		tests.setPassword(testsRequest.getPassword());
		tests.setTotalQuestions(testsRequest.getQuestions().size());
		tests.setTime(testsRequest.getTime());
		tests.setDescription(testsRequest.getDescription());
		tests.setStatus(testsRequest.getStatus());
		return tests;
	}

	public static QuestionsResponse questionMapToResponse(final Questions questions, final QuestionsResponse questionsResponse)
	{
		questionsResponse.setQuestionId(questions.getQuestionId());
		questionsResponse.setContent(questions.getContent());
		questionsResponse.setDescription(questions.getDescription());
		questionsResponse.setTitle(questions.getTitle());
		questionsResponse.setTime(questions.getTime());
		questionsResponse.setType(questions.getDescription());
		questionsResponse.setSkill(questions.getSkill());
		return questionsResponse;
	}

	public static Questions questionMapToEntity(final QuestionsRequest questionsRequest, final Questions questions, Tests tests)
	{
		questions.setContent(questionsRequest.getContent());
		questions.setDescription(questionsRequest.getDescription());
		questions.setTitle(questionsRequest.getTitle());
		questions.setTime(questionsRequest.getTime());
		questions.setType(questionsRequest.getType());
		questions.setTests(tests);
		questions.setSkill(questionsRequest.getSkill());
		return questions;
	}

	public static Questions questionMapToEntity(final QuestionsRequest questionsRequest, final Questions questions, Tests tests, Questions parentQuestions)
	{
		questions.setContent(questionsRequest.getContent());
		questions.setDescription(questionsRequest.getDescription());
		questions.setTitle(questionsRequest.getTitle());
		questions.setTime(questionsRequest.getTime());
		questions.setType(questionsRequest.getType());
		questions.setTests(tests);
		questions.setParentQuestion(parentQuestions);
		questions.setSkill(questionsRequest.getSkill());
		return questions;
	}

	public static QuestAnswerResponse questionAnswerMapToResponse(final QuestAnswer questAnswer, final QuestAnswerResponse questAnswerResponse)
	{
		questAnswerResponse.setAnswer(questAnswer.getAnswer());
		questAnswerResponse.setExplanation(questAnswer.getExplanation());
		return questAnswerResponse;
	}

	public static QuestAnswer questionAnswerMapToEntity(final QuestAnswerRequest questAnswerRequest, final QuestAnswer questAnswer, Questions questions)
	{
		questAnswer.setAnswer(questAnswerRequest.getAnswer());
		questAnswer.setExplanation(questAnswerRequest.getExplanation());
		questAnswer.setQuestion(questions);
		return questAnswer;
	}

	public static QuestOptionResponse questionOptionMapToResponse(final QuestOption questOption, final QuestOptionResponse questOptionResponse)
	{
		questOptionResponse.setOption(questOption.getOption());
		return questOptionResponse;
	}

	public static QuestOption questionOptionMapToEntity(final QuestOptionRequest questOptionRequest, final QuestOption questOption, Questions questions)
	{
		questOption.setOption(questOptionRequest.getOption());
		questOption.setQuestion(questions);
		return questOption;
	}

	public static QuestFileResponse questionFileMapToResponse(final QuestFile questFile, final QuestFileResponse questFileResponse)
	{
		questFileResponse.setType(questFile.getType());
		questFileResponse.setUrl(questFile.getUrl());
		questFileResponse.setName(questFile.getName());
		questFileResponse.setId(questFile.getId());
		return questFileResponse;
	}

	public static QuestFile questionFileMapToEntity(final QuestFileRequest questFileRequest, final QuestFile questFile, Questions questions) throws IOException
	{
		questFile.setType(questFileRequest.getType());
		questFile.setUrl(FilesStorageService.moveFileUploadsToContent(questFileRequest.getUrl()));
		questFile.setName(questFileRequest.getName());
		questFile.setQuestion(questions);
		return questFile;
	}

	public static StudentTestResultResponse testResultMapToResponse(final StudentTestResult studentTestResult, final StudentTestResultResponse testResultResponse)
	{
		testResultResponse.setId(studentTestResult.getId());
		testResultResponse.setScore(studentTestResult.getScore());
		testResultResponse.setTestDefaultScore(studentTestResult.getTestDefaultScore());
		testResultResponse.setTime(studentTestResult.getTime());
		return testResultResponse;
	}

	public static StudentTestResult testResultMapToEntity(final StudentTestResultRequest studentTestResultRequest, final StudentTestResult studentTestResult,
														  final Tests tests, final Student student)
	{
		studentTestResult.setScore(studentTestResultRequest.getScore());
		studentTestResult.setTestDefaultScore(studentTestResultRequest.getTestDefaultScore());
		studentTestResult.setTime(studentTestResultRequest.getTime());
		studentTestResult.setTests(tests);
		studentTestResult.setStudent(student);
		return studentTestResult;
	}

	public static QuestAnswerResultResponse questAnswerResultMapToResponse(final QuestAnswerResult questAnswerResult,
																		   final QuestAnswerResultResponse questAnswerResultResponse)
	{
		questAnswerResultResponse.setId(questAnswerResult.getId());
		questAnswerResultResponse.setScore(questAnswerResult.getScore());
		questAnswerResultResponse.setRightAnswer(questAnswerResult.getRightAnswer());
		questAnswerResultResponse.setDefaultScore(questAnswerResult.getDefaultScore());
		questAnswerResultResponse.setAnswers(questAnswerResult.getAnswers());
		return questAnswerResultResponse;
	}

	public static QuestAnswerResult questAnswerResultMapToEntity(final QuestAnswerResultRequest answerResultRequest, final QuestAnswerResult questAnswerResult,
																 final Questions questions, final StudentTestResult studentTestResult)
	{
		questAnswerResult.setScore(answerResultRequest.getScore());
		questAnswerResult.setAnswers(answerResultRequest.getAnswers());
		questAnswerResult.setRightAnswer(answerResultRequest.getRightAnswer());
		questAnswerResult.setDefaultScore(answerResultRequest.getDefaultScore());
		questAnswerResult.setQuestion(questions);
		questAnswerResult.setStudentTestResult(studentTestResult);
		return questAnswerResult;
	}
}
