package com.emh.service;

import com.emh.entity.ClassFile;
import com.emh.entity.Classes;
import com.emh.payload.request.ClassFileRequest;
import com.emh.payload.response.ClassFileResponse;
import com.emh.repos.ClassFileRepository;
import com.emh.repos.ClassesRepository;
import com.emh.util.MapperUtils;
import com.emh.util.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;


@Service
public class ClassFileService
{

	private final ClassFileRepository classFileRepository;
	private final ClassesRepository classesRepository;

	public ClassFileService(final ClassFileRepository classFileRepository,
							final ClassesRepository classesRepository)
	{
		this.classFileRepository = classFileRepository;
		this.classesRepository = classesRepository;
	}

	public List<ClassFileResponse> findAllByClass(Integer classId)
	{
		final Classes classs = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		return classFileRepository.findAllByClasss(classs).stream()
				.map(classFile -> MapperUtils.map(classFile, ClassFileResponse.class))
				.toList();
	}

	public Integer create(Integer classId, ClassFileRequest classFileRequest) throws IOException
	{
		ClassFile classFile = MapperUtils.map(classFileRequest, ClassFile.class);
		classFile.setUrl(FilesStorageService.moveFileUploadsToContent(classFileRequest.getUrl()));
		Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		classFile.setClasss(classes);
		return classFileRepository.save(classFile).getId();
	}

	@Transactional
	public void delete(Integer classId, Integer fileId)
	{
		Classes classes = classesRepository.findById(classId)
				.orElseThrow(NotFoundException::new);
		ClassFile classFile = classFileRepository.findOneByClasssAndId(classes, fileId);
		classFileRepository.delete(classFile);
	}

}
