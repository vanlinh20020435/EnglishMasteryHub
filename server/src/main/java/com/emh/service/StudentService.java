package com.emh.service;

import com.emh.entity.*;
import com.emh.model.StudentDTO;
import com.emh.repos.*;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassesRepository classesRepository;
    private final UserRepository userRepository;
    private final ScoresRepository scoresRepository;
    private final StudentTestDetailRepository studentTestDetailRepository;

    public StudentService(final StudentRepository studentRepository,
            final ClassesRepository classesRepository, final UserRepository userRepository,
            final ScoresRepository scoresRepository,
            final StudentTestDetailRepository studentTestDetailRepository) {
        this.studentRepository = studentRepository;
        this.classesRepository = classesRepository;
        this.userRepository = userRepository;
        this.scoresRepository = scoresRepository;
        this.studentTestDetailRepository = studentTestDetailRepository;
    }

    public List<StudentDTO> findAll() {
        final List<Student> students = studentRepository.findAll(Sort.by("studentId"));
        return students.stream()
                .map(student -> mapToDTO(student, new StudentDTO()))
                .toList();
    }

    public StudentDTO get(final Integer studentId) {
        return studentRepository.findById(studentId)
                .map(student -> mapToDTO(student, new StudentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudentDTO studentDTO) {
        final Student student = new Student();
        mapToEntity(studentDTO, student);
        return studentRepository.save(student).getStudentId();
    }

    public void update(final Integer studentId, final StudentDTO studentDTO) {
        final Student student = studentRepository.findById(studentId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studentDTO, student);
        studentRepository.save(student);
    }

    public void delete(final Integer studentId) {
        studentRepository.deleteById(studentId);
    }

    private StudentDTO mapToDTO(final Student student, final StudentDTO studentDTO) {
        studentDTO.setStudentId(student.getStudentId());
        studentDTO.setUsername(student.getUsername());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setPassword(student.getPassword());
        studentDTO.setName(student.getName());
        studentDTO.setLastLogin(student.getLastLogin());
        studentDTO.setGender(student.getGender());
        studentDTO.setStatus(student.getStatus());
        studentDTO.setAvatar(student.getAvatar());
        studentDTO.setBirthday(student.getBirthday());
        studentDTO.setDoingExam(student.getDoingExam());
        studentDTO.setStartingTime(student.getStartingTime());
        studentDTO.setTimeRemaining(student.getTimeRemaining());
        studentDTO.setClasss(student.getClasss() == null ? null : student.getClasss().getClassId());
        studentDTO.setUser(student.getUser() == null ? null : student.getUser().getUserId());
        return studentDTO;
    }

    private Student mapToEntity(final StudentDTO studentDTO, final Student student) {
        student.setUsername(studentDTO.getUsername());
        student.setEmail(studentDTO.getEmail());
        student.setPassword(studentDTO.getPassword());
        student.setName(studentDTO.getName());
        student.setLastLogin(studentDTO.getLastLogin());
        student.setGender(studentDTO.getGender());
        student.setStatus(studentDTO.getStatus());
        student.setAvatar(studentDTO.getAvatar());
        student.setBirthday(studentDTO.getBirthday());
        student.setDoingExam(studentDTO.getDoingExam());
        student.setStartingTime(studentDTO.getStartingTime());
        student.setTimeRemaining(studentDTO.getTimeRemaining());
        final Classes classs = studentDTO.getClasss() == null ? null : classesRepository.findById(studentDTO.getClasss())
                .orElseThrow(() -> new NotFoundException("classs not found"));
        student.setClasss(classs);
        final User user = studentDTO.getUser() == null ? null : userRepository.findById(studentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        student.setUser(user);
        return student;
    }

    public ReferencedWarning getReferencedWarning(final Integer studentId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Student student = studentRepository.findById(studentId)
                .orElseThrow(NotFoundException::new);
        final Scores studentScores = scoresRepository.findFirstByStudent(student);
        if (studentScores != null) {
            referencedWarning.setKey("student.scores.student.referenced");
            referencedWarning.addParam(studentScores.getScoreNumber());
            return referencedWarning;
        }
        final StudentTestDetail studentStudentTestDetail = studentTestDetailRepository.findFirstByStudent(student);
        if (studentStudentTestDetail != null) {
            referencedWarning.setKey("student.studentTestDetail.student.referenced");
            referencedWarning.addParam(studentStudentTestDetail.getId());
            return referencedWarning;
        }
        return null;
    }

}
