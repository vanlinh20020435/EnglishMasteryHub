package com.emh.service;

import com.emh.entity.Admin;
import com.emh.entity.User;
import com.emh.model.Role;
import com.emh.payload.request.AdminRequest;
import com.emh.payload.response.AdminResponse;
import com.emh.repos.AdminRepository;
import com.emh.repos.UserRepository;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService
{

	private final AdminRepository adminRepository;
	private final UserRepository userRepository;

	public AdminService(final AdminRepository adminRepository,
						final UserRepository userRepository)
	{
		this.adminRepository = adminRepository;
		this.userRepository = userRepository;
	}

	public List<AdminResponse> findAll()
	{
		final List<Admin> admins = adminRepository.findAll(Sort.by("adminId"));
		return admins.stream()
				.map(admin -> MapperUtils.adminMapToResponse(admin, new AdminResponse()))
				.toList();
	}

	public AdminResponse get(final Integer adminId)
	{
		return adminRepository.findById(adminId)
				.map(admin -> MapperUtils.adminMapToResponse(admin, new AdminResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final AdminRequest adminDTO)
	{
		final Admin admin = new Admin();
		adminDTO.setPassword(new BCryptPasswordEncoder().encode(adminDTO.getPassword()));
		User user = MapperUtils.map(adminDTO, User.class);
		user.setRole(Role.ADMIN.toString());
		user.setStatus(1);
		admin.setStatus(1);
		user = userRepository.save(user);
		MapperUtils.adminMapToEntity(adminDTO, admin, user);
		return adminRepository.save(admin).getAdminId();
	}

	public void update(final Integer adminId, final AdminRequest adminDTO)
	{
		final Admin admin = adminRepository.findById(adminId)
				.orElseThrow(NotFoundException::new);
		User user = MapperUtils.map(adminDTO, User.class);
		user = userRepository.save(user);
		MapperUtils.adminMapToEntity(adminDTO, admin, user);
		adminRepository.save(admin);
	}

	public void delete(final Integer adminId)
	{
		final Admin admin = adminRepository.findById(adminId)
				.orElseThrow(NotFoundException::new);
		adminRepository.deleteById(adminId);
		userRepository.delete(admin.getUser());
	}
}
