package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.Student;
import com.emh.entity.User;
import com.emh.model.Role;
import com.emh.payload.request.StudentRequest;
import com.emh.payload.response.StudentResponse;
import com.emh.repos.*;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentService
{

	private final StudentRepository studentRepository;
	private final ClassesRepository classesRepository;
	private final UserRepository userRepository;
	private final ScoresRepository scoresRepository;
	private final StudentTestDetailRepository studentTestDetailRepository;

	public StudentService(final StudentRepository studentRepository,
						  final ClassesRepository classesRepository, final UserRepository userRepository,
						  final ScoresRepository scoresRepository,
						  final StudentTestDetailRepository studentTestDetailRepository)
	{
		this.studentRepository = studentRepository;
		this.classesRepository = classesRepository;
		this.userRepository = userRepository;
		this.scoresRepository = scoresRepository;
		this.studentTestDetailRepository = studentTestDetailRepository;
	}

	public List<StudentResponse> findAll()
	{
		final List<Student> students = studentRepository.findAll(Sort.by("studentId"));
		return students.stream()
				.map(student -> MapperUtils.studentMapToResponse(student, new StudentResponse()))
				.toList();
	}

	public StudentResponse get(final Integer studentId)
	{
		return studentRepository.findById(studentId)
				.map(student -> MapperUtils.studentMapToResponse(student, new StudentResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final StudentRequest studentRequest)
	{
		final Student student = new Student();
		studentRequest.setPassword(new BCryptPasswordEncoder().encode(studentRequest.getPassword()));
		User user = MapperUtils.map(studentRequest, User.class);
		user.setRole(Role.STUDENT.toString());
		user.setStatus(1);
		student.setStatus(1);
		user = userRepository.save(user);
		Classes classs = classesRepository.findById(studentRequest.getClassId())
				.orElseThrow(NotFoundException::new);
		MapperUtils.studentMapToEntity(studentRequest, student, user, classs);
		return studentRepository.save(student).getStudentId();
	}

	public void update(final Integer studentId, final StudentRequest studentRequest)
	{
		final Student student = studentRepository.findById(studentId)
				.orElseThrow(NotFoundException::new);
		User user = MapperUtils.map(studentRequest, User.class);
		user = userRepository.save(user);
		Classes classs = classesRepository.findById(studentRequest.getClassId())
				.orElseThrow(NotFoundException::new);
		MapperUtils.studentMapToEntity(studentRequest, student, user, classs);
		studentRepository.save(student);
	}

	public void delete(final Integer studentId)
	{
		final Student student = studentRepository.findById(studentId)
				.orElseThrow(NotFoundException::new);
		studentRepository.deleteById(studentId);
		userRepository.delete(student.getUser());
	}

	public ReferencedWarning getReferencedWarning(final Integer studentId)
	{
		return null;
	}

}
