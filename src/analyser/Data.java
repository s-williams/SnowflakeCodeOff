package analyser;

import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Data model
 * @author Scott Williams
 * @since 2017/03/04
 */
public class Data {

    private Connection connection;
    private String source;

    private Set<Flight> flightSet;
    private Set<RecordPair> recordPairSet;
    private ArrayList<RecordPair> recordPairList;

    /**
     * Constructor
     * @param source the .csv source file name
     * @throws SQLException
     */
    public Data(String source) throws SQLException {
        this.source = source;
        connection = DriverManager.getConnection("jdbc:sqlite:"+ source + ".db");

        flightSet = new HashSet<Flight>();
        recordPairSet = new HashSet<RecordPair>();
        recordPairList = new ArrayList<RecordPair>();
    }

    /**
     * Creates the database loading records from the source file
     * @throws SQLException
     */
    public void createDatabase() throws SQLException {

        Statement stat = connection.createStatement();
        stat.executeUpdate("DROP TABLE IF EXISTS records;");
        stat.executeUpdate("CREATE TABLE records (TIMESTAMP, " +
                "                                     airframe_registration, " +
                "                                     content_airframe_type_icao_value, " +
                "                                     airframe_wtc," +
                "                                     arrival_aerodrome_actual_icao_value, " +
                "                                     arrival_aerodrome_scheduled_icao_value, " +
                "                                     arrival_time_inBlock_actual, " +
                "                                     arrival_time_landing_estimated, " +
                "                                     arrival_time_landing_scheduled, " +
                "                                     carrier_icao_quality, " +
                "                                     carrier_icao_value, " +
                "                                     departure_aerodrome_actual_icao_value, " +
                "                                     departure_aerodrome_scheduled_icao_value, " +
                "                                     departure_time_offBlock_estimated, " +
                "                                     departure_time_takeoff_actual, " +
                "                                     departure_time_takeoff_planned, " +
                "                                     departure_time_takeoff_scheduled, " +
                "                                     gufi, " +
                "                                     identification_callsign_quality, " +
                "                                     identification_callsign_value, " +
                "                                     identification_iataFlightNumber_quality, " +
                "                                     identification_iataFlightNumber_value, " +
                "                                     identification_squawk, " +
                "                                     movement_altitude_gps, " +
                "                                     movement_direction_heading, " +
                "                                     movement_position_lat, " +
                "                                     movement_position_lon, " +
                "                                     movement_speed_ground, " +
                "                                     movement_timestamp, " +
                "                                     movement_type, " +
                "                                     route_cruisingAltitude, " +
                "                                     route_cruisingSpeed, " +
                "                                     route_initialFlightRules, " +
                "                                     route_text, " +
                "                                     status);");
    }

    /**
     * Adds all data from a certain csv file to the SQL database
     * @throws Exception
     */
    public void addData() throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/" + source + ".csv"))) {
            String line;
            int j = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineParts = line.split(",");
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT into records VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                for (int i = 0; i < lineParts.length; i++) {
                    preparedStatement.setString(i + 1, lineParts[i]);
                }
                preparedStatement.addBatch();

                connection.setAutoCommit(false);
                preparedStatement.executeBatch();
                connection.setAutoCommit(true);

                j++;
                if (j%50 == 0) System.out.println(j + " lines added");
            }
        }
    }

    /**
     * Reads data from the database for testing purposes
     * @throws SQLException
     */
    public void readData() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM records;");
        while (resultSet.next()) {
            System.out.println("Timestamp = " + resultSet.getString("TIMESTAMP"));
            System.out.println("Airframe = " + resultSet.getString("content_airframe_type_icao_value"));

        }
    }

    /**
     * Finds records with similar characteristics and assumes they are the same flight
     * @throws SQLException
     */
    public void findRecordPairs() throws SQLException {
        Statement firstStatement = connection.createStatement();

        ResultSet firstLoop = firstStatement.executeQuery("SELECT * FROM records");

        //Compare every flight record with every other flight record
        while (firstLoop.next()) {
            Statement secondStatement = connection.createStatement();
            ResultSet secondLoop = secondStatement.executeQuery("SELECT * FROM records");

            while (secondLoop.next()) {
                RecordPair recordPair = new RecordPair(firstLoop.getString("TIMESTAMP"), secondLoop.getString("TIMESTAMP"));

                //Check if airframe registrations are the same
                if (Objects.equals(firstLoop.getString("airframe_registration"), secondLoop.getString("airframe_registration")) ||
                        Objects.equals(firstLoop.getString("airframe_registration"), "") ||
                        Objects.equals("", secondLoop.getString("airframe_registration"))) {
                    recordPair.addValue(1);
                }

                //Check if airframes are the same
                if (Objects.equals(firstLoop.getString("content_airframe_type_icao_value"), secondLoop.getString("content_airframe_type_icao_value")) ||
                        Objects.equals(firstLoop.getString("content_airframe_type_icao_value"), "") ||
                        Objects.equals("", secondLoop.getString("content_airframe_type_icao_value"))) {
                    recordPair.addValue(1);
                }

                //Check if departures destination is same
                if (Objects.equals(firstLoop.getString("departure_aerodrome_actual_icao_value"), secondLoop.getString("departure_aerodrome_actual_icao_value")) ||
                        Objects.equals(firstLoop.getString("departure_aerodrome_scheduled_icao_value"), secondLoop.getString("departure_aerodrome_actual_icao_value")) ||
                        Objects.equals(firstLoop.getString("departure_aerodrome_actual_icao_value"), secondLoop.getString("departure_aerodrome_scheduled_icao_value")) ||
                        Objects.equals(firstLoop.getString("departure_aerodrome_scheduled_icao_value"), secondLoop.getString("departure_aerodrome_scheduled_icao_value")) ||
                        Objects.equals(firstLoop.getString("departure_aerodrome_actual_icao_value"), "") ||
                        Objects.equals(firstLoop.getString("departure_aerodrome_scheduled_icao_value"), "") ||
                        Objects.equals("", secondLoop.getString("departure_aerodrome_actual_icao_value")) ||
                        Objects.equals("", secondLoop.getString("departure_aerodrome_scheduled_icao_value"))) {
                    recordPair.addValue(1);
                }

                //Check if arrival destination is same
                if (Objects.equals(firstLoop.getString("arrival_aerodrome_actual_icao_value"), secondLoop.getString("arrival_aerodrome_actual_icao_value")) ||
                        Objects.equals(firstLoop.getString("arrival_aerodrome_scheduled_icao_value"), secondLoop.getString("arrival_aerodrome_scheduled_icao_value")) ||
                        Objects.equals(firstLoop.getString("arrival_aerodrome_actual_icao_value"), secondLoop.getString("arrival_aerodrome_scheduled_icao_value")) ||
                        Objects.equals(firstLoop.getString("arrival_aerodrome_scheduled_icao_value"), secondLoop.getString("arrival_aerodrome_scheduled_icao_value")) ||
                        Objects.equals(firstLoop.getString("arrival_aerodrome_actual_icao_value"), "") ||
                        Objects.equals(firstLoop.getString("arrival_aerodrome_scheduled_icao_value"), "") ||
                        Objects.equals("", secondLoop.getString("arrival_aerodrome_actual_icao_value")) ||
                    Objects.equals("", secondLoop.getString("arrival_aerodrome_scheduled_icao_value"))) {
                    recordPair.addValue(1);
                }

                //Check if departures time planned is same
                if (Objects.equals(firstLoop.getString("departure_time_takeoff_planned"), secondLoop.getString("departure_time_takeoff_planned")) ||
                        Objects.equals(firstLoop.getString("departure_time_takeoff_planned"), "") ||
                        Objects.equals("", secondLoop.getString("departure_time_takeoff_planned"))) {
                    recordPair.addValue(1);
                }

                //Check if departures time scheduled is same
                if (Objects.equals(firstLoop.getString("departure_time_takeoff_scheduled"), secondLoop.getString("departure_time_takeoff_scheduled")) ||
                        Objects.equals(firstLoop.getString("departure_time_takeoff_scheduled"), "") ||
                        Objects.equals("", secondLoop.getString("departure_time_takeoff_scheduled"))) {
                    recordPair.addValue(1);
                }

                //Check if arrival time estimated is same
                if (Objects.equals(firstLoop.getString("arrival_time_landing_estimated"), secondLoop.getString("arrival_time_landing_estimated")) ||
                        Objects.equals(firstLoop.getString("arrival_time_landing_estimated"), "") ||
                        Objects.equals("", secondLoop.getString("arrival_time_landing_estimated"))) {
                    recordPair.addValue(1);
                }

                //Check if arrival time scheduled is same
                if (Objects.equals(firstLoop.getString("arrival_time_landing_scheduled"), secondLoop.getString("arrival_time_landing_scheduled")) ||
                        Objects.equals(firstLoop.getString("arrival_time_landing_scheduled"), "") ||
                        Objects.equals("", secondLoop.getString("arrival_time_landing_scheduled"))) {
                    recordPair.addValue(1);
                }

                //Add it to the set of reasonablely close pairs
                if (recordPair.getValue() > 7) {
                    recordPairSet.add(recordPair);
                }
            }
            secondLoop.close();
        }

        firstLoop.close();
    }

    /**
     * Finds the appropiate flights from the record pairs
     * @throws SQLException
     */
    public void findFlights() throws SQLException {
        for (RecordPair recordPair : recordPairSet) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM records WHERE TIMESTAMP = ?");
            statement.setString(1, recordPair.getRecordTimeStamp1());
            ResultSet resultSet = statement.executeQuery();

            for (Flight flight : flightSet) {
                if (!Objects.equals(flight, new Flight(resultSet.getString("content_airframe_type_icao_value"),resultSet.getString(""),resultSet.getString("arrival_aerodrome_actual_icao_value"),resultSet.getString("departure_time_takeoff_scheduled"), resultSet.getString("arrival_time_landing_estimated")))) {
                    flightSet.add(new Flight(resultSet.getString("content_airframe_type_icao_value"),resultSet.getString(""),resultSet.getString("arrival_aerodrome_actual_icao_value"),resultSet.getString("departure_time_takeoff_scheduled"), resultSet.getString("arrival_time_landing_estimated")));
                    break;
                }
            }

        }
    }

    /**
     * Returns the size of the flight set
     * @return the size of the flight set
     */
    public int getFlightSetSize() {
        return flightSet.size();
    }

    /**
     * Returns the size of the record pair set
     * @return the size of the record pair set
     */
    public int getRecordPairSize() {
        return recordPairSet.size();
    }

    /**
     * Gets the size of the record pair set after taking into account the minimum value wanted of record pair similarity
     * @param n minimum value wanted of record pair similarity
     * @return the size of the record pair set where every pair has value greater than n
     */
    public int getRecordPairSizeOverValue(int n) {
        Set<RecordPair> likelyPairs = new HashSet<RecordPair>();
        for (RecordPair recordPair : recordPairSet) {
            if (recordPair.getValue() > n) likelyPairs.add(recordPair);
        }
        return likelyPairs.size();
    }
}
