package SimpleFootballSim.DownSeriesSim;

import SimpleFootballSim.DriveResult;
import SimpleFootballSim.FootballException;
import SimpleFootballSim.FourthDownCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Football game based on down series instead of individual plays. This approach is simpler to program
 * and easier to customize from team to team, but less accurate on the micro level.
 * Should work well for Expected Points calculations, but not Win Probability.
 * Values given will be for first downs only.
 */
public class DownSeriesFootballGame {
	private boolean verbose = false;

	/**
	 * The team that starts on offense
	 */
	private DownSeriesFootballTeam team1;

	/**
	 * The team that starts on defense
	 */
	private DownSeriesFootballTeam team2;

	/**
	 * Random number generator for this game
	 */
	private Random rand;

	public List<DriveResult> drives = new ArrayList<>();

	/**
	 * Constructor for a new Football Game
	 * @param teamA the first team (arbitrary)
	 * @param teamB the second team (arbitrary)
	 * @throws FootballException if teamA and teamB are identical
	 */
	public DownSeriesFootballGame(DownSeriesFootballTeam teamA, DownSeriesFootballTeam teamB, Random random) throws FootballException {
		if (teamA.equals(teamB)) {
			throw new FootballException("teamA and teamB have the same name");
		}
		team1 = teamA;
		team2 = teamB;

		rand = random;
	}

	/**
	 * Get expected points for offTeam in the given scenario
	 * @param yardline the yardline where team1 has the ball at the start
	 * @param offTeam the team starting on offense in this scenario
	 * @param reps the number of times this scenario will be run
	 * @return the expected point value of the given situation
	 */
	public double getExpectedPoints(int yardline, DownSeriesFootballTeam offTeam, int reps) throws FootballException {
		double netPoints = 0;
		verifyTeam(offTeam);

		for (int i=0; i<reps; i++) {
			netPoints += getNextPoints(yardline, offTeam);
		}
		return netPoints / reps;
	}

	/**
	 * Get the net points for the offensive team on the next score.
	 * Negative values mean the other team scored points.
	 * @param yardline the current field position
	 * @param startingOffTeam the team currently on offense
	 * @return the net points for startingOffTeam on the next score
	 */
	private double getNextPoints(int yardline, DownSeriesFootballTeam startingOffTeam) {
		double nextPoints = 0;
		boolean turnover = false;
		int driveCount = 1;

		DownSeriesFootballTeam offTeam = startingOffTeam;
		DownSeriesFootballTeam defTeam;
		if (offTeam.equals(team1)) {
			defTeam = team2;
		}
		else {
			defTeam = team1;
		}

		while (nextPoints == 0) {
			if (turnover) {
				// Swap offense/defense
				if (offTeam.equals(team1)) {
					defTeam = team1;
					offTeam = team2;
				}
				else {
					defTeam = team2;
					offTeam = team1;
				}

				// swap reference point for field position
				yardline = 100 - yardline;
				turnover = false;
				driveCount++;
			}
			println("1st and 10 " + offTeam + " from the " + yardline);

			int yardsGained;
			if (rand.nextDouble() <= getGameDsr(offTeam, yardline)) {
				yardsGained = offTeam.getYardsOnConversion(yardline);
				yardline -= yardsGained;
				println(offTeam + " gained " + yardsGained + " yards and converted the down.");
				if (yardline <= 0) {
					// Touchdown
					nextPoints = 6.5;
					if (verbose) {
						println("Touchdown, " + offTeam + "!");
					}
				}
			}
			else {
				yardsGained = offTeam.getYardsOnStop(yardline);
				if (yardsGained < 0) {
					println(offTeam + " lost " + yardsGained + " yards and failed to convert the down.");
				}
				else {
					println(offTeam + " gained " + yardsGained + " yards but failed to convert the down");
				}
				int yardsToGo = 10 - yardsGained;
				yardline -= yardsGained;
				if (yardline >= 100) {
					// Safety
					nextPoints = -2.5;
					println("Safety on " + offTeam + "!");
				}
				else if (rand.nextDouble() <= offTeam.getTurnoverPct()) {
					turnover = true;
					println(offTeam + " lost the ball! Recovered by " + defTeam);
				}
				else {
					FourthDownCall call = offTeam.make4thDownCall(yardline, yardsToGo);
					if (call.equals(FourthDownCall.PUNT)) {
						turnover = true;
						yardsGained = offTeam.getYardsOnPunt();
						yardline -= yardsGained;
						print(offTeam + " punts for " + yardsGained + " yards, ");
						if (yardline <= 0) {
							// Touchback
							yardline = 20;
							println("Touchback.");
						} else {
							int returnYards = defTeam.getYardsOnPuntReturn();
							yardline += returnYards;
							println("returned by " + defTeam + " for " + returnYards + " yards.");
							if (yardline >= 100) {
								// Punt return touchdown
								nextPoints = -6.5;
								println("Touchdown, " + defTeam + "!");
							}
						}
					} else if (call.equals(FourthDownCall.FIELD_GOAL)) {
						print("Field goal attempt by " + offTeam + " from " + (yardline + 17) + " yards out is ");
						if (rand.nextDouble() <= offTeam.getFGProbability(yardline)) {
							nextPoints = 2.5;
							println("Good!");
						} else {
							println("No good!");
							turnover = true;
							yardline += 10;
							if (yardline >= 100) {
								// A missed FG in the end zone is a safety
								nextPoints = -2.5;
								println("Safety on " + offTeam + "!");
							}
						}
					} else {
						println("Unanticipated 4th down conversion attempt! Fails automatically.");
						turnover = true;
					}
				}
			}
		}
		if (!offTeam.equals(startingOffTeam)) {
			nextPoints = -1 * nextPoints;
		}
		if (drives.size() <= driveCount) {
			for (int i=drives.size(); i<=driveCount; i++) {
				drives.add(new DriveResult());
			}
		}
		drives.get(driveCount-1).addScore(nextPoints);

		return nextPoints;
	}

	/**
	 * Get the Drive success rate of this team, adjusted for proximity to the goal line
	 * @param offTeam the team currently on offense
	 * @param yardline the current field position
	 * @return the expected Drive Success Rate
	 */
	public double getGameDsr(DownSeriesFootballTeam offTeam, int yardline) {
		double dsr = offTeam.getDsr();
		if (yardline < 10) {
			// adjust dsr for the reduced distance needed to convert
			dsr = 1 - (1 - dsr) * yardline / 10;
		}
		if (yardline <= 20) {
			// adjust dsr for the difficulty of working close to the goal line
			// dsr reduced to 90% at the one yard line, penalty decreases linearly and zeros out at the 21
			dsr = dsr * (179 + yardline) / 200;
		}
		return dsr;
	}

	/**
	 * Verify that the given team is one of the two teams in this game.
	 * @param aTeam a football team
	 * @throws FootballException if the given team does not match either team in this game
	 */
	private void verifyTeam(DownSeriesFootballTeam aTeam) throws FootballException {
		if (!aTeam.equals(team1) && !aTeam.equals(team2)) {
			throw new FootballException("offTeam does not match team1 or team2");
		}
	}

	/**
	 * Helper function. Prints the given string if verbose is true.
	 * @param s the string to print
	 */
	private void print(String s) {
		if (verbose) {
			System.out.print(s);
		}
	}

	/**
	 * Helper function. Prints the given string as a new line if verbose is true.
	 * @param s the string to print
	 */
	private void println(String s) {
		if (verbose) {
			System.out.println(s);
		}
	}
}
