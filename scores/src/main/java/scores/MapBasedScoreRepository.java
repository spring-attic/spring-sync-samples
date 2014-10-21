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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.stereotype.Repository;

@Repository
public class MapBasedScoreRepository implements ScoreRepository {

	private AtomicLong nextId = new AtomicLong(1L);
	
	private Map<Long, Game> games = new HashMap<Long, Game>();
	
	public MapBasedScoreRepository() {
		initializeGames();
	}
	
	public List<Game> findAll() {
		return deepCloneList(new ArrayList<Game>(games.values()));
	}

	public Game findOne(Long id) {
		return games.get(id);
	}

	public void save(List<Game> games) {
		for (Game game : games) {
			save(game);
		}
	}

	public Game save(Game game) {
		if (game.getId() == null) {
			game.setId(nextId.incrementAndGet());
		}
		
		games.put(game.getId(), game);
		
		return game;
	}

	// private helpers
	
	private void initializeGames() {
		List<String> teamPool = new ArrayList<String>(Arrays.asList(ALL_TEAMS));		
		while(!teamPool.isEmpty()) {
			String homeTeam = teamPool.get((int)(Math.random() * teamPool.size()));
			teamPool.remove(homeTeam);
			String awayTeam = teamPool.get((int)(Math.random() * teamPool.size()));
			teamPool.remove(awayTeam);
			save(new Game(homeTeam, awayTeam));
		}
	}
	
	private List<Game> deepCloneList(List<Game> original) {
		List<Game> copy = new ArrayList<Game>(original.size());
		for(Game t : original) {
			copy.add((Game) SerializationUtils.clone((Serializable) t)); 
		}
		return copy;
	}
	
	private static final String[] ALL_TEAMS = {
		"Ravens", "Bengals", "Browns", "Steelers",
		"Texans", "Colts", "Jaguars", "Titans",
		"Bills", "Dolphins", "Patriots", "Jets",
		"Broncos", "Chiefs", "Raiders", "Chargers",
		"Bears", "Lions", "Packers", "Vikings",
		"Falcons", "Panthers", "Saints", "Buccaneers",
		"Cowboys", "Giants", "Eagles", "Redskins",
		"Cardinals", "49ers", "Seahawks", "Rams"
	};

}
