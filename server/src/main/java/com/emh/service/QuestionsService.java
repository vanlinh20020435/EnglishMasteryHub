package com.emh.service;

import com.emh.entity.*;
import com.emh.model.QuestionsDTO;
import com.emh.repos.*;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestionsService {

    private final QuestionsRepository questionsRepository;
    private final QuestTypeRepository questTypeRepository;
    private final QuestOfTestRepository questOfTestRepository;
    private final StudentTestDetailRepository studentTestDetailRepository;
    private final QuestAnswerRepository questAnswerRepository;

    public QuestionsService(final QuestionsRepository questionsRepository,
            final QuestTypeRepository questTypeRepository,
            final QuestOfTestRepository questOfTestRepository,
            final StudentTestDetailRepository studentTestDetailRepository,
            final QuestAnswerRepository questAnswerRepository) {
        this.questionsRepository = questionsRepository;
        this.questTypeRepository = questTypeRepository;
        this.questOfTestRepository = questOfTestRepository;
        this.studentTestDetailRepository = studentTestDetailRepository;
        this.questAnswerRepository = questAnswerRepository;
    }

    public List<QuestionsDTO> findAll() {
        final List<Questions> questionses = questionsRepository.findAll(Sort.by("questionId"));
        return questionses.stream()
                .map(questions -> mapToDTO(questions, new QuestionsDTO()))
                .toList();
    }

    public QuestionsDTO get(final Integer questionId) {
        return questionsRepository.findById(questionId)
                .map(questions -> mapToDTO(questions, new QuestionsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final QuestionsDTO questionsDTO) {
        final Questions questions = new Questions();
        mapToEntity(questionsDTO, questions);
        return questionsRepository.save(questions).getQuestionId();
    }

    public void update(final Integer questionId, final QuestionsDTO questionsDTO) {
        final Questions questions = questionsRepository.findById(questionId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(questionsDTO, questions);
        questionsRepository.save(questions);
    }

    public void delete(final Integer questionId) {
        questionsRepository.deleteById(questionId);
    }

    private QuestionsDTO mapToDTO(final Questions questions, final QuestionsDTO questionsDTO) {
        questionsDTO.setQuestionId(questions.getQuestionId());
        questionsDTO.setUnit(questions.getUnit());
        questionsDTO.setLevelId(questions.getLevelId());
        questionsDTO.setQuestionContent(questions.getQuestionContent());
        questionsDTO.setCorrectAnswer(questions.getCorrectAnswer());
        questionsDTO.setSentBy(questions.getSentBy());
        questionsDTO.setStatus(questions.getStatus());
        questionsDTO.setType(questions.getType() == null ? null : questions.getType().getTypeId());
        return questionsDTO;
    }

    private Questions mapToEntity(final QuestionsDTO questionsDTO, final Questions questions) {
        questions.setUnit(questionsDTO.getUnit());
        questions.setLevelId(questionsDTO.getLevelId());
        questions.setQuestionContent(questionsDTO.getQuestionContent());
        questions.setCorrectAnswer(questionsDTO.getCorrectAnswer());
        questions.setSentBy(questionsDTO.getSentBy());
        questions.setStatus(questionsDTO.getStatus());
        final QuestType type = questionsDTO.getType() == null ? null : questTypeRepository.findById(questionsDTO.getType())
                .orElseThrow(() -> new NotFoundException("type not found"));
        questions.setType(type);
        return questions;
    }

    public ReferencedWarning getReferencedWarning(final Integer questionId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Questions questions = questionsRepository.findById(questionId)
                .orElseThrow(NotFoundException::new);
        final QuestOfTest questionQuestOfTest = questOfTestRepository.findFirstByQuestion(questions);
        if (questionQuestOfTest != null) {
            referencedWarning.setKey("questions.questOfTest.question.referenced");
            referencedWarning.addParam(questionQuestOfTest.getId());
            return referencedWarning;
        }
        final StudentTestDetail questionStudentTestDetail = studentTestDetailRepository.findFirstByQuestion(questions);
        if (questionStudentTestDetail != null) {
            referencedWarning.setKey("questions.studentTestDetail.question.referenced");
            referencedWarning.addParam(questionStudentTestDetail.getId());
            return referencedWarning;
        }
        final QuestAnswer questionQuestAnswer = questAnswerRepository.findFirstByQuestion(questions);
        if (questionQuestAnswer != null) {
            referencedWarning.setKey("questions.questAnswer.question.referenced");
            referencedWarning.addParam(questionQuestAnswer.getId());
            return referencedWarning;
        }
        return null;
    }

}
