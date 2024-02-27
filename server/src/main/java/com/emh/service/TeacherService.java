package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.Teacher;
import com.emh.entity.TeacherNotifications;
import com.emh.entity.User;
import com.emh.model.Role;
import com.emh.payload.request.TeacherRequest;
import com.emh.payload.response.TeacherResponse;
import com.emh.repos.ClassesRepository;
import com.emh.repos.TeacherNotificationsRepository;
import com.emh.repos.TeacherRepository;
import com.emh.repos.UserRepository;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
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
		final TeacherNotifications teacherTeacherNotifications = teacherNotificationsRepository.findFirstByTeacher(teacher);
		if (teacherTeacherNotifications != null)
		{
			referencedWarning.setKey("teacher.teacherNotifications.teacher.referenced");
			referencedWarning.addParam(teacherTeacherNotifications.getId());
			return referencedWarning;
		}
		return null;
	}

}
