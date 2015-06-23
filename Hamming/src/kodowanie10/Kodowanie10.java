package kodowanie10;

import java.io.File;
import java.io.IOException;

public class Kodowanie10 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/kgb/test1");
        File file2 = new File("/Users/kgb/test22.e"); 
        File file4 = new File("/Users/kgb/test22.es"); 
        File file3 = new File("/Users/kgb/test333"); 
        new Hamming().koder(file, file2);
        
        //new Hamming().szum(10, file2, file4);
        new Hamming().dekoder(file2, file3);
        //int a = new Hamming().sprawdz(file2, file3);
        //System.out.println(a);
     
    }
    private static boolean power2(int x) {return x > 0 && (x & -x) == x;}
    
}
