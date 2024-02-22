package com.emh.service;

import com.emh.entity.Classes;
import com.emh.entity.Student;
import com.emh.entity.StudentNotifications;
import com.emh.entity.Teacher;
import com.emh.model.ClassesDTO;
import com.emh.repos.ClassesRepository;
import com.emh.repos.StudentNotificationsRepository;
import com.emh.repos.StudentRepository;
import com.emh.repos.TeacherRepository;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClassesService {

    private final ClassesRepository classesRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final StudentNotificationsRepository studentNotificationsRepository;

    public ClassesService(final ClassesRepository classesRepository,
            final TeacherRepository teacherRepository, final StudentRepository studentRepository,
            final StudentNotificationsRepository studentNotificationsRepository) {
        this.classesRepository = classesRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.studentNotificationsRepository = studentNotificationsRepository;
    }

    public List<ClassesDTO> findAll() {
        final List<Classes> classeses = classesRepository.findAll(Sort.by("classId"));
        return classeses.stream()
                .map(classes -> mapToDTO(classes, new ClassesDTO()))
                .toList();
    }

    public ClassesDTO get(final Integer classId) {
        return classesRepository.findById(classId)
                .map(classes -> mapToDTO(classes, new ClassesDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ClassesDTO classesDTO) {
        final Classes classes = new Classes();
        mapToEntity(classesDTO, classes);
        return classesRepository.save(classes).getClassId();
    }

    public void update(final Integer classId, final ClassesDTO classesDTO) {
        final Classes classes = classesRepository.findById(classId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(classesDTO, classes);
        classesRepository.save(classes);
    }

    public void delete(final Integer classId) {
        classesRepository.deleteById(classId);
    }

    private ClassesDTO mapToDTO(final Classes classes, final ClassesDTO classesDTO) {
        classesDTO.setClassId(classes.getClassId());
        classesDTO.setClassName(classes.getClassName());
        classesDTO.setTeacher(classes.getTeacher() == null ? null : classes.getTeacher().getTeacherId());
        return classesDTO;
    }

    private Classes mapToEntity(final ClassesDTO classesDTO, final Classes classes) {
        classes.setClassName(classesDTO.getClassName());
        final Teacher teacher = classesDTO.getTeacher() == null ? null : teacherRepository.findById(classesDTO.getTeacher())
                .orElseThrow(() -> new NotFoundException("teacher not found"));
        classes.setTeacher(teacher);
        return classes;
    }

    public ReferencedWarning getReferencedWarning(final Integer classId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Classes classes = classesRepository.findById(classId)
                .orElseThrow(NotFoundException::new);
        final Student classsStudent = studentRepository.findFirstByClasss(classes);
        if (classsStudent != null) {
            referencedWarning.setKey("classes.student.classs.referenced");
            referencedWarning.addParam(classsStudent.getStudentId());
            return referencedWarning;
        }
        final StudentNotifications classsStudentNotifications = studentNotificationsRepository.findFirstByClasss(classes);
        if (classsStudentNotifications != null) {
            referencedWarning.setKey("classes.studentNotifications.classs.referenced");
            referencedWarning.addParam(classsStudentNotifications.getId());
            return referencedWarning;
        }
        return null;
    }

}
