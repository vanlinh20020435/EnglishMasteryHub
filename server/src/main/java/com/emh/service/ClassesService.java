package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.Student;
import com.emh.entity.StudentNotifications;
import com.emh.entity.Teacher;
import com.emh.payload.request.ClassesRequest;
import com.emh.payload.response.ClassesResponse;
import com.emh.payload.response.StudentResponse;
import com.emh.repos.ClassesRepository;
import com.emh.repos.StudentNotificationsRepository;
import com.emh.repos.StudentRepository;
import com.emh.repos.TeacherRepository;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClassesService
{

	private final ClassesRepository classesRepository;
	private final TeacherRepository teacherRepository;
	private final StudentRepository studentRepository;
	private final StudentNotificationsRepository studentNotificationsRepository;

	public ClassesService(final ClassesRepository classesRepository,
						  final TeacherRepository teacherRepository, final StudentRepository studentRepository,
						  final StudentNotificationsRepository studentNotificationsRepository)
	{
		this.classesRepository = classesRepository;
		this.teacherRepository = teacherRepository;
		this.studentRepository = studentRepository;
		this.studentNotificationsRepository = studentNotificationsRepository;
	}

	public List<ClassesResponse> findAll()
	{
		final List<Classes> classeses = classesRepository.findAll(Sort.by("classId"));
		return classeses.stream()
				.map(classes -> MapperUtils.classMapToResponse(classes, new ClassesResponse()))
				.toList();
	}

	public ClassesResponse get(final Integer classId)
	{
		return classesRepository.findById(classId)
				.map(classes -> MapperUtils.classMapToResponse(classes, new ClassesResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final ClassesRequest classRequest)
	{
		final Classes classes = new Classes();
		Teacher teacher = teacherRepository.findById(classRequest.getTeacherId())
				.orElseThrow(() -> new NotFoundException("teacher not found"));
		MapperUtils.classMapToEntity(classRequest, classes, teacher);
		return classesRepository.save(classes).getClassId();
	}

	public void update(final Integer classId, final ClassesRequest classRequest)
	{
		final Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		Teacher teacher = teacherRepository.findById(classRequest.getTeacherId())
				.orElseThrow(() -> new NotFoundException("teacher not found"));
		MapperUtils.classMapToEntity(classRequest, classes, teacher);
		classesRepository.save(classes);
	}

	public void delete(final Integer classId)
	{
		classesRepository.deleteById(classId);
	}

	public ReferencedWarning getReferencedWarning(final Integer classId)
	{
		final ReferencedWarning referencedWarning = new ReferencedWarning();
		final Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		final Student classsStudent = studentRepository.findFirstByClasss(classes);
		if (classsStudent != null)
		{
			referencedWarning.setKey("classes.student.classs.referenced");
			referencedWarning.addParam(classsStudent.getStudentId());
			return referencedWarning;
		}
		final StudentNotifications classsStudentNotifications = studentNotificationsRepository.findFirstByClasss(classes);
		if (classsStudentNotifications != null)
		{
			referencedWarning.setKey("classes.studentNotifications.classs.referenced");
			referencedWarning.addParam(classsStudentNotifications.getId());
			return referencedWarning;
		}
		return null;
	}

	public List<StudentResponse> getAllStudents(Integer classId)
	{
		Classes classes = classesRepository.findById(classId).orElseThrow(NotFoundException::new);
		return classes.getClassStudents().stream()
				.map(student -> MapperUtils.studentMapToResponse(student, new StudentResponse()))
				.toList();
	}
}
