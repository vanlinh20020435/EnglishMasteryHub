package com.emh.service;

import com.emh.entity.Admin;
import com.emh.entity.User;
import com.emh.model.AdminDTO;
import com.emh.repos.AdminRepository;
import com.emh.repos.UserRepository;
import com.emh.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminService(final AdminRepository adminRepository,
            final UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    public List<AdminDTO> findAll() {
        final List<Admin> admins = adminRepository.findAll(Sort.by("adminId"));
        return admins.stream()
                .map(admin -> mapToDTO(admin, new AdminDTO()))
                .toList();
    }

    public AdminDTO get(final Integer adminId) {
        return adminRepository.findById(adminId)
                .map(admin -> mapToDTO(admin, new AdminDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AdminDTO adminDTO) {
        final Admin admin = new Admin();
        mapToEntity(adminDTO, admin);
        return adminRepository.save(admin).getAdminId();
    }

    public void update(final Integer adminId, final AdminDTO adminDTO) {
        final Admin admin = adminRepository.findById(adminId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(adminDTO, admin);
        adminRepository.save(admin);
    }

    public void delete(final Integer adminId) {
        adminRepository.deleteById(adminId);
    }

    private AdminDTO mapToDTO(final Admin admin, final AdminDTO adminDTO) {
        adminDTO.setAdminId(admin.getAdminId());
        adminDTO.setUsername(admin.getUsername());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setPassword(admin.getPassword());
        adminDTO.setName(admin.getName());
        adminDTO.setLastLogin(admin.getLastLogin());
        adminDTO.setGender(admin.getGender());
        adminDTO.setStatus(admin.getStatus());
        adminDTO.setAvatar(admin.getAvatar());
        adminDTO.setBirthday(admin.getBirthday());
        adminDTO.setUser(admin.getUser() == null ? null : admin.getUser().getUserId());
        return adminDTO;
    }

    private Admin mapToEntity(final AdminDTO adminDTO, final Admin admin) {
        admin.setUsername(adminDTO.getUsername());
        admin.setEmail(adminDTO.getEmail());
        admin.setPassword(adminDTO.getPassword());
        admin.setName(adminDTO.getName());
        admin.setLastLogin(adminDTO.getLastLogin());
        admin.setGender(adminDTO.getGender());
        admin.setStatus(adminDTO.getStatus());
        admin.setAvatar(adminDTO.getAvatar());
        admin.setBirthday(adminDTO.getBirthday());
        final User user = adminDTO.getUser() == null ? null : userRepository.findById(adminDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        admin.setUser(user);
        return admin;
    }

}
