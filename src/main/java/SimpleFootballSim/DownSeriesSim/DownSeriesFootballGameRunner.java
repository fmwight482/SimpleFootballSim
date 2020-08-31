package SimpleFootballSim.DownSeriesSim;

import SimpleFootballSim.FootballException;

import java.util.Random;

public class DownSeriesFootballGameRunner {

	public static void executeGames(int reps, String name1, double dsr1, String name2, double dsr2)
			throws FootballException {
		Random rand = new Random();

		DownSeriesFootballTeam team1 = new DownSeriesFootballTeam(name1, dsr1, rand);
		DownSeriesFootballTeam team2 = new DownSeriesFootballTeam(name2, dsr2, rand);

		DownSeriesFootballGame game = new DownSeriesFootballGame(team1, team2, rand);

		// Get values for yard lines 1-99
		int samples = 99;

		System.out.println("**** " + team1 + " ****");
		for (int i=1; i<=samples; i++) {
			double ep = game.getExpectedPoints(i, team1, reps);
			System.out.printf("Expected points value for %s ball at the %d in %d attempts is %.2f",
					team1, i, reps, Math.round(ep * 100.0) / 100.0);
			System.out.println();
		}

		System.out.println("**** " + team2 + " ****");
		for (int i=1; i<=samples; i++) {
			double ep = game.getExpectedPoints(i, team2, reps);
			System.out.printf("Expected points value for %s ball at the %d in %d attempts is %.2f",
					team2, i, reps, Math.round(ep * 100.0) / 100.0);
			System.out.println();
		}
	}

	public static void main(String[] args) {
		executeGames(100000, "BAL", 0.79, "OPP", 0.67);
	}
}
