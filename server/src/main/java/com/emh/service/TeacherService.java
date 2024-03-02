package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.Teacher;
import com.emh.entity.User;
import com.emh.model.Role;
import com.emh.payload.request.TeacherRequest;
import com.emh.payload.response.ClassesResponse;
import com.emh.payload.response.TeacherResponse;
import com.emh.repos.ClassesRepository;
import com.emh.repos.TeacherNotificationsRepository;
import com.emh.repos.TeacherRepository;
import com.emh.repos.UserRepository;
import com.emh.specifications.FilterOperation;
import com.emh.specifications.SearchCriteria;
import com.emh.specifications.SpecificationsBuilder;
import com.emh.util.AppException;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TeacherService
{

	private final TeacherRepository teacherRepository;
	private final UserRepository userRepository;
	private final ClassesRepository classesRepository;
	private final TeacherNotificationsRepository teacherNotificationsRepository;

	public TeacherService(final TeacherRepository teacherRepository,
						  final UserRepository userRepository, final ClassesRepository classesRepository,
						  final TeacherNotificationsRepository teacherNotificationsRepository)
	{
		this.teacherRepository = teacherRepository;
		this.userRepository = userRepository;
		this.classesRepository = classesRepository;
		this.teacherNotificationsRepository = teacherNotificationsRepository;
	}

	public List<TeacherResponse> findAll()
	{
		final List<Teacher> teachers = teacherRepository.findAll(Sort.by("teacherId"));
		return teachers.stream()
				.map(teacher -> MapperUtils.teacherMapToResponse(teacher, new TeacherResponse()))
				.toList();
	}

	public TeacherResponse get(final Integer teacherId)
	{
		return teacherRepository.findById(teacherId)
				.map(teacher -> MapperUtils.teacherMapToResponse(teacher, new TeacherResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final TeacherRequest teacherRequest)
	{
		if (userRepository.findOneByUsername(teacherRequest.getUsername()) != null)
			throw new AppException("Account Already Exists");
		final Teacher teacher = new Teacher();
		teacherRequest.setPassword(new BCryptPasswordEncoder().encode(teacherRequest.getPassword()));
		User user = MapperUtils.map(teacherRequest, User.class);
		user.setRole(Role.TEACHER.toString());
		user.setStatus(1);
		teacher.setStatus(1);
		user = userRepository.save(user);
		MapperUtils.teacherMapToEntity(teacherRequest, teacher, user);
		return teacherRepository.save(teacher).getTeacherId();
	}

	public void update(final Integer teacherId, final TeacherRequest teacherRequest)
	{
		final Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(NotFoundException::new);
		User user = MapperUtils.map(teacherRequest, User.class);
		user = userRepository.save(user);
		MapperUtils.teacherMapToEntity(teacherRequest, teacher, user);
		teacherRepository.save(teacher);
	}

	public void delete(final Integer teacherId)
	{
		final Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(NotFoundException::new);
		teacherRepository.deleteById(teacherId);
		userRepository.delete(teacher.getUser());
	}

	public ReferencedWarning getReferencedWarning(final Integer teacherId)
	{
		final ReferencedWarning referencedWarning = new ReferencedWarning();
		final Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(NotFoundException::new);
		final Classes teacherClasses = classesRepository.findFirstByTeacher(teacher);
		if (teacherClasses != null)
		{
			referencedWarning.setKey("teacher.classes.teacher.referenced");
			referencedWarning.addParam(teacherClasses.getClassId());
			return referencedWarning;
		}
		return null;
	}

	public List<ClassesResponse> getAllClasss(Integer teacherId)
	{
		Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(NotFoundException::new);
		return teacher.getClasses().stream()
				.map(classs -> MapperUtils.classMapToResponse(classs, new ClassesResponse()))
				.toList();
	}

	public List<TeacherResponse> searchTeacher(String username, String email, String name) throws Exception
	{
		SpecificationsBuilder<Teacher> spec = new SpecificationsBuilder<>();
		if (StringUtils.isNotBlank(username))
			spec.with(new SearchCriteria("username", FilterOperation.EQUAL.toString(), username, false));
		if (StringUtils.isNotBlank(email))
			spec.with(new SearchCriteria("email", FilterOperation.EQUAL.toString(), email, false));
		if (StringUtils.isNotBlank(name))
			spec.with(new SearchCriteria("name", FilterOperation.EQUAL.toString(), name, false));
		final List<Teacher> teachers = teacherRepository.findAll(spec.build());
		return teachers.stream()
				.map(teacher -> MapperUtils.teacherMapToResponse(teacher, new TeacherResponse()))
				.toList();
	}
}
