package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.Teacher;
import com.emh.payload.request.ClassesRequest;
import com.emh.payload.response.ClassesResponse;
import com.emh.payload.response.StudentResponse;
import com.emh.repos.ClassesRepository;
import com.emh.repos.StudentNotificationsRepository;
import com.emh.repos.StudentRepository;
import com.emh.repos.TeacherRepository;
import com.emh.specifications.FilterOperation;
import com.emh.specifications.SearchCriteria;
import com.emh.specifications.SearchForeignCriteria;
import com.emh.specifications.SpecificationsBuilder;
import com.emh.util.EntityMapper;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
				.map(classes -> EntityMapper.classMapToResponse(classes, new ClassesResponse()))
				.toList();
	}

	public ClassesResponse get(final Integer classId)
	{
		return classesRepository.findById(classId)
				.map(classes -> EntityMapper.classMapToResponse(classes, new ClassesResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final ClassesRequest classRequest)
	{
		final Classes classes = new Classes();
		Teacher teacher = teacherRepository.findById(classRequest.getTeacherId())
				.orElseThrow(() -> new NotFoundException("teacher not found"));
		EntityMapper.classMapToEntity(classRequest, classes, teacher);
		return classesRepository.save(classes).getClassId();
	}

	public void update(final Integer classId, final ClassesRequest classRequest)
	{
		final Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		Teacher teacher = teacherRepository.findById(classRequest.getTeacherId())
				.orElseThrow(() -> new NotFoundException("teacher not found"));
		EntityMapper.classMapToEntity(classRequest, classes, teacher);
		classesRepository.save(classes);
	}

	public void delete(final Integer classId)
	{
		classesRepository.deleteById(classId);
	}

	public ReferencedWarning getReferencedWarning(final Integer classId)
	{
		return null;
	}

	public List<StudentResponse> getAllStudents(Integer classId)
	{
		Classes classes = classesRepository.findById(classId).orElseThrow(NotFoundException::new);
		return classes.getClassStudents().stream()
				.map(student -> EntityMapper.studentMapToResponse(student, new StudentResponse()))
				.toList();
	}

	public List<ClassesResponse> searchClass(String className, Integer teacherId) throws Exception
	{
		SpecificationsBuilder<Classes> spec = new SpecificationsBuilder<>();
		if (StringUtils.isNotBlank(className))
			spec.with(new SearchCriteria("className", FilterOperation.EQUAL.toString(), className, false));
		if (ObjectUtils.defaultIfNull(teacherId, 0) != 0)
			spec.with(new SearchForeignCriteria("teacher", "teacherId", FilterOperation.FOREIGN_KEY.toString(), teacherId, false));
		final List<Classes> classes = classesRepository.findAll(spec.build());
		return classes.stream()
				.map(classs -> EntityMapper.classMapToResponse(classs, new ClassesResponse()))
				.toList();
	}
}
