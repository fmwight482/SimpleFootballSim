package SimpleFootballSim.DownSeriesSim;

import SimpleFootballSim.FootballException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DownSeriesStateTest {
	private DownSeriesState dss;

	@BeforeEach
	void beforeEach() {
		dss = new DownSeriesState("ATL", 45);
	}

	@Test
	void addPoints() {
		assertEquals(0, dss.getCounter());
		assertEquals(0, dss.getPoints());
		dss.addPoints(7);
		assertEquals(1, dss.getCounter());
		assertEquals(7, dss.getPoints());
	}

	@Test
	void arePointsFinal() {
		assertFalse(dss.arePointsFinal());
		dss.setCounter(100000);
		dss.setPoints(250000);
		assertTrue(dss.arePointsFinal());
	}

	@Test
	void testGetExpectedPoints() throws FootballException {
		dss.setCounter(100000);
		dss.setPoints(250000);
		dss.addPoints(3);
		assertEquals(2.50, dss.getExpectedPoints(), 3);
	}

	@Test
	void testInvalidExpectedPoints() {
		dss.addPoints(3);
		assertThrows(FootballException.class, () -> {
			dss.getExpectedPoints();
		});
	}

	@Test
	void testEquals() {
		DownSeriesState dss2 = new DownSeriesState("ATL", 46);
		DownSeriesState dss3 = new DownSeriesState("DRW", 45);
		DownSeriesState dss4 = new DownSeriesState("ATL", 45);

		assertEquals(dss, dss4);
		assertNotEquals(dss, dss2);
		assertNotEquals(dss, dss3);
		assertNotEquals(dss, 5);
	}
}