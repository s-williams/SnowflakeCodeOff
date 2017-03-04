package analyser;

/**
 * Models a flight
 * @author Scott Williams
 * @since 2017/03/04
 */
public class Flight {

    private String plane;
    private String departureDestination;
    private String arrivalDestination;
    private String departureTime;
    private String arrivalTime;

    /**
     * Constructor
     * @param plane the model of the plane used in the flight
     * @param departureDestination the aerodrome the flight is travelling from
     * @param arrivalDestination the aerodrome the flight is travelling to
     * @param departureTime the time the flight left
     * @param arrivalTime the time the flight arrives at its destination
     */
    public Flight(String plane, String departureDestination, String arrivalDestination, String departureTime, String arrivalTime) {
        this.plane = plane;
        this.departureDestination = departureDestination;
        this.arrivalDestination = arrivalDestination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    /**
     * Returns a string of the flight details
     * @return a string of the flight details
     */
    public String getFlightDetails() {
        return "Model " + plane + ", going from " + departureDestination + " to " + arrivalDestination + ", leaving at " + departureTime + ", and arriving at " + arrivalTime;
    }
}
