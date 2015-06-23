package kodowanie;

import java.io.File;
import java.io.IOException;
/**
 *
 * @author kgb
 */
public class Kodowanie {    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {    
       File file = new File("/Users/kgb/test222.txt");
       File file2 = new File("/Users/kgb/test.art");      
       File file3 = new File("/Users/kgb/cokolwiek.txt"); 
      
       ArtAdaptiveEncoder aae = new ArtAdaptiveEncoder();
       aae.kompresja(file, file2);
       ArtAdaptiveDecoder aad = new ArtAdaptiveDecoder();

       aad.dekompresja(file2, file3);
    }
}
