package SimpleFootballSim;

public class DriveResult {
	public int scores;
	public int points;

	public DriveResult() {
		scores = 0;
		points = 0;
	}

	public void addScore(double pointsScored) {
		scores++;
		points += pointsScored;
	}
}
