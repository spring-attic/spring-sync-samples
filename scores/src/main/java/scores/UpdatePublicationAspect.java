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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.sync.Diff;
import org.springframework.sync.Patch;
import org.springframework.sync.json.JsonPatchPatchConverter;

@Aspect
@Component
public class UpdatePublicationAspect {

	// TODO: Reconsider this aspect. Instead have GameDayService publish a score update via Reactor.
	//       Rework this aspect into a handler for the update that sends to the client.

	private GameDayService service;
	private SimpMessagingTemplate messaging;

	@Autowired
	public UpdatePublicationAspect(GameDayService service, SimpMessagingTemplate messaging) {
		this.service = service;
		this.messaging = messaging;
	}
	
	@Around("execution(* save(..)) && args(updated)")
	public void publishChange(ProceedingJoinPoint jp, Object updated) throws Throwable {
		List<Game> original = service.getScores();
		
		jp.proceed();

		Patch diff = Diff.diff(original, updated);
		messaging.convertAndSend("/topic/scores", new JsonPatchPatchConverter().convert(diff));
	}
	
}
