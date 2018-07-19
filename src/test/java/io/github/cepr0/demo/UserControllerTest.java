package io.github.cepr0.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	@Autowired private MockMvc mvc;
	@Autowired private UserRepo userRepo;
	
	@Before
	public void setUp() {
		// Populate test data
		userRepo.deleteAll();
		IntStream.range(0, 3).mapToObj(i -> new User("User" + i, i + 10)).forEach(userRepo::create);
	}
	
	@Test
	public void shouldReturnAllUsers() throws Exception {
		mvc.perform(get("/users"))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void shouldReturnOneUser() throws Exception {
		mvc.perform(get("/users/{id}", userRepo.getAll().get(0).getId()))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void shouldReturnNotFound() throws Exception {
		mvc.perform(get("/users/{id}", UUID.randomUUID()))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldCreateUser() throws Exception {
		mvc.perform(post("/users")
				.content("{\"name\": \"user\",\"age\": 18}")
				.contentType(APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
				.andExpect(header().exists("Location"))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void shouldUpdateUser() throws Exception {
		mvc.perform(patch("/users/{id}", userRepo.getAll().get(0).getId())
				.content("{\"name\": \"user\",\"age\": 20}")
				.contentType(APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void shouldReturnNotFoundWhileUpdatingUser() throws Exception {
		mvc.perform(patch("/users/{id}", UUID.randomUUID())
				.content("{\"name\": \"user\",\"age\": 20}")
				.contentType(APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldDeleteUser() throws Exception {
		mvc.perform(delete("/users/{id}", userRepo.getAll().get(0).getId()))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void shouldReturnNotFoundWhileDeletingUser() throws Exception {
		mvc.perform(delete("/users/{id}", UUID.randomUUID()))
				.andExpect(status().isNotFound());
	}
	
	@TestConfiguration
	static class TestConfig {

		@Bean
		public UserRepo userRepo() {
			return new UserRepo();
		}
	}
}