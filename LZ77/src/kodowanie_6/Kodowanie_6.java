package kodowanie_6;

import java.io.File;
import java.io.IOException;
/**
 *
 * @author kgb
 */
public class Kodowanie_6 {
    public static void main(String[] args) throws IOException {       
       File file = new File("/Users/kgb/test.lzw");
       File file2 = new File("/Users/kgb/test.lz77");      
       File file3 = new File("/Users/kgb/cokolwiek.txt"); 
       
       LZ77 lz77 = new LZ77();
       lz77.kompresja(file,file2);
       lz77.dekompresja(file2, file3);
    }      
}
