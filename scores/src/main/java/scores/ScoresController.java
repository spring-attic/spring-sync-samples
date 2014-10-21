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
package scores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ScoresController {

	private GameDayService service;

	@Autowired
	public ScoresController(GameDayService service) {
		this.service = service;
	}

	/*
	 * For the client to get an initial list of game scores without waiting on the next update.
	 * Could also have been done via a GET request, but where's the fun in that?
	 */
	@SubscribeMapping("/scores")
	public List<Game> scores() {
		return service.getScores();
	}
	
}
