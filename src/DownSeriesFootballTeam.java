import java.util.Random;

/**
 * A Football Team. Contains Attributes which affect how that team performs
 */
public class DownSeriesFootballTeam {
	private String name;
	private Random rand;

	/**
	 * offensive Drive Success Rate for this team
	 */
	private double dsr;

	/**
	 * the percentage chance that a failed set of downs resulted in a turnover (a fumble or interception)
	 */
	private double turnoverPct = 0.17;

	/**
	 * gross average on punts by this team
	 */
	private int grossPuntAvg = 45;

	/**
	 * percentage of punts fielded by this team which are returned
	 */
	private double returnPct = 0.5;

	/**
	 * return average on punts returned by this team
	 */
	private int returnAvg = 10;

	public DownSeriesFootballTeam(String aName, Random aRand, double aDSR) {
		name = aName;
		rand = aRand;
		dsr = aDSR;
	}

	public String getName() {
		return name;
	}

	/**
	 * set the Drive Success Rate value to the given value
	 * @param newDsr Drive Success Rate
	 */
	public void setDsr(double newDsr) {
		dsr = newDsr;
	}

	/**
	 * @return the Drive Success Rate of this team
	 */
	public double getDsr() {
		return dsr;
	}

	/**
	 * @return the percentage of failed sets of downs which result in a turnover
	 * (a fumble or interception)
	 */
	public double getTurnoverPct() {
		return turnoverPct;
	}

	/**
	 * Decides what this team will do on 4th down
	 * @param yardline the yardline at which this team possesses the ball
	 * @param yardsToGo the number of yards needed for a first down
	 * @return an enum representing what this team will try to do
	 */
	public FourthDownCall make4thDownCall(int yardline, int yardsToGo) {
		if (yardline < 34) {
			return FourthDownCall.FIELD_GOAL;
		}
		else {
			return FourthDownCall.PUNT;
		}
	}

	/**
	 * get the gross distance on this team's next punt.
	 * Generated randomly using team specific parameters.
	 * @return a gross punt distance
	 */
	public int getYardsOnPunt() {
		return grossPuntAvg + rand.nextInt(11) - 5;
	}

	/**
	 * get the return yardage on this teams next fielded punt.
	 * Generated randomly using team specific parameters.
	 * @return a return distance
	 */
	public int getYardsOnPuntReturn() {
		int retYards = 0;
		if (rand.nextDouble() <= returnPct) {
			retYards = rand.nextInt(returnAvg) + rand.nextInt(returnAvg);
		}
		return retYards;
	}

	/**
	 * Get the probability that this team would convert a field goal attempt from this yard line
	 * @param yardline the current field position of this team
	 * @return the probability of making a field goal
	 */
	public double getFGProbability(int yardline) {
		//TODO: replace hardcoded values with something easily modifiable
		double makeProb = 1 - (0.026 * Math.exp(0.0952 * yardline));
		if (makeProb < 0) {
			makeProb = 0;
		}

		return makeProb;
	}

	/**
	 * Returns the total yards gained on a successful conversion attempt.
	 * Range is 10 to 25, linear when at least 25 yards from the goal, 10 to goal line when closer.
	 * @param yardline the current field position of this team
	 * @return the total yards gained
	 */
	public int getYardsOnConversion(int yardline) {
		int yards;
		if (yardline >= 25) {
			yards = 10 + rand.nextInt(16);
		}
		else {
			yards = 10 + rand.nextInt(Math.max(1, yardline - 9));
		}

		return yards;
	}

	/**
	 * Returns the total yards gained on an unsuccessful conversion attempt.
	 * Range is -5 to 9, skewed towards 3 when 5 or more yards from your own goal.
	 * When closer to your own goal, range is own goal to 9 skewed towards the median.
	 * @param yardline the current field position of this team
	 * @return the total yards gained
	 */
	public int getYardsOnStop(int yardline) {
		int yards;
		int maxLoss = Math.min(5, 100 - yardline);
		yards = rand.nextInt(5 + maxLoss / 2 + maxLoss % 2) + rand.nextInt(6 + maxLoss / 2) - maxLoss;
		return yards;
	}

	/**
	 * Equals method checks if the names of the two teams are identical.
	 * @param o object to be compared
	 * @return true if the given object is a DownSeriesFootballTeam with the same name
	 */
	public boolean equals(Object o) {
		if (o instanceof DownSeriesFootballTeam) {
			DownSeriesFootballTeam otherTeam = (DownSeriesFootballTeam) o;
			return name.equals(otherTeam.getName());
		} else {
			return false;
		}
	}

	/**
	 * toString override
	 * @return the name of this team
	 */
	public String toString() {
		return getName();
	}
}
