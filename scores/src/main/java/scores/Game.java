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

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id = null;
	
	private final String homeTeam;
	private final String awayTeam;
	private int homeTeamScore = 0;
	private int awayTeamScore = 0;
	
	public Game(String homeTeam, String awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public String getHomeTeam() {
		return homeTeam;
	}
	
	public int getHomeTeamScore() {
		return homeTeamScore;
	}
	
	public void incrementHomeTeamScore(int points) {
		this.homeTeamScore += points;
	}
	
	public String getAwayTeam() {
		return awayTeam;
	}
	
	public int getAwayTeamScore() {
		return awayTeamScore;
	}
	
	public void incrementAwayTeamScore(int points) {
		this.awayTeamScore += points;
	}

}
