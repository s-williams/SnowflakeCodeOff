package analyser;

import java.util.HashSet;
import java.util.Set;

/**
 * Creates and stores flights
 * @author Scott Williams
 * @since 2017/03/04
 */
public class FlightManager {

    //Stores all known flights
    private Set<Flight> flightSet;

    /**
     * Constructor for the flight manager
     */
    public FlightManager() {
        flightSet = new HashSet<Flight>();
    }

    /**
     * Creates a flight and stores it in the flightSet
     * @param plane
     * @param departureDestination
     * @param arrivalDestination
     * @param departureTime
     * @param arrivalTime
     */
    public void createFlight(String plane, String departureDestination, String arrivalDestination, String departureTime, String arrivalTime) {
        Flight flight = new Flight(plane, departureDestination, arrivalDestination, departureTime, arrivalTime);
        flightSet.add(flight);
    }

    /**
     * Returns the size of the flightSet
     * @return the size of the flightSet
     */
    public int getFlightSetSize() {
        return flightSet.size();
    }

    /**
     * Converts the contents of the flightSet into a string
     * @return the contents of the flightSet after it has been converted into a string
     */
    public String getFlightSetContents() {
        String string = "";
        for (Flight flight : flightSet) {
            string = string + flight.getFlightDetails() + "\n";
        }
        return string;
    }
}
