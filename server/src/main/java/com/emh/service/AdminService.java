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
import com.emh.util.EntityMapper;
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
				.map(admin -> EntityMapper.adminMapToResponse(admin, new AdminResponse()))
				.toList();
	}

	public AdminResponse get(final Integer adminId)
	{
		return adminRepository.findById(adminId)
				.map(admin -> EntityMapper.adminMapToResponse(admin, new AdminResponse()))
				.orElseThrow(NotFoundException::new);
	}

	public Integer create(final AdminRequest adminRequest)
	{
		if (userRepository.findOneByUsername(adminRequest.getUsername()) != null)
			throw new AppException("Account Already Exists");
		final Admin admin = new Admin();
		adminRequest.setPassword(new BCryptPasswordEncoder().encode(adminRequest.getPassword()));
		User user = MapperUtils.map(adminRequest, User.class);
		user.setRole(Role.ADMIN.toString());
		user.setStatus(1);
		admin.setStatus(1);
		user = userRepository.save(user);
		EntityMapper.adminMapToEntity(adminRequest, admin, user);
		return adminRepository.save(admin).getAdminId();
	}

	public void update(final Integer adminId, final AdminRequest adminRequest)
	{
		final Admin admin = adminRepository.findById(adminId)
				.orElseThrow(NotFoundException::new);
		adminRequest.setPassword(admin.getPassword());
		User user = MapperUtils.map(adminRequest, User.class);
		user.setUserId(admin.getUser().getUserId());
		user.setRole(Role.ADMIN.toString());
		user.setStatus(admin.getUser().getStatus());
		user = userRepository.save(user);
		EntityMapper.adminMapToEntity(adminRequest, admin, user);
		adminRepository.save(admin);
	}

	public void delete(final Integer adminId)
	{
		final Admin admin = adminRepository.findById(adminId)
				.orElseThrow(NotFoundException::new);
		userRepository.delete(admin.getUser());
		adminRepository.deleteById(adminId);
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
				.map(admin -> EntityMapper.adminMapToResponse(admin, new AdminResponse()))
				.toList();
	}

	public void updateStatus(Integer id, Integer status)
	{
		final Admin admin = adminRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		User user = admin.getUser();
		admin.setStatus(status == 1 ? 1 : 0);
		user.setStatus(status == 1 ? 1 : 0);
		userRepository.save(user);
		adminRepository.save(admin);
	}

	public void updatePassword(Integer id, String password)
	{
		password = new BCryptPasswordEncoder().encode(password);
		final Admin admin = adminRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		User user = admin.getUser();
		admin.setPassword(password);
		user.setPassword(password);
		userRepository.save(user);
		adminRepository.save(admin);
	}
}
