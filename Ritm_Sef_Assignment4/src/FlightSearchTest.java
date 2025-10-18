import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public  class FlightSearchTest {
	
	
	@Test
    public void testTotalPassengersValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 1, 0, 0));
    }

    @Test
    public void testTotalPassengersInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 0, 0, 0));
    }

    // Condition 2: Children not in emergency or first class
    @Test
    public void testChildSeatingValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 1, 1, 0));
    }

    @Test
    public void testChildSeatingInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("25/12/2025", "mel", true, "30/12/2025", "pvg", "first", 1, 1, 0));
    }

    // Condition 3: Infants not in emergency or business class
    @Test
    public void testInfantSeatingValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 1, 0, 1));
    }

    @Test
    public void testInfantSeatingInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("25/12/2025", "mel", true, "30/12/2025", "pvg", "business", 1, 0, 1));
    }

    // Condition 4: Max 2 children per adult
    @Test
    public void testChildToAdultRatioValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 2, 4, 0));
    }

    @Test
    public void testChildToAdultRatioInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 1, 3, 0));
    }

    // Condition 5: Max 1 infant per adult
    @Test
    public void testInfantToAdultRatioValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 2, 0, 2));
    }

    @Test
    public void testInfantToAdultRatioInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 1, 0, 2));
    }

    // Condition 6: Departure date not in past
    @Test
    public void testDepartureDateValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 1, 0, 0));
    }

    @Test
    public void testDepartureDateInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("01/01/2020", "mel", false, "05/01/2020", "pvg", "economy", 1, 0, 0));
    }

    // Condition 7: Strict date format
    @Test
    public void testDateFormatValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("28/02/2025", "mel", false, "05/03/2025", "pvg", "economy", 1, 0, 0));
    }

    @Test
    public void testDateFormatInvalidLeapYear() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("29/02/2026", "mel", false, "05/03/2026", "pvg", "economy", 1, 0, 0));
    }

    // Condition 8: Return date after departure
    @Test
    public void testReturnDateValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 1, 0, 0));
    }

    @Test
    public void testReturnDateInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("30/12/2025", "mel", false, "25/12/2025", "pvg", "economy", 1, 0, 0));
    }

    // Condition 9: Valid seating class
    @Test
    public void testSeatingClassValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "premium economy", 1, 0, 0));
    }

    @Test
    public void testSeatingClassInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "luxury", 1, 0, 0));
    }

    // Condition 10: Emergency row only in economy
    @Test
    public void testEmergencyRowValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", true, "30/12/2025", "pvg", "economy", 1, 0, 0));
    }

    @Test
    public void testEmergencyRowInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("25/12/2025", "mel", true, "30/12/2025", "pvg", "business", 1, 0, 0));
    }

    // Condition 11: Valid airport codes and not same
    @Test
    public void testAirportCodesValid() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "syd", false, "30/12/2025", "mel", "economy", 1, 0, 0));
    }

    @Test
    public void testAirportCodesInvalid() {
        FlightSearch fs = new FlightSearch();
        assertFalse(fs.runFlightSearch("25/12/2025", "syd", false, "30/12/2025", "syd", "economy", 1, 0, 0));
    }

    // Final Case: All valid inputs (4 variations)
    @Test
    public void testAllValidInputs1() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("25/12/2025", "mel", false, "30/12/2025", "pvg", "economy", 2, 2, 2));
    }

    @Test
    public void testAllValidInputs2() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("01/01/2026", "cdg", false, "10/01/2026", "del", "premium economy", 1, 0, 0));
    }

    @Test
    public void testAllValidInputs3() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("15/03/2026", "lax", false, "25/03/2026", "doh", "business", 3, 0, 1));
    }

    @Test
    public void testAllValidInputs4() {
        FlightSearch fs = new FlightSearch();
        assertTrue(fs.runFlightSearch("10/04/2026", "syd", false, "20/04/2026"));

}
}
