package SimpleFootballSim;

import SimpleFootballSim.DownSeriesSim.DownSeriesFootballTeam;

import java.util.Random;

public class FootballGame {
	/**
	 * distance from the goal line from the perspective of the current offensive team
	 */
	private int yardLine;
	private int down;
	private int yardsToGo;

	private int gainOnPlay;

	private int passPct = 60;
	private int compPct = 60;

	Random rand = new Random();

	/**
	 * The team that starts on offense
	 */
	private DownSeriesFootballTeam team1;

	/**
	 * The team that starts on defense
	 */
	private DownSeriesFootballTeam team2;

	private DownSeriesFootballTeam currentOffTeam;

	public FootballGame(int aYardline, int aDown, int ytg, DownSeriesFootballTeam offTeam, DownSeriesFootballTeam defTeam) {
		yardLine = aYardline;
		down = aDown;
		yardsToGo = ytg;
		gainOnPlay = 0;
		team1 = offTeam;
		team2 = defTeam;
		currentOffTeam = offTeam;
	}

	/**
	 * Helper function
	 * @return a random integer in the form of a percentile
	 */
	private int randPct() {
		return rand.nextInt(100)+1;
	}

	private String getDownDistStr() {
		String downStr;
		if (down == 1) {
			downStr = "1st";
		}
		else if (down == 2) {
			downStr = "2nd";
		}
		else if (down == 3) {
			downStr = "3rd";
		}
		else {
			downStr = "4th";
		}
		return downStr + " and " + yardsToGo;
	}


	public int getNextScore() {
		int nextScore = 0;

		while (nextScore == 0) {
			nextScore = executeDownSeries();
		}

		return nextScore;
	}

	private int executeDownSeries() {
		int pointsScored = 0;
		return pointsScored;
	}

	private int executePlay() {
		int pointsScored = 0;

		if (down == 1 || down == 2 || down == 3) {
			if (randPct() < passPct) {
				// pass play
				if (randPct() < compPct) {
					// completed pass
					gainOnPlay = getPassYards();
				}
			}
			else {
				// run play
				gainOnPlay = getRushYards();
			}
		}

		return pointsScored;
	}

	private int processPlay() {
		int pointsScored = 0;
		System.out.println(getDownDistStr());
		System.out.print("Gained " + gainOnPlay + " yards. ");
		if (gainOnPlay >= yardsToGo) {
			down = 1;
			System.out.println("First down!");
		}
		else if (down == 4) {
			System.out.println("Turnover on downs.");
		}
		else {
			down++;
		}

		yardLine -= gainOnPlay;

		if (yardLine <= 0) {
			pointsScored = 7;
		}

		return pointsScored;
	}

	private int getRushYards() {
		int yards;
		int roll = randPct();
		if (roll <= 7) {
			yards = -2;
		}
		else if (roll <= 12) {
			yards = -1;
		}
		else if (roll <= 21) {
			yards = 0;
		}
		else if (roll <= 33) {
			yards = 1;
		}
		else if (roll <= 45) {
			yards = 2;
		}
		else if (roll <= 57) {
			yards = 3;
		}
		else if (roll <= 67) {
			yards = 4;
		}
		else if (roll <= 75) {
			yards = 5;
		}
		else if (roll <= 80) {
			yards = 6;
		}
		else if (roll <= 84) {
			yards = 7;
		}
		else if (roll <= 87) {
			yards = 8;
		}
		else if (roll <= 90) {
			yards = 9;
		}
		else if (roll <= 92) {
			yards = 10;
		}
		else {
			yards = rand.nextInt(10)+11;
		}
		if (yards > 19) {
			yards += rand.nextInt(99);
		}

		return yards;
	}

	private int getPassYards() {
		int yards;
		int roll = randPct();
		if (roll <= 4) {
			yards = -1;
		}
		else if (roll <= 7) {
			yards = 1;
		}
		else if (roll <= 15) {
			yards = 3;
		}
		else if (roll <= 52) {
			yards = rand.nextInt(6) + 4;
		}
		else {
			yards = rand.nextInt(16) + 10;
		}

		if (yards >= 25) {
			yards += rand.nextInt(99);
		}

		return yards;
	}
}
