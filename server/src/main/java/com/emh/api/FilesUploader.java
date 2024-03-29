package com.emh.api;

import com.emh.payload.response.FileInfo;
import com.emh.payload.response.MessageResponse;
import com.emh.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
public class FilesUploader
{

	@Autowired
	FilesStorageService storageService;

	@PostMapping("/upload")
	@Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"})
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file)
	{
		try
		{
			FileInfo fileInfo = storageService.save(file);
			return ResponseEntity.status(HttpStatus.OK).body(fileInfo);
		}
		catch (Exception e)
		{
			String message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}
}