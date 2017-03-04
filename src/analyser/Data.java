package analyser;

import java.io.*;
import java.sql.*;

/**
 * Data model
 * @author Scott Williams
 * @since 2017/03/04
 */
public class Data {

    private Connection connection;
    private String source;


    /**
     * Constructor
     * @param source the .csv source file name
     * @throws SQLException
     */
    public Data(String source) throws SQLException {
        this.source = source;
        connection = DriverManager.getConnection("jdbc:sqlite:"+ source + ".db");

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
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(source + ".csv"))) {
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
        //statement.execute("SELECT * FROM records");

        ResultSet resultSet = statement.executeQuery("SELECT * FROM records;");
        while (resultSet.next()) {
            System.out.println("Timestamp = " + resultSet.getString("TIMESTAMP"));
            System.out.println("Airframe = " + resultSet.getString("content_airframe_type_icao_value"));

        }
    }
}
