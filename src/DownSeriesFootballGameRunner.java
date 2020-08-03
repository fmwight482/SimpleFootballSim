import java.util.Random;

public class DownSeriesFootballGameRunner {

	public static void main(String args[]) throws FootballException {
		Random rand = new Random();

		DownSeriesFootballTeam archers = new DownSeriesFootballTeam("ATL", rand, 0.67);
		DownSeriesFootballTeam redWings = new DownSeriesFootballTeam("DRW", rand, 0.67);

		DownSeriesFootballGame game = new DownSeriesFootballGame(archers, redWings, rand);

		int reps = 100000;
		int yardline = 75;
		int samples = 99;

		for (int i=1; i<=samples; i++) {
			double ep = game.getExpectedPoints(i, archers, reps);
			System.out.print("Expected points value for " + archers + " ball at the " + i + " in " + reps +
					" attempts is " + ep);
			System.out.println();
		}
	}
}
