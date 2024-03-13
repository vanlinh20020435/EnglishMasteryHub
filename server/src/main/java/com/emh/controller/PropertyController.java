package com.emh.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController
{
	public static String JWT_SECRET;
	public static Long JWT_EXPIRATION_MS;
	public static Long JWT_REFRESH_EXPIRATION_MS;
	public static String RESOURCE_HANDLER;
	public static String RESOURCE_PATH;
	public static String FILE_HANDLER;
	public static String FILE_PATH;
	public static String FILE_UPLOAD;
	public static String FILE_UPLOAD_URI;
	public static String FILE_STORAGE;
	public static String FILE_STORAGE_URI;

	@Value("${app.jwt.jwtSecret}")
	public void setJwtSecret(String jwtSecret)
	{
		PropertyController.JWT_SECRET = jwtSecret;
	}

	@Value("${app.jwt.jwtExpirationMs}")
	public void setJwtExpirationMs(String jwtExpirationMs)
	{
		PropertyController.JWT_EXPIRATION_MS = Long.parseLong(jwtExpirationMs);
	}

	@Value("${app.jwt.jwtRefreshExpirationMs}")
	public void setJwtRefreshExpirationMs(String jwtRefreshExpirationMs)
	{
		PropertyController.JWT_REFRESH_EXPIRATION_MS = Long.parseLong(jwtRefreshExpirationMs);
	}

	@Value("${app.resource.handler}")
	public void setResourceHandler(String resourceHandler)
	{
		PropertyController.RESOURCE_HANDLER = resourceHandler;
	}

	@Value("${app.resource.path}")
	public void setResourcePath(String resourcePath)
	{
		PropertyController.RESOURCE_PATH = resourcePath;
	}

	@Value("${app.file.handler}")
	public void setFileHandler(String fileHandler)
	{
		PropertyController.FILE_HANDLER = fileHandler;
	}

	@Value("${app.file.path}")
	public void setFilePath(String filePath)
	{
		PropertyController.FILE_PATH = filePath;
	}

	@Value("${app.file.file-upload-path}")
	public void setFileUploadPath(String fileUpload)
	{
		PropertyController.FILE_UPLOAD = fileUpload;
	}

	@Value("${app.file.file-upload-uri}")
	public void setFileUploadUri(String fileUploadUri)
	{
		PropertyController.FILE_UPLOAD_URI = fileUploadUri;
	}

	@Value("${app.file.file-storage-path}")
	public void setFileStoragePath(String fileStorage)
	{
		PropertyController.FILE_STORAGE = fileStorage;
	}

	@Value("${app.file.file-storage-uri}")
	public void setFileStorageUri(String fileStorageUri)
	{
		PropertyController.FILE_STORAGE_URI = fileStorageUri;
	}
}