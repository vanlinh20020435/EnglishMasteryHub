package com.emh.service;

import com.emh.entity.Admin;
import com.emh.entity.Student;
import com.emh.entity.Teacher;
import com.emh.entity.User;
import com.emh.model.UserDTO;
import com.emh.repos.AdminRepository;
import com.emh.repos.StudentRepository;
import com.emh.repos.TeacherRepository;
import com.emh.repos.UserRepository;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public UserService(final UserRepository userRepository, final AdminRepository adminRepository,
            final TeacherRepository teacherRepository, final StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserId();
    }

    public void update(final Integer userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer userId) {
        userRepository.deleteById(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setName(user.getName());
        userDTO.setStatus(user.getStatus());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setStatus(userDTO.getStatus());
        user.setRole(userDTO.getRole());
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Integer userId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        final Admin userAdmin = adminRepository.findFirstByUser(user);
        if (userAdmin != null) {
            referencedWarning.setKey("user.admin.user.referenced");
            referencedWarning.addParam(userAdmin.getAdminId());
            return referencedWarning;
        }
        final Teacher userTeacher = teacherRepository.findFirstByUser(user);
        if (userTeacher != null) {
            referencedWarning.setKey("user.teacher.user.referenced");
            referencedWarning.addParam(userTeacher.getTeacherId());
            return referencedWarning;
        }
        final Student userStudent = studentRepository.findFirstByUser(user);
        if (userStudent != null) {
            referencedWarning.setKey("user.student.user.referenced");
            referencedWarning.addParam(userStudent.getStudentId());
            return referencedWarning;
        }
        return null;
    }

}
