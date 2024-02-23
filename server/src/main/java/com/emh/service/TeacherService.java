package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.Teacher;
import com.emh.entity.TeacherNotifications;
import com.emh.entity.User;
import com.emh.model.Role;
import com.emh.model.TeacherDTO;
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
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final ClassesRepository classesRepository;
    private final TeacherNotificationsRepository teacherNotificationsRepository;

    public TeacherService(final TeacherRepository teacherRepository,
            final UserRepository userRepository, final ClassesRepository classesRepository,
            final TeacherNotificationsRepository teacherNotificationsRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.classesRepository = classesRepository;
        this.teacherNotificationsRepository = teacherNotificationsRepository;
    }

    public List<TeacherDTO> findAll() {
        final List<Teacher> teachers = teacherRepository.findAll(Sort.by("teacherId"));
        return teachers.stream()
                .map(teacher -> mapToDTO(teacher, new TeacherDTO()))
                .toList();
    }

    public TeacherDTO get(final Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .map(teacher -> mapToDTO(teacher, new TeacherDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TeacherDTO teacherDTO) {
        final Teacher teacher = new Teacher();
        teacherDTO.setPassword(new BCryptPasswordEncoder().encode(teacherDTO.getPassword()));
        User user = MapperUtils.map(teacherDTO, User.class);
        user.setRole(Role.TEACHER.toString());
        user = userRepository.save(user);
        mapToEntity(teacherDTO, teacher, user);
        return teacherRepository.save(teacher).getTeacherId();
    }

    public void update(final Integer teacherId, final TeacherDTO teacherDTO) {
        final Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(NotFoundException::new);
        User user = MapperUtils.map(teacherDTO, User.class);
        user = userRepository.save(user);
        mapToEntity(teacherDTO, teacher, user);
        teacherRepository.save(teacher);
    }

    public void delete(final Integer teacherId) {
        final Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(NotFoundException::new);
        teacherRepository.deleteById(teacherId);
        userRepository.delete(teacher.getUser());
    }

    private TeacherDTO mapToDTO(final Teacher teacher, final TeacherDTO teacherDTO) {
        teacherDTO.setTeacherId(teacher.getTeacherId());
        teacherDTO.setUsername(teacher.getUsername());
        teacherDTO.setEmail(teacher.getEmail());
        teacherDTO.setPassword(teacher.getPassword());
        teacherDTO.setName(teacher.getName());
        teacherDTO.setLastLogin(teacher.getLastLogin());
        teacherDTO.setGender(teacher.getGender());
        teacherDTO.setStatus(teacher.getStatus());
        teacherDTO.setAvatar(teacher.getAvatar());
        teacherDTO.setBirthday(teacher.getBirthday());
        teacherDTO.setUser(teacher.getUser() == null ? null : teacher.getUser().getUserId());
        return teacherDTO;
    }

    private Teacher mapToEntity(final TeacherDTO teacherDTO, final Teacher teacher) {
        teacher.setUsername(teacherDTO.getUsername());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPassword(teacherDTO.getPassword());
        teacher.setName(teacherDTO.getName());
        teacher.setLastLogin(teacherDTO.getLastLogin());
        teacher.setGender(teacherDTO.getGender());
        teacher.setStatus(teacherDTO.getStatus());
        teacher.setAvatar(teacherDTO.getAvatar());
        teacher.setBirthday(teacherDTO.getBirthday());
        final User user = teacherDTO.getUser() == null ? null : userRepository.findById(teacherDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        teacher.setUser(user);
        return teacher;
    }

    private Teacher mapToEntity(final TeacherDTO teacherDTO, final Teacher teacher, final User user) {
        teacher.setUsername(teacherDTO.getUsername());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPassword(teacherDTO.getPassword());
        teacher.setName(teacherDTO.getName());
        teacher.setLastLogin(teacherDTO.getLastLogin());
        teacher.setGender(teacherDTO.getGender());
        teacher.setStatus(teacherDTO.getStatus());
        teacher.setAvatar(teacherDTO.getAvatar());
        teacher.setBirthday(teacherDTO.getBirthday());
        teacher.setUser(user);
        return teacher;
    }

    public ReferencedWarning getReferencedWarning(final Integer teacherId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(NotFoundException::new);
        final Classes teacherClasses = classesRepository.findFirstByTeacher(teacher);
        if (teacherClasses != null) {
            referencedWarning.setKey("teacher.classes.teacher.referenced");
            referencedWarning.addParam(teacherClasses.getClassId());
            return referencedWarning;
        }
        final TeacherNotifications teacherTeacherNotifications = teacherNotificationsRepository.findFirstByTeacher(teacher);
        if (teacherTeacherNotifications != null) {
            referencedWarning.setKey("teacher.teacherNotifications.teacher.referenced");
            referencedWarning.addParam(teacherTeacherNotifications.getId());
            return referencedWarning;
        }
        return null;
    }

}
