package nbpapi;

import static org.junit.Assert.*;

import org.junit.Test;

public class NBPTest {

	@org.junit.Test
	public void testCheckDates() {
		Main main = new Main();
		boolean f1 = main.checkDates("2017-01-01", "2017-01-02");
		assertTrue(f1);
		boolean f2 = main.checkDates("2017-01-02", "2017-01-01");
		assertFalse(f2);
		boolean f3 = main.checkDates("2017-01-01", "2017-01-01");
		assertTrue(f1);
	}
	
	@org.junit.Test
	public void testGetNewEnd() {
		Main main = new Main();
		String m1 = main.getNewEnd("2017-01-01");
		assertEquals("2017-04-03", m1);
		String m2 = main.getNewEnd("2017-08-23");
		assertEquals("2017-11-22", m2);
		String m3 = main.getNewEnd("2017-12-03");
		assertEquals("2018-03-05", m3);
	}
	@org.junit.Test
	public void testGetNewStart() {
		Main main = new Main();
		String m1 = main.getNewStart("2017-01-01");
		assertEquals("2017-01-02", m1);
		String m2 = main.getNewStart("2017-12-31");
		assertEquals("2018-01-01", m2);
		String m3 = main.getNewStart("2017-02-28");
		assertEquals("2017-03-01", m3);
	}
	@org.junit.Test
	public void testGetNewYear() {
		Main main = new Main();
		String m1 = main.getNewYear("2017-01-01");
		assertEquals("2018-01-02", m1);
		String m2 = main.getNewYear("2015-02-28");
		assertEquals("2016-02-29", m2);
		String m3 = main.getNewYear("2017-12-03");
		assertEquals("2018-12-04", m3);
	}
	@org.junit.Test
	public void testNumberOfDays(){
		Main main = new Main();
		long i1 = main.numberOfDays("2017-01-01", "2017-12-31");	
		assertEquals(364, i1);
		long i2 = main.numberOfDays("2017-01-01", "2018-01-01");
		assertEquals(365, i2);
		long i3 = main.numberOfDays("2018-01-01", "2018-01-01");
		assertEquals(0, i3);
	}
	
}
