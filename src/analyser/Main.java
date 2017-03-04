package analyser;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Main
 * @author Scott Williams
 * @since 2017/03/04
 */
public class Main {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("What database to create/edit?");
            Data data = new Data(scanner.next());

            System.out.println("Recreate database? Y/n");
            if (Objects.equals(scanner.next().toLowerCase(), "y")) {
                System.out.println("Recreating database");
                data.createDatabase();
                data.addData();
            } else {
                System.out.println("Database not recreated");
            }

            System.out.println("Finding flights");
            data.findFlights();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
