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
import com.emh.util.EntityMapper;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

	@Transactional
	public void update(final Integer userId, final UserRequest userRequest)
	{
		User user = userRepository.findById(userId)
				.orElseThrow(NotFoundException::new);
		user.setUsername(userRequest.getUsername());
		user.setEmail(userRequest.getEmail());
		user.setName(userRequest.getName());
		user.setGender(userRequest.getGender());
		user.setAvatar(userRequest.getAvatar());
		user.setBirthday(userRequest.getBirthday());
		if (StringUtils.isNotBlank(userRequest.getPassword()))
			userRequest.setPassword(new BCryptPasswordEncoder().encode(userRequest.getPassword()));
		else
			userRequest.setPassword(user.getPassword());
		user = userRepository.save(user);
		switch (Role.valueOf(user.getRole()))
		{
			case ADMIN ->
			{
				Admin admin = adminRepository.findFirstByUser(user);
				AdminRequest adminRequest = MapperUtils.map(userRequest, AdminRequest.class);
				EntityMapper.adminMapToEntity(adminRequest, admin, user);
				adminRepository.save(admin);
			}
			case TEACHER ->
			{
				Teacher teacher = teacherRepository.findFirstByUser(user);
				TeacherRequest teacherRequest = MapperUtils.map(userRequest, TeacherRequest.class);
				EntityMapper.teacherMapToEntity(teacherRequest, teacher, user);
				teacherRepository.save(teacher);
			}
			case STUDENT ->
			{
				Student student = studentRepository.findFirstByUser(user);
				StudentRequest studentRequest = MapperUtils.map(userRequest, StudentRequest.class);
				EntityMapper.studentMapToEntity(studentRequest, student, user, student.getClasss());
				studentRepository.save(student);
			}
		}
	}

	@Transactional
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
		userResponse.setName(user.getName());
		userResponse.setGender(user.getGender());
		userResponse.setStatus(user.getStatus());
		userResponse.setRole(user.getRole());
		userResponse.setAvatar(user.getAvatar());
		userResponse.setBirthday(user.getBirthday());
		switch (Role.valueOf(user.getRole()))
		{
			case ADMIN -> userResponse.setAdminId(adminRepository.findFirstByUser(user).getAdminId());
			case TEACHER -> userResponse.setTeacherId(teacherRepository.findFirstByUser(user).getTeacherId());
			case STUDENT -> userResponse.setStudentId(studentRepository.findFirstByUser(user).getStudentId());
		}
		return userResponse;
	}

	public void updatePassword(Integer id, String password)
	{
		password = new BCryptPasswordEncoder().encode(password);
		User user = userRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		user.setPassword(password);
		userRepository.save(user);
		switch (Role.valueOf(user.getRole()))
		{
			case ADMIN ->
			{
				Admin admin = adminRepository.findFirstByUser(user);
				admin.setPassword(password);
				adminRepository.save(admin);
			}
			case TEACHER ->
			{
				Teacher teacher = teacherRepository.findFirstByUser(user);
				teacher.setPassword(password);
				teacherRepository.save(teacher);
			}
			case STUDENT ->
			{
				Student student = studentRepository.findFirstByUser(user);
				student.setPassword(password);
				studentRepository.save(student);
			}
		}
	}
}
