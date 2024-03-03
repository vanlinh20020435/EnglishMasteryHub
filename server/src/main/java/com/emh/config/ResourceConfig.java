package com.emh.config;

import com.emh.controller.PropertyController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
@EnableWebMvc
public class ResourceConfig implements WebMvcConfigurer
{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		String rootPath = new File("").getAbsolutePath();
		String path = "file:" + rootPath + "/";
		System.out.println(rootPath);
//		registry.addResourceHandler(PropertyController.RESOURCE_HANDLER).addResourceLocations("file:E:/Workspace/EnglishMasteryHub/server/uploads/");
		registry.addResourceHandler(PropertyController.RESOURCE_HANDLER).
				addResourceLocations(path + PropertyController.RESOURCE_PATH + "/",
						path + PropertyController.FILE_PATH + "/");
	}
}
