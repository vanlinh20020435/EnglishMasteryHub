package com.emh.util;

import csv.impl.AbstractStreamTableReader;
import csv.impl.CSVReader;
import csv.impl.ExcelReader;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileReaderUtil
{
	public static AbstractStreamTableReader from(MultipartFile file) throws Exception
	{
		String fileName = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(fileName);
		InputStream inputStream = file.getInputStream();
		assert extension != null;
		AbstractStreamTableReader reader = switch (extension)
		{
			case "txt", "csv" ->
			{
				CSVReader csvReader = new CSVReader(inputStream);
				csvReader.setColumnSeparator(',');
				yield csvReader;
			}
			case "xlsx", "xls" -> new ExcelReader(inputStream);
			default -> throw new AppException("Unknown file extension");
		};
		reader.setCharsetDecoder(StandardCharsets.UTF_8.newDecoder());
		return reader;
	}

	public static AbstractStreamTableReader from(File file) throws Exception
	{
		String fileName = file.getName();
		String extension = FilenameUtils.getExtension(fileName);
		return switch (extension)
		{
			case "txt", "csv" ->
			{
				CSVReader reader = new CSVReader(file);
				reader.setColumnSeparator(',');
				yield reader;
			}
			case "xlsx", "xls" -> new ExcelReader(file);
			default -> throw new AppException("Unknown file extension");
		};
	}
}
