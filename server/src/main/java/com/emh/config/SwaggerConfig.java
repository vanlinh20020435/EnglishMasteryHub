package com.emh.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig
{

	@Bean
	public OpenAPI openApiSpec()
	{
		return new OpenAPI().components(new Components()
				.addSchemas("ApiErrorResponse", new ObjectSchema()
						.addProperty("status", new IntegerSchema())
						.addProperty("code", new StringSchema())
						.addProperty("message", new StringSchema())
						.addProperty("fieldErrors", new ArraySchema().items(
								new Schema<ArraySchema>().$ref("ApiFieldError"))))
				.addSchemas("ApiFieldError", new ObjectSchema()
						.addProperty("code", new StringSchema())
						.addProperty("message", new StringSchema())
						.addProperty("property", new StringSchema())
						.addProperty("rejectedValue", new ObjectSchema())
						.addProperty("path", new StringSchema())))
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
					.components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
	}

	private SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer");
	}
}
