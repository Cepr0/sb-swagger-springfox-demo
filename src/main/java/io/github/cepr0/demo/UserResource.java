package io.github.cepr0.demo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import java.util.UUID;

@ApiModel(value = "UserResource", reference = "User response body", description = "User response body")
@Value
public class UserResource {
	
	@ApiModelProperty(
			value = "User's ID",
			example = "8f63faa0-e5c7-4bbf-93f4-783173357a09",
			dataType = "java.util.UUID",
			required = true,
			position = 1
	)
	private UUID userId;
	
	@ApiModelProperty(
			value = "User's name",
			example = "John Smith",
			dataType = "java.lang.String",
			required = true,
			position = 2
	)
	private String name;
	
	@ApiModelProperty(
			value = "User's age",
			example = "18",
			allowableValues = "range[1, 150]",
			dataType = "java.lang.Integer",
			required = true,
			position = 3
	)
	private Integer age;
	
	public UserResource(User user) {
		this.userId = user.getId();
		this.name = user.getName();
		this.age = user.getAge();
	}
}
