package kodowanie_9;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author kgb_putin
 */
public class Kodowanie_9 {
    private  ArrayList<String> mse,snr;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/kgb/mg.tga");
        File file2 = new File("/Users/kgb/test.r"); 
        File file3 = new File("/Users/kgb/test.tga"); 
         
        new Roznicowe().kodowanie(file,file2,7);
        new Roznicowe().dekodowanie(file2,file3);
        new Roznicowe().mse(file, file3);
        
    }
    
  
    
}
