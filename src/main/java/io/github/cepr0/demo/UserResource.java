package io.github.cepr0.demo;

import lombok.Value;

import java.util.UUID;

@Value
public class UserResource {
	private UUID userId;
	private String name;
	private Integer age;
	
	public UserResource(User user) {
		this.userId = user.getId();
		this.name = user.getName();
		this.age = user.getAge();
	}
}
