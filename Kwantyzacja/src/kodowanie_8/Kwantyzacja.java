package kodowanie_8;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import java.util.ArrayList;
/**
 *
 * @author kgb
 */
public class Kwantyzacja {
    private OutputStream output;
    private int  [] licznik = new int[256];
    private ArrayList<String> mse,snr;
    public void obraz(File in,File out,int r,int g,int b) throws FileNotFoundException, IOException{
        mse = new ArrayList<>();
        snr = new ArrayList<>();
        output = new BufferedOutputStream(new FileOutputStream(out));
        
        double r_p = (256/Math.pow(2, r));
        double g_p = (256/Math.pow(2, g));
        double b_p = (256/Math.pow(2, b));
        byte by[]=b_to_arr(in);
        praw(by);
        System.out.println("Entropia in = "+ent(in));
        for(int i=0;i<by.length;i++){           
            if((i>=18)&&(i<by.length-26)){
                int r_v = Byte.toUnsignedInt(by[i+2]);            
                int g_v = Byte.toUnsignedInt(by[i+1]);
                int b_v = Byte.toUnsignedInt(by[i]);
                
                output.write((int)(b_v/b_p)*(int)b_p+(int)(b_p/2));
                output.write((int)(g_v/g_p)*(int)g_p+(int)(g_p/2));
                output.write((int)(r_v/r_p)*(int)r_p+(int)(r_p/2));
                i+=2;
            }
            else{
                output.write(by[i]);
            }
        }
        output.close();
        byte[]b2 = b_to_arr(out);
        praw(b2);
        System.out.println("Entropia out = "+ent(out)+"\n");
        for(int i=0;i<4;i++)
            mse(by,b2,i);
        for (String mse1 : mse) 
            System.out.println(mse1);
        System.out.println();
        for (String snr1 : snr) 
            System.out.println(snr1);      
    }
  
    private void mse(byte[]b1,byte[]b2,int x) throws IOException{
        double  peak, signal, noise, mse;
        String color;
        int r1,r2;
        signal = noise = 0;
        for(int i=0;i<b1.length;i++){ 
            if((i>=18)&&(i<b1.length-26)){
                if(x!=3){
                    r1 = Byte.toUnsignedInt(b1[i+x]);            
                    r2 = Byte.toUnsignedInt(b2[i+x]);  
                }
                else{
                    r1 = Byte.toUnsignedInt(b1[i]);            
                    r2 = Byte.toUnsignedInt(b2[i]);  
                }
                signal+=r1*r2;
                noise += (r1-r2)*(r1-r2);
                if(x!=3)
                    i+=2;
            }
        }
        if(x==0)
            color ="b";
        else if(x==1)
            color ="g";
        else
            color="r";
        if(x!=3){
            mse = noise/((b1.length-44)/3);
            this.mse.add("mse("+color+")= " + mse);
            this.snr.add("snr("+color+")= " +(signal/noise)+ " ("+10*log10(signal/noise)+"dB)");
        }
        else{
            mse = noise/((b1.length-44));
            this.mse.add("mse = " + mse);
            this.snr.add("snr = " +(signal/noise)+ "("+10*log10(signal/noise)+"dB)");
        }    
    }
  
    private  byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        byte [] arr = new byte[(int)file.length()];
        try(FileInputStream fis = new FileInputStream(file)){
            fis.read(arr);
            fis.close();
        }
        return arr;
    }  
    private double ent(File file){
        double ent =0;
        for(int i=0;i<256;i++){
            double p = (double)licznik[i]/ file.length();
            if (p>0) 
                ent -= (double) p*(log(p)/log(2)); 
        }
        return ent;
    }
    private void praw(byte[] b_arr){
        licznik = new int[256];
        for(int i=0;i<b_arr.length;i++){
            licznik[Byte.toUnsignedInt(b_arr[i])]++;
        }
    }
}
