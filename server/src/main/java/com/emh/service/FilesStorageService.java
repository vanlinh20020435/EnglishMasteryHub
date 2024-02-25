package com.emh.service;

import com.emh.controller.PropertyController;
import com.emh.payload.response.FileInfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FilesStorageService
{

	private final Path root = Paths.get("uploads");

	public void init()
	{
		try
		{
			Files.createDirectories(root);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	public FileInfo save(MultipartFile file)
	{
		try
		{
			String type = file.getContentType();
			String fileName = file.getOriginalFilename();
			String fileNameRandom = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), FilenameUtils.getExtension(fileName));
			Files.copy(file.getInputStream(), this.root.resolve(fileNameRandom));
			return new FileInfo(type, getFileUri(fileNameRandom), fileName);
		}
		catch (Exception e)
		{
			if (e instanceof FileAlreadyExistsException)
			{
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}
	}

	public void deleteAll()
	{
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	public Stream<Path> loadAll()
	{
		try
		{
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not load the files!");
		}
	}

	public String getFileUri(String fileName)
	{
		return PropertyController.FILE_UPLOAD_URI.replace("$FILE_NAME$", fileName);
	}
}