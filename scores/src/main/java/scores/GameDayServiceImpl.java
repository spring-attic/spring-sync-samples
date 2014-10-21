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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class GameDayServiceImpl implements GameDayService {
	
	private Logger logger = LoggerFactory.getLogger(GameDayServiceImpl.class);

	private ScoreRepository scoreRepo;

	@Autowired
	public GameDayServiceImpl(ScoreRepository scoreRepo) {
		this.scoreRepo = scoreRepo;
	}
	
	@Override
	public List<Game> getScores() {
		return scoreRepo.findAll();
	}
	
	@Scheduled(fixedRate=5000)
	public void updateScores() {
		List<Game> scores = getScores(); // fetch data
		randomlyUpdate(scores);          // update data
		scoreRepo.save(scores);          // save data
	}
	
	// private helpers
	
	/*
	 * The code in this method was extracted so that updateScoresRandomly could focus on the basics of
	 * updating some data and saving it. This method is entirely focused on the task of updating the
	 * data and is largely filled with random number generation. Its function is only passively
	 * relevant to the concern of differential synchronization.
	 */
	private void randomlyUpdate(List<Game> scores) {
		// choose a game to update
		int gameIndex = (int)(Math.random() * scores.size());
		
		// choose either a touchdown or a field goal
		int points = Math.random() > 0.6 ? 7 : 3;
		String pointType = points == 3 ? "field goal" : "touchdown";
		
		// choose either home or away team
		double homeOrAway = Math.random();
		
		// adjust score
		Game game = scores.get(gameIndex);
		if (homeOrAway > 0.5) {
			logger.info("The " + game.getHomeTeam() + " scored a " + pointType + " against the " + game.getAwayTeam());
			game.incrementHomeTeamScore(points);
		} else {
			logger.info("The " + game.getAwayTeam() + " scored a " + pointType + " against the " + game.getHomeTeam());
			game.incrementAwayTeamScore(points);
		}
	}

}
