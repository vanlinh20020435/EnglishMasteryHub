package com.emh.service;

import com.emh.entity.QuestOfTest;
import com.emh.entity.Scores;
import com.emh.entity.StudentTestDetail;
import com.emh.entity.Tests;
import com.emh.model.TestsDTO;
import com.emh.repos.QuestOfTestRepository;
import com.emh.repos.ScoresRepository;
import com.emh.repos.StudentTestDetailRepository;
import com.emh.repos.TestsRepository;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TestsService {

    private final TestsRepository testsRepository;
    private final QuestOfTestRepository questOfTestRepository;
    private final ScoresRepository scoresRepository;
    private final StudentTestDetailRepository studentTestDetailRepository;

    public TestsService(final TestsRepository testsRepository,
            final QuestOfTestRepository questOfTestRepository,
            final ScoresRepository scoresRepository,
            final StudentTestDetailRepository studentTestDetailRepository) {
        this.testsRepository = testsRepository;
        this.questOfTestRepository = questOfTestRepository;
        this.scoresRepository = scoresRepository;
        this.studentTestDetailRepository = studentTestDetailRepository;
    }

    public List<TestsDTO> findAll() {
        final List<Tests> testses = testsRepository.findAll(Sort.by("testCode"));
        return testses.stream()
                .map(tests -> mapToDTO(tests, new TestsDTO()))
                .toList();
    }

    public TestsDTO get(final Integer testCode) {
        return testsRepository.findById(testCode)
                .map(tests -> mapToDTO(tests, new TestsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TestsDTO testsDTO) {
        final Tests tests = new Tests();
        mapToEntity(testsDTO, tests);
        return testsRepository.save(tests).getTestCode();
    }

    public void update(final Integer testCode, final TestsDTO testsDTO) {
        final Tests tests = testsRepository.findById(testCode)
                .orElseThrow(NotFoundException::new);
        mapToEntity(testsDTO, tests);
        testsRepository.save(tests);
    }

    public void delete(final Integer testCode) {
        testsRepository.deleteById(testCode);
    }

    private TestsDTO mapToDTO(final Tests tests, final TestsDTO testsDTO) {
        testsDTO.setTestCode(tests.getTestCode());
        testsDTO.setTestName(tests.getTestName());
        testsDTO.setPassword(tests.getPassword());
        testsDTO.setSubjectId(tests.getSubjectId());
        testsDTO.setGradeId(tests.getGradeId());
        testsDTO.setTotalQuestions(tests.getTotalQuestions());
        testsDTO.setTimeToDo(tests.getTimeToDo());
        testsDTO.setNote(tests.getNote());
        testsDTO.setStatus(tests.getStatus());
        testsDTO.setTimest(tests.getTimest());
        return testsDTO;
    }

    private Tests mapToEntity(final TestsDTO testsDTO, final Tests tests) {
        tests.setTestName(testsDTO.getTestName());
        tests.setPassword(testsDTO.getPassword());
        tests.setSubjectId(testsDTO.getSubjectId());
        tests.setGradeId(testsDTO.getGradeId());
        tests.setTotalQuestions(testsDTO.getTotalQuestions());
        tests.setTimeToDo(testsDTO.getTimeToDo());
        tests.setNote(testsDTO.getNote());
        tests.setStatus(testsDTO.getStatus());
        tests.setTimest(testsDTO.getTimest());
        return tests;
    }

    public ReferencedWarning getReferencedWarning(final Integer testCode) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Tests tests = testsRepository.findById(testCode)
                .orElseThrow(NotFoundException::new);
        final QuestOfTest testCodeQuestOfTest = questOfTestRepository.findFirstByTestCode(tests);
        if (testCodeQuestOfTest != null) {
            referencedWarning.setKey("tests.questOfTest.testCode.referenced");
            referencedWarning.addParam(testCodeQuestOfTest.getId());
            return referencedWarning;
        }
        final Scores testCodeScores = scoresRepository.findFirstByTestCode(tests);
        if (testCodeScores != null) {
            referencedWarning.setKey("tests.scores.testCode.referenced");
            referencedWarning.addParam(testCodeScores.getScoreNumber());
            return referencedWarning;
        }
        final StudentTestDetail testCodeStudentTestDetail = studentTestDetailRepository.findFirstByTestCode(tests);
        if (testCodeStudentTestDetail != null) {
            referencedWarning.setKey("tests.studentTestDetail.testCode.referenced");
            referencedWarning.addParam(testCodeStudentTestDetail.getId());
            return referencedWarning;
        }
        return null;
    }

}
