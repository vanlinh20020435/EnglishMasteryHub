package com.emh.service;

import com.emh.entity.User;
import com.emh.payload.response.UserResponse;
import com.emh.repos.AdminRepository;
import com.emh.repos.StudentRepository;
import com.emh.repos.TeacherRepository;
import com.emh.repos.UserRepository;
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
