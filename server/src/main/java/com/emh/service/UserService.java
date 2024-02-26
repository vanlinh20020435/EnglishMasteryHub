package com.emh.service;

import com.emh.entity.Admin;
import com.emh.entity.Student;
import com.emh.entity.Teacher;
import com.emh.entity.User;
import com.emh.model.Role;
import com.emh.payload.request.AdminRequest;
import com.emh.payload.request.StudentRequest;
import com.emh.payload.request.TeacherRequest;
import com.emh.payload.request.UserRequest;
import com.emh.payload.response.UserResponse;
import com.emh.repos.AdminRepository;
import com.emh.repos.StudentRepository;
import com.emh.repos.TeacherRepository;
import com.emh.repos.UserRepository;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService
{

	private final UserRepository userRepository;
	private final AdminRepository adminRepository;
	private final TeacherRepository teacherRepository;
	private final StudentRepository studentRepository;

	public UserService(final UserRepository userRepository, final AdminRepository adminRepository,
					   final TeacherRepository teacherRepository, final StudentRepository studentRepository)
	{
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
		this.teacherRepository = teacherRepository;
		this.studentRepository = studentRepository;
	}

	public UserResponse get(final Integer userId)
	{
		return userRepository.findById(userId)
				.map(user -> mapToResponse(user, new UserResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public void update(final Integer userId, final UserRequest userRequest)
	{
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		user = userRepository.save(user);
		switch (Role.valueOf(user.getRole()))
		{
			case ADMIN:
				Admin admin = adminRepository.findFirstByUser(user);
				AdminRequest adminRequest = MapperUtils.map(userRequest, AdminRequest.class);
				MapperUtils.adminMapToEntity(adminRequest, admin, user);
				adminRepository.save(admin);
			case TEACHER:
				Teacher teacher = teacherRepository.findFirstByUser(user);
				TeacherRequest teacherRequest = MapperUtils.map(userRequest, TeacherRequest.class);
				MapperUtils.teacherMapToEntity(teacherRequest, teacher, user);
				teacherRepository.save(teacher);
			case STUDENT:
				Student student = studentRepository.findFirstByUser(user);
				StudentRequest studentRequest = MapperUtils.map(userRequest, StudentRequest.class);
				MapperUtils.studentMapToEntity(studentRequest, student, user, student.getClasss());
				studentRepository.save(student);
		}
	}

	public void delete(final Integer studentId)
	{
		final Student student = studentRepository.findById(studentId)
				.orElseThrow(NotFoundException::new);
		studentRepository.deleteById(studentId);
		userRepository.delete(student.getUser());
	}

	private UserResponse mapToResponse(final User user, final UserResponse userResponse)
	{
		userResponse.setUserId(user.getUserId());
		userResponse.setUsername(user.getUsername());
		userResponse.setEmail(user.getEmail());
		userResponse.setPassword(user.getPassword());
		userResponse.setName(user.getName());
		userResponse.setGender(user.getGender());
		userResponse.setStatus(user.getStatus());
		userResponse.setRole(user.getRole());
		return userResponse;
	}
}
