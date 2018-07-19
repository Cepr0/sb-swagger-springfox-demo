package io.github.cepr0.demo;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
public class SwaggerConfig {
	
	@Value("${app.version}")
	private String appVersion;
	
	private final TypeResolver typeResolver;
	
	@Autowired
	public SwaggerConfig(TypeResolver typeResolver) {
		this.typeResolver = typeResolver;
	}
	
	@Bean
	public Docket api() {
		return new Docket(SWAGGER_2)
				.apiInfo(getApiInfo())
				.useDefaultResponseMessages(false)
//				.genericModelSubstitutes(ResponseEntity.class)
//				.ignoredParameterTypes(ResponseEntity.class)
//				.directModelSubstitute(LocalDate.class, String.class)
//				.alternateTypeRules(
//						newRule(
//								typeResolver.resolve(
//										DeferredResult.class,
//										typeResolver.resolve(ResponseEntity.class, WildcardType.class)
//								),
//								typeResolver.resolve(WildcardType.class)
//						)
//				)
				.host("example.com")
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build();
	}
	
	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder()
				.title("User API")
				.version(appVersion)
				.build();
	}
	
	@Bean
	UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder()
				.deepLinking(false)
				.displayOperationId(true)
				.defaultModelsExpandDepth(1)
				.defaultModelExpandDepth(1)
				.defaultModelRendering(ModelRendering.EXAMPLE)
				.displayRequestDuration(false)
				.docExpansion(DocExpansion.LIST)
				.filter(false)
				.maxDisplayedTags(null)
				.operationsSorter(OperationsSorter.ALPHA)
				.showExtensions(false)
				.tagsSorter(TagsSorter.ALPHA)
				.supportedSubmitMethods(new String[]{})
				.validatorUrl(null)
				.build();
	}
	
	@ApiModel(value = "Error", reference = "Error body", description = "Error body")
	interface ErrorResponse {
		
		@ApiModelProperty(
				value = "Error's timestamp",
				example = "1413313361387.1234",
				dataType = "timestamp",
				required = true,
				position = 1
		)
		BigDecimal getTimestamp();
		
		@ApiModelProperty(
				value = "Error's exeption",
				example = "org.springframework.web.bind.MissingServletRequestParameterException",
				dataType = "java.lang.String",
				required = true,
				position = 2
		)
		String getException();
		
		@ApiModelProperty(
				value = "Error's HTTP status",
				example = "500",
				dataType = "java.lang.Integer",
				required = true,
				position = 3
		)
		Integer getStatus();
		
		@ApiModelProperty(
				value = "Error's status description",
				example = "Internal server error",
				dataType = "java.lang.String",
				required = true,
				position = 4
		)
		String getError();
		
		@ApiModelProperty(
				value = "Request path of the error",
				example = "/path",
				dataType = "java.lang.String",
				required = true,
				position = 5
		)
		String getPath();
		
		@ApiModelProperty(
				value = "Error's timestamp",
				example = "Required parameter is not present",
				dataType = "java.lang.String",
				required = true,
				position = 6
		)
		String getMessage();
	}
}
