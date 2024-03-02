package com.emh.service;

import com.emh.entity.Admin;
import com.emh.entity.User;
import com.emh.model.Role;
import com.emh.payload.request.AdminRequest;
import com.emh.payload.response.AdminResponse;
import com.emh.repos.AdminRepository;
import com.emh.repos.UserRepository;
import com.emh.specifications.FilterOperation;
import com.emh.specifications.SearchCriteria;
import com.emh.specifications.SpecificationsBuilder;
import com.emh.util.AppException;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import org.apache.commons.lang3.StringUtils;
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
		if (userRepository.findOneByUsername(adminDTO.getUsername()) != null)
			throw new AppException("Account Already Exists");
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

	public List<AdminResponse> searchAdmin(String username, String email, String name) throws Exception
	{
		SpecificationsBuilder<Admin> spec = new SpecificationsBuilder<>();
		if (StringUtils.isNotBlank(username))
			spec.with(new SearchCriteria("username", FilterOperation.EQUAL.toString(), username, false));
		if (StringUtils.isNotBlank(email))
			spec.with(new SearchCriteria("email", FilterOperation.EQUAL.toString(), email, false));
		if (StringUtils.isNotBlank(name))
			spec.with(new SearchCriteria("name", FilterOperation.EQUAL.toString(), name, false));
		final List<Admin> admins = adminRepository.findAll(spec.build());
		return admins.stream()
				.map(admin -> MapperUtils.adminMapToResponse(admin, new AdminResponse()))
				.toList();
	}
}
