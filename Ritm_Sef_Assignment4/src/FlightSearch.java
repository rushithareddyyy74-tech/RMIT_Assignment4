import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BooleanSupplier;


public class FlightSearch {

	public static void main(String[] args) {

	}

	public FlightSearch(String departureDate, String departureAirportCode, boolean emergencyRowSeating,
			String returnDate, String destinationAirportCode, String seatingClass, int adultPassengerCount,
			int childPassengerCount, int infantPassengerCount) {
		super();
		this.departureDate = departureDate;
		this.departureAirportCode = departureAirportCode;
		this.emergencyRowSeating = emergencyRowSeating;
		this.returnDate = returnDate;
		this.destinationAirportCode = destinationAirportCode;
		this.seatingClass = seatingClass;
		this.adultPassengerCount = adultPassengerCount;
		this.childPassengerCount = childPassengerCount;
		this.infantPassengerCount = infantPassengerCount;
	}
	// TODO Auto-generated method stub

	public FlightSearch() {
		// TODO Auto-generated constructor stub
	}

	private String departureDate;
	private String departureAirportCode;
	private boolean emergencyRowSeating;
	private String returnDate;
	private String destinationAirportCode;
	private String seatingClass;
	private int adultPassengerCount;
	private int childPassengerCount;
	private int infantPassengerCount;

	public boolean runFlightSearch(String departureDate, String departureAirportCode, boolean emergencyRowSeating,
			String returnDate, String destinationAirportCode, String seatingClass, int adultPassengerCount,
			int childPassengerCount, int infantPassengerCount) {

		int totalPassengers = adultPassengerCount + childPassengerCount + infantPassengerCount;
		if (totalPassengers < 1 || totalPassengers > 9)
			return false;

		if ((childPassengerCount > 0 && (emergencyRowSeating || "first".equals(seatingClass)))
				|| (infantPassengerCount > 0 && (emergencyRowSeating || "business".equals(seatingClass)))) {
			return false;
		}

		if (childPassengerCount > adultPassengerCount * 2)
			return false;
		if (infantPassengerCount > adultPassengerCount)
			return false;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		Date depDate, retDate;
		try {
			depDate = sdf.parse(departureDate);
			retDate = sdf.parse(returnDate);
			if (depDate.before(new Date()))
				return false;
		} catch (ParseException e) {
			return false;
		}

		if (retDate.before(depDate))
			return false;

		if (!isValidSeatingClass(seatingClass))
			return false;
		if (emergencyRowSeating && !"economy".equals(seatingClass))
			return false;
		if (!isValidAirport(departureAirportCode) || !isValidAirport(destinationAirportCode)
				|| departureAirportCode.equals(destinationAirportCode)) {
			return false;
		}

		// All validations passed — initialize attributes
		this.departureDate = departureDate;
		this.departureAirportCode = departureAirportCode;
		this.emergencyRowSeating = emergencyRowSeating;
		this.returnDate = returnDate;
		this.destinationAirportCode = destinationAirportCode;
		this.seatingClass = seatingClass;
		this.adultPassengerCount = adultPassengerCount;
		this.childPassengerCount = childPassengerCount;
		this.infantPassengerCount = infantPassengerCount;

		return true;
	}

	private boolean isValidSeatingClass(String cls) {
		return "economy".equals(cls) || "premium economy".equals(cls) || "business".equals(cls) || "first".equals(cls);
	}

	private boolean isValidAirport(String code) {
		String[] validCodes = { "syd", "mel", "lax", "cdg", "del", "pvg", "doh" };
		for (String c : validCodes) {
			if (c.equals(code))
				return true;
		}
		return false;
	}

	public BooleanSupplier runFlightSearch(String string, String string2, boolean b, String string3) {
		// TODO Auto-generated method stub
		return null;
	}

}
