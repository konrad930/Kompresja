package kodowanie_3;

import java.io.File;
import java.io.IOException;

public class Kodowanie_3 {
    /**
     * @param args the command line arguments
     */        
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/kgb/test.lzw");
        File file2 = new File("/Users/kgb/test.huff");      
        File file3 = new File("/Users/kgb/cokolwiek.txt"); 
       
        HuffmanEncoder enc = new HuffmanEncoder();
        HuffmanDecoder dec = new HuffmanDecoder();
         
        HuffmanDEncoder hde = new HuffmanDEncoder();
        HuffmanDDecoder hdd = new HuffmanDDecoder();
        hde.kompresja(file, file2);
      
        //hdd.dekompresja(file2, file3);
   
    }  
               
}
