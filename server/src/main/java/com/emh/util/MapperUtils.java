package com.emh.util;

import com.emh.entity.*;
import com.emh.model.ModelMapper;
import com.emh.payload.request.*;
import com.emh.payload.response.*;
import com.emh.service.FilesStorageService;
import org.modelmapper.convention.MatchingStrategies;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class MapperUtils
{
	private static ModelMapper mapper;

	public static <D, T> List<D> mapList(final Collection<T> entityList, Class<D> outCLass)
	{
		if (mapper == null)
			mapper = new ModelMapper(MatchingStrategies.STANDARD);
		return mapper.mapList(entityList, outCLass);
	}

	public static <D> D map(Object source, Class<D> destinationType)
	{
		if (mapper == null)
			mapper = new ModelMapper(MatchingStrategies.STANDARD);
		return mapper.map(source, destinationType);
	}

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
		classesResponse.setTeacher(classes.getTeacher() == null ? null : MapperUtils.teacherMapToResponse(classes.getTeacher(), new TeacherResponse()));
		classesResponse.setAvatar(classes.getAvatar());
		classesResponse.setDescription(classes.getDescription());
		classesResponse.setStartDate(classes.getStartDate());
		classesResponse.setEndDate(classes.getEndDate());
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
		questionsResponse.setExplanation(questions.getExplanation());
		questionsResponse.setTime(questions.getTime());
		questionsResponse.setType(questions.getDescription());
		questionsResponse.setSkill(questions.getSkill());
		return questionsResponse;
	}

	public static Questions questionMapToEntity(final QuestionsRequest questionsRequest, final Questions questions, Tests tests)
	{
		questions.setContent(questionsRequest.getContent());
		questions.setDescription(questionsRequest.getDescription());
		questions.setExplanation(questionsRequest.getExplanation());
		questions.setTime(questionsRequest.getTime());
		questions.setType(questionsRequest.getDescription());
		questions.setTests(tests);
		questions.setSkill(questionsRequest.getSkill());
		return questions;
	}

	public static Questions questionMapToEntity(final QuestionsRequest questionsRequest, final Questions questions, Tests tests, Questions parentQuestions)
	{
		questions.setContent(questionsRequest.getContent());
		questions.setDescription(questionsRequest.getDescription());
		questions.setExplanation(questionsRequest.getExplanation());
		questions.setTime(questionsRequest.getTime());
		questions.setType(questionsRequest.getDescription());
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
}