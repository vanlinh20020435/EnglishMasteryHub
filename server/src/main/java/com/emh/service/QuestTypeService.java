package com.emh.service;

import com.emh.entity.QuestType;
import com.emh.entity.Questions;
import com.emh.model.QuestTypeDTO;
import com.emh.repos.QuestTypeRepository;
import com.emh.repos.QuestionsRepository;
import com.emh.util.NotFoundException;
import com.emh.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestTypeService {

    private final QuestTypeRepository questTypeRepository;
    private final QuestionsRepository questionsRepository;

    public QuestTypeService(final QuestTypeRepository questTypeRepository,
            final QuestionsRepository questionsRepository) {
        this.questTypeRepository = questTypeRepository;
        this.questionsRepository = questionsRepository;
    }

    public List<QuestTypeDTO> findAll() {
        final List<QuestType> questTypes = questTypeRepository.findAll(Sort.by("typeId"));
        return questTypes.stream()
                .map(questType -> mapToDTO(questType, new QuestTypeDTO()))
                .toList();
    }

    public QuestTypeDTO get(final Integer typeId) {
        return questTypeRepository.findById(typeId)
                .map(questType -> mapToDTO(questType, new QuestTypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final QuestTypeDTO questTypeDTO) {
        final QuestType questType = new QuestType();
        mapToEntity(questTypeDTO, questType);
        return questTypeRepository.save(questType).getTypeId();
    }

    public void update(final Integer typeId, final QuestTypeDTO questTypeDTO) {
        final QuestType questType = questTypeRepository.findById(typeId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(questTypeDTO, questType);
        questTypeRepository.save(questType);
    }

    public void delete(final Integer typeId) {
        questTypeRepository.deleteById(typeId);
    }

    private QuestTypeDTO mapToDTO(final QuestType questType, final QuestTypeDTO questTypeDTO) {
        questTypeDTO.setTypeId(questType.getTypeId());
        questTypeDTO.setCode(questType.getCode());
        questTypeDTO.setName(questType.getName());
        return questTypeDTO;
    }

    private QuestType mapToEntity(final QuestTypeDTO questTypeDTO, final QuestType questType) {
        questType.setCode(questTypeDTO.getCode());
        questType.setName(questTypeDTO.getName());
        return questType;
    }

    public ReferencedWarning getReferencedWarning(final Integer typeId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final QuestType questType = questTypeRepository.findById(typeId)
                .orElseThrow(NotFoundException::new);
        final Questions typeQuestions = questionsRepository.findFirstByType(questType);
        if (typeQuestions != null) {
            referencedWarning.setKey("questType.questions.type.referenced");
            referencedWarning.addParam(typeQuestions.getQuestionId());
            return referencedWarning;
        }
        return null;
    }

}
