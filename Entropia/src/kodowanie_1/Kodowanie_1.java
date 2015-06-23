package kodowanie_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.log;

public class Kodowanie_1 {
    
    static int  [] licznik = new int[256];
    static int [][] licznik_war=new int[256][256];
    static File file;
 
    public static void main(String[] args) throws IOException {
        file = new File("/Users/kgb/test.txt");
        byte[] b_arr = b_to_arr(file);
        praw(b_arr);
        double ent = ent();
        double con_ent = con_ent();
        System.out.println("Entropia "+ent);
        System.out.println("Entropia warunkowa "+con_ent);  
        System.out.println("Roznica "+(ent-con_ent));
    }                        
    
    private static byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        byte[] b_arr = new byte[(int)file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(b_arr);
            fis.close();
        }
        return b_arr;
    }
    
    private static void praw(byte[] b_arr){
        byte b1,b2;
        for(int i=0;i<b_arr.length;i++){
            licznik[Byte.toUnsignedInt(b_arr[i])]++;
            b1 = b_arr[i];
            if(i==0)
                b2 = Byte.parseByte("97");
            else
                b2 = b_arr[i-1];
            licznik_war[Byte.toUnsignedInt(b2)][Byte.toUnsignedInt(b1)]++;
        }
    }
    private static double ent(){
        double ent =0;
        for(int i=0;i<256;i++){
            double p = (double)licznik[i]/ file.length();
            if (p>0) 
                ent -= (double) p*(log(p)/log(2)); 
        }
        return ent;
    }
    private static double con_ent(){
        double p,temp,con_ent;
        con_ent =0;
        for(int i=0;i<256;i++){
            p = 0;
            if(licznik[i]>0){
                for(int j=0;j<256;j++){
                    if(licznik_war[i][j]>0){
                        temp = (double)licznik_war[i][j]/(double)licznik[i];
                        p-= temp*(log(temp)/log(2));
                    }
                }
                con_ent+=((double)licznik[i]/file.length())*p;
            }
        }
        return con_ent;
    }
}
