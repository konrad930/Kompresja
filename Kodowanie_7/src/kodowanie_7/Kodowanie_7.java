package kodowanie_7;

import java.io.File;
import java.io.IOException;
/**
 *
 * @author kgb
 */
public class Kodowanie_7 {
    public static void main(String[] args) throws IOException {
        File file1 = new File("/Users/kgb/test.txt");
        JPEGLS ls = new JPEGLS();
        ls.liczarka(file1);   
        ls.schemat();
    }
}
