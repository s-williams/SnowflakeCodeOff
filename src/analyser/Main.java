package analyser;

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

            Data data = new Data("beeEdit");

            System.out.println("Recreate database? Y/n");
            if (Objects.equals(scanner.next().toLowerCase(), "y")) {
                System.out.println("Recreating database");
                data.createDatabase();
                data.addData();
            } else {
                System.out.println("Database not recreated");
            }

            System.out.println("Finding flights");
            data.findRecordPairs();

            System.out.println("Size of recordPair set is: " + data.getRecordPairSize());
            System.out.println("Size of likely flight set is: " + data.getRecordPairSizeOverValue(3));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
