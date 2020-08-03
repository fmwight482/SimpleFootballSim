package SimpleFootballSim.DownSeriesSim;

import SimpleFootballSim.FootballException;

/**
 * Object to track appearances of and scores from a DownSeriesFootballGame state
 */
public class DownSeriesState {
	/**
	 * The number of terminating series involving this state before we consider it's expected point value
	 * to be final.
	 */
	private static final int reps = 100000;

	private final String team;
	private final int yardLine;

	private int counter = 0;
	private int points = 0;
	private double expectedPoints;

	public DownSeriesState(String team, int yardLine) {
		this.team = team;
		this.yardLine = yardLine;
	}

	/**
	 * Add the net point value of the latest series and increment the counter.
	 * If the counter has reached the reps threshold, calculate an expectedPoints value.
	 * @param pts net points scored
	 */
	public void addPoints(int pts) {
		counter++;
		points += pts;

		if (counter >= reps) {
			expectedPoints = points / (double)counter;
		}
	}

	/**
	 * @return true if this state has tracked enough scores that we consider it's expected point value final
	 */
	public boolean arePointsFinal() {
		return counter >= reps;
	}

	/**
	 * @return the expected points value of this state
	 */
	public double getExpectedPoints() throws FootballException {
		if (counter >= reps) {
			return expectedPoints;
		}
		else {
			throw new FootballException("Tried to get expectedPoints value of state " + this +
					", but counter value " + counter + " < " + reps);
		}
	}

	public String getTeam() {
		return team;
	}

	public int getYardLine() {
		return yardLine;
	}

	/**
	 * package-private getter for testing purposes
	 */
	int getCounter() {
		return counter;
	}

	/**
	 * package-private getter for testing purposes
	 */
	int getPoints() {
		return points;
	}

	/**
	 * package-private setter for testing purposes
	 */
	void setCounter(int cnt) {
		counter = cnt;
	}

	/**
	 * package-private setter for testing purposes
	 */
	void setPoints( int pts) {
		points = pts;
	}

	@Override
	public String toString() {
		return team + "@" + yardLine;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DownSeriesState) {
			DownSeriesState dss = (DownSeriesState)obj;
			return (dss.getTeam().equals(this.team) && dss.getYardLine() == this.yardLine);
		}
		return false;
	}
}
