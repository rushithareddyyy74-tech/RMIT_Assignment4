
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FlightSearchTest {
    private FlightSearch fs;

    @BeforeEach
    void setUp() {
        fs = new FlightSearch();
    }

    private void assertDefaultAttributes() {
        assertNull(fs.getDepartureDate());
        assertNull(fs.getDepartureAirportCode());
        assertFalse(fs.isEmergencyRowSeating());
        assertNull(fs.getReturnDate());
        assertNull(fs.getDestinationAirportCode());
        assertNull(fs.getSeatingClass());
        assertEquals(0, fs.getAdultPassengerCount());
        assertEquals(0, fs.getChildPassengerCount());
        assertEquals(0, fs.getInfantPassengerCount());
    }

    // Condition 1: totalPassengers ∈ [1,9]
    @Test
    void condition1_validMinPassengers() {
        boolean ok = fs.runFlightSearch(
            "01/04/2027", "syd", false,
            "05/04/2027", "mel", "economy",
            1, 0, 0
        );
        assertTrue(ok);
        assertEquals("01/04/2027", fs.getDepartureDate());
        assertEquals(1, fs.getAdultPassengerCount());
    }

    @Test
    void condition1_invalidZeroPassengers() {
        boolean ok = fs.runFlightSearch(
            "01/04/2027", "syd", false,
            "05/04/2027", "mel", "economy",
            0, 0, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 2: children not in emergency row or first class
    @Test
    void condition2_validChildSeating() {
        boolean ok = fs.runFlightSearch(
            "10/05/2027", "mel", false,
            "15/05/2027", "pvg", "economy",
            2, 1, 0
        );
        assertTrue(ok);
        assertEquals(1, fs.getChildPassengerCount());
    }

    @Test
    void condition2_invalidChildInFirstOrEmergency() {
        boolean ok = fs.runFlightSearch(
            "10/05/2027", "mel", true,
            "15/05/2027", "pvg", "first",
            2, 1, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 3: infants not in emergency row or business class
    @Test
    void condition3_validInfantSeating() {
        boolean ok = fs.runFlightSearch(
            "20/06/2027", "lax", false,
            "25/06/2027", "doh", "economy",
            2, 0, 1
        );
        assertTrue(ok);
        assertEquals(1, fs.getInfantPassengerCount());
    }

    @Test
    void condition3_invalidInfantInBusinessOrEmergency() {
        boolean ok = fs.runFlightSearch(
            "20/06/2027", "lax", true,
            "25/06/2027", "doh", "business",
            2, 0, 1
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 4: max 2 children per adult
    @Test
    void condition4_validChildToAdultRatio() {
        boolean ok = fs.runFlightSearch(
            "05/07/2027", "cdg", false,
            "10/07/2027", "del", "economy",
            3, 6, 0
        );
        assertTrue(ok);
        assertEquals(6, fs.getChildPassengerCount());
    }

    @Test
    void condition4_invalidTooManyChildren() {
        boolean ok = fs.runFlightSearch(
            "05/07/2027", "cdg", false,
            "10/07/2027", "del", "economy",
            2, 5, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 5: max 1 infant per adult
    @Test
    void condition5_validInfantToAdultRatio() {
        boolean ok = fs.runFlightSearch(
            "12/08/2027", "del", false,
            "18/08/2027", "cdg", "economy",
            2, 0, 2
        );
        assertTrue(ok);
        assertEquals(2, fs.getInfantPassengerCount());
    }

    @Test
    void condition5_invalidTooManyInfants() {
        boolean ok = fs.runFlightSearch(
            "12/08/2027", "del", false,
            "18/08/2027", "cdg", "economy",
            1, 0, 2
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 6: departure date not in the past
    @Test
    void condition6_validFutureDeparture() {
        boolean ok = fs.runFlightSearch(
            "30/12/2027", "pvg", false,
            "05/01/2028", "lax", "economy",
            1, 0, 0
        );
        assertTrue(ok);
        assertEquals("30/12/2027", fs.getDepartureDate());
    }

    @Test
    void condition6_invalidPastDeparture() {
        boolean ok = fs.runFlightSearch(
            "01/01/2020", "pvg", false,
            "05/01/2020", "lax", "economy",
            1, 0, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 7: strict date format & valid calendar
    @Test
    void condition7_validDate() {
        boolean ok = fs.runFlightSearch(
            "28/02/2028", "syd", false,
            "05/03/2028", "mel", "economy",
            1, 0, 0
        );
        assertTrue(ok);
        assertEquals("28/02/2028", fs.getDepartureDate());
    }

    @Test
    void condition7_invalidLeapDate() {
        boolean ok = fs.runFlightSearch(
            "29/02/2027", "syd", false,
            "05/03/2027", "mel", "economy",
            1, 0, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 8: return date after departure
    @Test
    void condition8_validReturnAfterDeparture() {
        boolean ok = fs.runFlightSearch(
            "10/08/2027", "cdg", false,
            "15/08/2027", "del", "economy",
            1, 0, 0
        );
        assertTrue(ok);
        assertEquals("15/08/2027", fs.getReturnDate());
    }

    @Test
    void condition8_invalidReturnBeforeDeparture() {
        boolean ok = fs.runFlightSearch(
            "15/08/2027", "cdg", false,
            "10/08/2027", "del", "economy",
            1, 0, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 9: valid seating class
    @Test
    void condition9_validClass() {
        boolean ok = fs.runFlightSearch(
            "01/09/2027", "lax", false,
            "05/09/2027", "doh", "premium economy",
            1, 0, 0
        );
        assertTrue(ok);
        assertEquals("premium economy", fs.getSeatingClass());
    }

    @Test
    void condition9_invalidClass() {
        boolean ok = fs.runFlightSearch(
            "01/09/2027", "lax", false,
            "05/09/2027", "doh", "luxury",
            1, 0, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 10: emergency row only in economy
    @Test
    void condition10_validEmergencyInEconomy() {
        boolean ok = fs.runFlightSearch(
            "20/11/2027", "mel", true,
            "25/11/2027", "pvg", "economy",
            1, 0, 0
        );
        assertTrue(ok);
        assertTrue(fs.isEmergencyRowSeating());
    }

    @Test
    void condition10_invalidEmergencyInNonEconomy() {
        boolean ok = fs.runFlightSearch(
            "20/11/2027", "mel", true,
            "25/11/2027", "pvg", "business",
            1, 0, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Condition 11: valid airports & not identical
    @Test
    void condition11_validAirports() {
        boolean ok = fs.runFlightSearch(
            "01/10/2027", "del", false,
            "07/10/2027", "cdg", "economy",
            1, 0, 0
        );
        assertTrue(ok);
        assertEquals("del", fs.getDepartureAirportCode());
        assertEquals("cdg", fs.getDestinationAirportCode());
    }

    @Test
    void condition11_invalidSameAirport() {
        boolean ok = fs.runFlightSearch(
            "01/10/2027", "del", false,
            "07/10/2027", "del", "economy",
            1, 0, 0
        );
        assertFalse(ok);
        assertDefaultAttributes();
    }

    // Four “all-valid” combinations

    @Test
    void allValidCombo1() {
        assertTrue(fs.runFlightSearch(
            "25/12/2027", "mel", false,
            "30/12/2027", "pvg", "economy",
            2, 2, 2
        ));
    }

    @Test
    void allValidCombo2() {
        assertTrue(fs.runFlightSearch(
            "01/01/2028", "cdg", true,
            "10/01/2028", "del", "economy",
            1, 0, 0
        ));
    }

    @Test
    void allValidCombo3() {
        // Changed from "business" → "economy" so infants are allowed
        assertTrue(fs.runFlightSearch(
            "15/03/2027", "lax", false,
            "25/03/2027", "doh", "economy",
            3, 0, 1
        ));
    }

    @Test
    void allValidCombo4() {
        assertTrue(fs.runFlightSearch(
            "05/05/2027", "syd", false,
            "12/05/2027", "lax", "premium economy",
            4, 4, 0
        ));
    }
}
