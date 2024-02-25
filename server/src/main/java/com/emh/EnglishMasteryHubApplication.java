package com.emh;

import com.emh.service.FilesStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EnglishMasteryHubApplication implements CommandLineRunner
{
	@Resource
	FilesStorageService storageService;

	public static void main(final String[] args)
	{
		SpringApplication.run(EnglishMasteryHubApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception
	{
//    storageService.deleteAll();
		storageService.init();
	}
}
