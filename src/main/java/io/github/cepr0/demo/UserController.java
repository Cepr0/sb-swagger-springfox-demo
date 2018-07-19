package io.github.cepr0.demo;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Api(
		tags = "/users",
		description = "User related operations"
)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
public class UserController {
	
	private final UserRepo userRepo;
	
	@ApiOperation(
			nickname = "Get users",
			value = "Get all user",
			response = UserResource.class
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = UserResource.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Internal server error", response = SwaggerConfig.ErrorResponse.class)
	})
	@GetMapping
	public List<UserResource> getAll() {
		return userRepo.getAll().stream().map(UserResource::new).collect(toList());
	}

	@ApiOperation(
			nickname = "Get user",
			value = "Get user by ID",
			notes = "Valid value type of ID is UUID",
			response = UserResource.class
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "x-header-1", value = "Example of a custom request header", paramType = "header"),
			@ApiImplicitParam(name = "x-header-2", value = "Example of a custom request header", paramType = "header")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = UserResource.class),
			@ApiResponse(code = 404, message = "User not found"),
			@ApiResponse(code = 500, message = "Internal server error", response = SwaggerConfig.ErrorResponse.class)
	})
	@GetMapping("/{id}")
	public ResponseEntity<?> get(@ApiParam(value = "User's ID", required = true) @PathVariable("id") UUID id) {
		return userRepo.get(id)
				.map(UserResource::new)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	@ApiOperation(
			nickname = "Create user",
			value = "Create new user",
			code = 201
	)
	@ApiResponses(value = {
			@ApiResponse(
					code = 201,
					message = "Success", response = UserResource.class,
					responseHeaders = @ResponseHeader(name = "Location", description = "Link to the new user", response = String.class)
			),
			@ApiResponse(code = 500, message = "Internal server error", response = SwaggerConfig.ErrorResponse.class)
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody UserDto userDto) {
		User user = userRepo.create(userDto.toUser());
		return ResponseEntity.created(URI.create(linkTo(methodOn(UserController.class).get(user.getId())).withSelfRel().getHref()))
				.body(new UserResource(user));
	}
	
	@ApiOperation(
			nickname = "Update user",
			value = "Update existed user by ID",
			notes = "Valid value type of ID is UUID"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = UserResource.class),
			@ApiResponse(code = 404, message = "User not found"),
			@ApiResponse(code = 500, message = "Internal server error", response = SwaggerConfig.ErrorResponse.class)
	})
	@PatchMapping("/{id}")
	public ResponseEntity<?> update(
			@ApiParam(value = "User's ID", required = true) @PathVariable("id") UUID id,
			@RequestBody UserDto userDto
	) {
		return userRepo.update(id, userDto.toUser())
				.map(UserResource::new)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	@ApiOperation(
			nickname = "Delete user",
			value = "Delete existed user by ID",
			notes = "Valid value type of ID is UUID",
			code = 204
	)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Success", response = Void.class),
			@ApiResponse(code = 404, message = "User not found"),
			@ApiResponse(code = 500, message = "Internal server error", response = SwaggerConfig.ErrorResponse.class)
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> delete(@ApiParam(value = "User's ID", required = true) @PathVariable("id") UUID id) {
		if(userRepo.delete(id) == 1) return ResponseEntity.noContent().build();
		else return ResponseEntity.notFound().build();
	}
}
