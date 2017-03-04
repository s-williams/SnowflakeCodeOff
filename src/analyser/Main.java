package analyser;

import java.util.Objects;
import java.util.Scanner;

/**
 * Main
 * @author Scott Williams
 * @since 2017/03/04
 */
public class Main {

    /**
     * Main
     * @param args arguments
     */
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            Data data = new Data("beeTester");

            System.out.println("Recreate database? Y/n");
            if (Objects.equals(scanner.next().toLowerCase(), "y")) {
                System.out.println("Recreating database");
                data.createDatabase();
                data.addData();
            } else {
                System.out.println("Database not recreated");
            }

            System.out.println("Finding similar records");
            data.findRecordPairs();
            System.out.println("Size of recordPair set: " + data.getRecordPairSize());

            System.out.println("Finding flights");
            data.findFlights();
            System.out.println("Number of flights:" + data.getFlightSetSize());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
