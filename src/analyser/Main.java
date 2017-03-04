package analyser;

import java.io.File;
import java.sql.SQLException;

/**
 * Main
 * @author Scott Williams
 * @since 2017/03/04
 */
public class Main {

    public static void main(String[] args) {
        try {
            Data data = new Data("beeEdit");

//            data.createDatabase();
//            data.addData(new File("resources/beeEdit.csv"));

            data.readData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
