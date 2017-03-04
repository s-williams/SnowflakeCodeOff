package analyser;

/**
 * Creates and stores flightPairs
 * @author Scott Williams
 * @since 2017/03/04
 */
public class RecordPair {

    private String recordTimeStamp1;
    private String recordTimeStamp2;

    private int value;

    /**
     * Constructor for flightPair
     * @param recordTimeStamp1 the first flight of the pair
     * @param recordTimeStamp2 the second flight of the pair
     */
    public RecordPair(String recordTimeStamp1, String recordTimeStamp2) {
        this.recordTimeStamp1 = recordTimeStamp1;
        this.recordTimeStamp2 = recordTimeStamp2;

        value = 0;
    }

    /**
     * Adds an 1 to the value of the flightPair
     */
    public void addValue() {
        addValue(1);
    }

    /**
     * Adds an integer to the value of the flightPair
     * @param i the amount to be added to the value
     */
    public void addValue(int i) {
        value = value + i;
    }

    /**
     * Gets the value of the flight pair
     * @return the value of the flight pair
     */
    public int getValue() {
        return value;
    }

    public String getRecordTimeStamp1() {
        return recordTimeStamp1;
    }
}
