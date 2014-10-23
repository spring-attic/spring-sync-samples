/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package todos;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Roy Clarkson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
@Ignore
public class MainControllerTest {

	@Autowired
	private WebApplicationContext context;

	@Mock
	private TodoRepository repository;

	@InjectMocks
	TodoController mainController;

	private MockMvc mvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(mainController).build();
	}

	@Test
	public void testList() throws Exception {
		final Todo a = new Todo(1L, "a", false);
		final Todo b = new Todo(2L, "b", false);
		final Todo c = new Todo(3L, "c", false);
		when(repository.findAll()).thenReturn(Arrays.asList(a, b, c));

		mvc.perform(get("/todos")
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].description", is("a")))
				.andExpect(jsonPath("$[0].complete", is(false)))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].description", is("b")))
				.andExpect(jsonPath("$[1].complete", is(false)))
				.andExpect(jsonPath("$[2].id", is(3)))
				.andExpect(jsonPath("$[2].description", is("c")))
				.andExpect(jsonPath("$[2].complete", is(false)));

		 verify(repository, times(1)).findAll();
		 verifyNoMoreInteractions(repository);
	}

	@Ignore
	@Test
	public void testPatch() throws Exception {

	}

	@Test
	public void testCreate() throws Exception {
		final Todo todo = new Todo(1L, "a", false);
		ObjectMapper objectMapper = new ObjectMapper();
		final byte[] bytes = objectMapper.writeValueAsBytes(todo);

		when(repository.save(Mockito.any(Todo.class))).thenReturn(todo);

		mvc.perform(post("/todos")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(bytes))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.description", is("a")))
				.andExpect(jsonPath("$.complete", is(false)));

		verify(repository, times(1)).save(Mockito.any(Todo.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void testUpdateSameIds() throws Exception {
		final Todo updatedTodo = new Todo(1L, "z", true);
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] bytes = objectMapper.writeValueAsBytes(updatedTodo);

		when(repository.save(Mockito.any(Todo.class))).thenReturn(updatedTodo);

		mvc.perform(put("/todos/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(bytes))
				.andExpect(status().isNoContent());

		verify(repository, times(0)).delete(1L);
		verify(repository, times(1)).save(Mockito.any(Todo.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void testUpdateDifferentIds() throws Exception {
		final Todo updatedTodo = new Todo(99L, "z", true);
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] bytes = objectMapper.writeValueAsBytes(updatedTodo);

		when(repository.save(Mockito.any(Todo.class))).thenReturn(updatedTodo);

		mvc.perform(put("/todos/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(bytes))
				.andExpect(status().isNoContent());

		verify(repository, times(1)).delete(1L);
		verify(repository, times(1)).save(Mockito.any(Todo.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void testDelete() throws Exception {
//		this is how to test a void method with Mockito
//		doThrow(new IllegalArgumentException()).when(repository).delete(null);

		mvc.perform(delete("/todos/{id}", 1L))
				.andExpect(status().isNoContent());
	}

}