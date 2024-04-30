package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.Student;
import com.emh.entity.User;
import com.emh.model.Role;
import com.emh.payload.request.StudentRequest;
import com.emh.payload.response.StudentResponse;
import com.emh.repos.ClassesRepository;
import com.emh.repos.StudentRepository;
import com.emh.repos.StudentTestResultRepository;
import com.emh.repos.UserRepository;
import com.emh.specifications.FilterOperation;
import com.emh.specifications.SearchCriteria;
import com.emh.specifications.SpecificationsBuilder;
import com.emh.util.*;
import csv.impl.AbstractStreamTableReader;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
public class StudentService
{

	private final StudentRepository studentRepository;
	private final ClassesRepository classesRepository;
	private final UserRepository userRepository;
	private final StudentTestResultRepository studentTestResultRepository;

	public StudentService(final StudentRepository studentRepository,
						  final ClassesRepository classesRepository, final UserRepository userRepository,
						  final StudentTestResultRepository studentTestResultRepository)
	{
		this.studentRepository = studentRepository;
		this.classesRepository = classesRepository;
		this.userRepository = userRepository;
		this.studentTestResultRepository = studentTestResultRepository;
	}

	public List<StudentResponse> findAll()
	{
		final List<Student> students = studentRepository.findAll(Sort.by("studentId"));
		return students.stream()
				.map(student -> EntityMapper.studentMapToResponse(student, new StudentResponse()))
				.toList();
	}

	public StudentResponse get(final Integer studentId)
	{
		return studentRepository.findById(studentId)
				.map(student -> EntityMapper.studentMapToResponse(student, new StudentResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final StudentRequest studentRequest)
	{
		if (userRepository.findOneByUsername(studentRequest.getUsername()) != null)
			throw new AppException("Account Already Exists");
		final Student student = new Student();
		studentRequest.setPassword(new BCryptPasswordEncoder().encode(studentRequest.getPassword()));
		User user = MapperUtils.map(studentRequest, User.class);
		user.setRole(Role.STUDENT.toString());
		user.setStatus(1);
		student.setStatus(1);
		user = userRepository.save(user);
		Classes classs = classesRepository.findById(studentRequest.getClassId())
				.orElseThrow(NotFoundException::new);
		EntityMapper.studentMapToEntity(studentRequest, student, user, classs);
		return studentRepository.save(student).getStudentId();
	}

	@Transactional
	public void update(final Integer studentId, final StudentRequest studentRequest)
	{
		final Student student = studentRepository.findById(studentId)
				.orElseThrow(NotFoundException::new);
		studentRequest.setPassword(student.getPassword());
		User user = MapperUtils.map(studentRequest, User.class);
		user.setUserId(student.getUser().getUserId());
		user.setRole(Role.STUDENT.toString());
		user.setStatus(student.getUser().getStatus());
		user = userRepository.save(user);
		Classes classs = classesRepository.findById(studentRequest.getClassId())
				.orElseThrow(NotFoundException::new);
		EntityMapper.studentMapToEntity(studentRequest, student, user, classs);
		studentRepository.save(student);
	}

	@Transactional
	public void delete(final Integer studentId)
	{
		final Student student = studentRepository.findById(studentId)
				.orElseThrow(NotFoundException::new);
		userRepository.delete(student.getUser());
		studentRepository.deleteById(studentId);
	}

	public ReferencedWarning getReferencedWarning(final Integer studentId)
	{
		return null;
	}

	public List<StudentResponse> searchStudent(String username, String email, String name, Integer classId) throws Exception
	{
		SpecificationsBuilder<Student> spec = new SpecificationsBuilder<>();
		if (StringUtils.isNotBlank(username))
			spec.with(new SearchCriteria("username", FilterOperation.EQUAL.toString(), username, false));
		if (StringUtils.isNotBlank(email))
			spec.with(new SearchCriteria("email", FilterOperation.EQUAL.toString(), email, false));
		if (StringUtils.isNotBlank(name))
			spec.with(new SearchCriteria("name", FilterOperation.EQUAL.toString(), name, false));
		if (ObjectUtils.defaultIfNull(classId, 0) != 0)
			spec.with(new SearchCriteria("class_id", FilterOperation.EQUAL.toString(), classId, false));
		final List<Student> students = studentRepository.findAll(spec.build());
		return students.stream()
				.map(student -> EntityMapper.studentMapToResponse(student, new StudentResponse()))
				.toList();
	}

	public void updateStatus(Integer id, Integer status)
	{
		final Student student = studentRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		User user = student.getUser();
		student.setStatus(status == 1 ? 1 : 0);
		user.setStatus(status == 1 ? 1 : 0);
		userRepository.save(user);
		studentRepository.save(student);
	}

	public void updatePassword(Integer id, String password)
	{
		password = new BCryptPasswordEncoder().encode(password);
		final Student student = studentRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		User user = student.getUser();
		student.setPassword(password);
		user.setPassword(password);
		userRepository.save(user);
		studentRepository.save(student);
	}

	public StudentResponse getByUserId(Integer userId)
	{
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		Student student = studentRepository.findFirstByUser(user);
		return EntityMapper.studentMapToResponse(student, new StudentResponse());
	}

	public List<Integer> importFile(MultipartFile multipartFile, Integer classId) throws Exception
	{
		List<Integer> results = new ArrayList<>();
		AbstractStreamTableReader in = FileReaderUtil.from(multipartFile);
		while (in.hasNext())
		{
			Object[] columns = in.next();
			String name = (String) columns[0];
			String genderStr = columns.length >= 2 ? (String) columns[1] : "Nam";
			Integer gender = genderStr.equalsIgnoreCase("Nam") ? 1 : 0;
			StudentRequest student = new StudentRequest();
			student.setName(name);
			student.setGender(gender);
			student.setUsername(UsernameGeneratorUtils.generateUsername(name));
			student.setPassword("123456");
			student.setClassId(classId);
			results.add(this.create(student));
		}
		in.close();
		return results;
	}
}
