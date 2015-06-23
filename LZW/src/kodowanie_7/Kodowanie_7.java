package kodowanie_7;

import java.io.File;
import java.io.IOException;
/**
 * @author kgb_putin
 */
public class Kodowanie_7 {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/kgb/test.mtf");
        File file2 = new File("/Users/kgb/test.lzw");      
        File file3 = new File("/Users/kgb/cokolwiek"); 
        LZW.kompresor(file, file2);
        //LZW.dekompresor(file2,file3);  
    } 
}
