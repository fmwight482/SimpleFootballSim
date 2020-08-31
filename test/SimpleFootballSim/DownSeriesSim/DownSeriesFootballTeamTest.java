package SimpleFootballSim.DownSeriesSim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.mockito.Mockito.times;

class DownSeriesFootballTeamTest {
	DownSeriesFootballTeam team;

	// mock Random class to get desired number spread
	Random randomNumberMock = Mockito.mock(Random.class);

	@BeforeEach
	void beforeEach() {
		team = new DownSeriesFootballTeam("ATL", 0.67, randomNumberMock);
	}

	@Test
	void testGetYardsOnStopMidfield() {
		team.getYardsOnStop(50);
		Mockito.verify(randomNumberMock, times(2)).nextInt(7);
	}

	@Test
	void getYardsOnStopOwnGoal() {
		team.getYardsOnStop(99);
		Mockito.verify(randomNumberMock, times(2)).nextInt(5);
	}

	@Test
	void getYardsOnStopOpponentGoal() {
		team.getYardsOnStop(1);
		Mockito.verify(randomNumberMock).nextInt(3);
		Mockito.verify(randomNumberMock).nextInt(2);
	}
}