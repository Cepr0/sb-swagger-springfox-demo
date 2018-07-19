package io.github.cepr0.demo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "UserDTO", reference = "User request body", description = "User request body")
@Value
public class UserDto {
	
	@ApiModelProperty(
			value = "User's name",
			example = "John Smith",
			dataType = "java.lang.String",
			required = true,
			position = 1
	)
	@NotBlank
	private String name;
	
	
	@ApiModelProperty(
			value = "User's age",
			example = "18",
			allowableValues = "range[1, 150]",
			dataType = "java.lang.Integer",
			required = true,
			position = 2
	)
	@NotNull
	@Min(1)
	@Max(150)
	private Integer age;
	
	public User toUser() {
		return new User(name, age);
	}
}
