package kodowanie_3;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.List;

public class HuffmanDEncoder {
    
    private OutputStream output;
    private HuffmanDTree ht;
    private int currentByte,numBitsInCurrentByte;
    static int  [] licznik = new int[256];
 
    public void kompresja(File in,File out) throws IOException{   
        output = new BufferedOutputStream(new FileOutputStream(out));
        ht = new HuffmanDTree(in);
        byte[] by=ht.b_to_arr(in);
        praw(by);
        System.out.println("Entropia in : "+ent(in));
        List<Integer> bits;
        int count=0;
        for(int i=0;i<by.length;i++){
            bits = ht.getCode(Byte.toUnsignedInt(by[i]));  
            for(int b : bits){
                write(b);
            }   
            count++;
            ht.nodes.get(Byte.toUnsignedInt(by[i])).licznik++;    
            if (power2(count)){  
                ht.buildTree(); 
                ht.inOrder(ht.node, new ArrayList<>());              
            }
        }
        for(int b : ht.getCode(256)){write(b);}
        close();
        System.out.println("Stopien kompresji : "+(((double)out.length()/(double)in.length())));
    }
    private void write(int b) throws IOException{
        currentByte = (b==1) ? currentByte*2+1 : currentByte*2;
        numBitsInCurrentByte++; 
         if (numBitsInCurrentByte == 8) {
            output.write(currentByte);       
            currentByte=0;
            numBitsInCurrentByte=0;
        }
    }
    private void close() throws IOException {
        while (numBitsInCurrentByte != 0)
            write(0);
        output.close();
    }   
    private static boolean power2(int x) {return x > 0 && (x & -x) == x;}
    
    private static double ent(File file){
        double ent =0;
        for(int i=0;i<256;i++){
            double p = (double)licznik[i]/ file.length();
            if (p>0) 
                ent -= (double) p*(log(p)/log(2)); 
        }
        return ent;
    }
    private static void praw(byte[] b_arr){
        for(int i=0;i<b_arr.length;i++){
            licznik[Byte.toUnsignedInt(b_arr[i])]++;
        }
    }
}
