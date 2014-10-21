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

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.sync.diffsync.web.JsonPatchHttpMessageConverter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ComponentScan
@EnableAutoConfiguration
public class Application extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		TodoRepository repository = ctx.getBean(TodoRepository.class);
		repository.save(new Todo(1L, "a", false));
		repository.save(new Todo(2L, "b", false));
		repository.save(new Todo(3L, "c", false));
	}

	@Bean
	public ShallowEtagHeaderFilter etagFilter() {
		return new ShallowEtagHeaderFilter();
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new JsonPatchHttpMessageConverter());
	}
	
}
