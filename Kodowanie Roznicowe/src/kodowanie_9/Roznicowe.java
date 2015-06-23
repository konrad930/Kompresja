package kodowanie_9;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Math.log10;
import java.util.ArrayList;

/**
 *
 * @author kgb_putin
 */
public class Roznicowe {
    static double p;
    static byte[] arr ;
    private ArrayList<String> mse,snr;

    public void kodowanie(File in,File out,int k) throws FileNotFoundException, IOException{
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(out))) {
             
            p = (256/Math.pow(2,k));
            arr = b_to_arr(in);     
            double Xr=0,Xg=0,Xb=0,temp;
            for(int i=0;i<arr.length;i++){
                if((i>=18)&&(i<arr.length-26)){   
                    
                    int r_v =Byte.toUnsignedInt(arr[i+2]);
                    temp = (r_v-Xr); 
                    int r =((int)(temp/p)*(int)p);
                    int g_v =Byte.toUnsignedInt(arr[i+1]);
                    temp = (g_v-Xg);                  
                    int g =((int)(temp/p)*(int)p);                   
                    int b_v =Byte.toUnsignedInt(arr[i]);
                    temp = (b_v-Xb);                     
                    int b =((int)(temp/p)*(int)p);
                                      
                    Xr+=r;
                    Xg+=g;
                    Xb+=b;
                
                    output.write(b);
                    output.write(g);
                    output.write(r);
                   
                    i+=2;
                }
                else{
                    output.write(arr[i]);
                }
            }
        }
        
    }
    public void dekodowanie(File in,File out) throws FileNotFoundException, IOException{
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(out))) {
            byte[] arr = b_to_arr(in);
            int Xr=0,Xg=0,Xb=0;
            for(int i=0;i<arr.length;i++){
                if((i>=18)&&(i<arr.length-26)){
                  
                    int r_v = Byte.toUnsignedInt(arr[i+2]);
                    int g_v = Byte.toUnsignedInt(arr[i+1]);                   
                    int b_v = Byte.toUnsignedInt(arr[i]);
            
                    output.write(b_v+Xb+(int)(p/2));
                    output.write(g_v+Xg+(int)(p/2));
                    output.write(r_v+Xr+(int)(p/2));
                                   
                    Xr+=r_v;
                    Xg+=g_v;
                    Xb+=b_v;
                                        
                    i+=2;
                }
                else{
                    output.write(arr[i]);
                }
            }
        }
        
        
    }
    public void mse(File in,File out) throws IOException{
        mse = new ArrayList<>();
        snr = new ArrayList<>();
        byte[]b2 = b_to_arr(out),b1 = b_to_arr(in);
        for(int i=0;i<4;i++)
            mse(b2,b1,i);
        for (String mse1 : mse) 
            System.out.println(mse1);
        System.out.println();
        for (String snr1 : snr) 
            System.out.println(snr1); 
        
    }
    
    private  byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        byte [] arr = new byte[(int)file.length()];
        try(FileInputStream fis = new FileInputStream(file)){
            fis.read(arr);
            fis.close();
        }
        return arr;
    } 
      private void mse(byte[]b1,byte[]b2,int x) throws IOException{
        double  peak, signal, noise, mset;
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
            mset = noise/((b1.length-44)/3);
            this.mse.add("mse("+color+")= " + mset);
            this.snr.add("snr("+color+")= " +(signal/noise)+ " ("+10*log10(signal/noise)+"dB)");
        }
        else{
            mset = noise/((b1.length-44));
            this.mse.add("mse = " + mset);
            this.snr.add("snr = " +(signal/noise)+ "("+10*log10(signal/noise)+"dB)");
        }    
    }

}
