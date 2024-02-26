package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.TestClass;
import com.emh.entity.Tests;
import com.emh.payload.response.TestsResponse;
import com.emh.repos.ClassesRepository;
import com.emh.repos.TestClassRepository;
import com.emh.repos.TestsRepository;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import org.springframework.stereotype.Service;

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
				.map(testClass -> MapperUtils.testMapToResponse(testClass.getTests(), new TestsResponse()))
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

}
