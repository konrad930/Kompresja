package kodowanie_6;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Math.log;
/**
 *
 * @author kgb
 */
public class LZ77{
    private OutputStream output;
    private final int n=255;
    static int  [] licznik = new int[256];
    
    public void kompresja(File in,File out) throws FileNotFoundException, IOException{
        String window="",s="";
        char x;
        output = new BufferedOutputStream(new FileOutputStream(out));
        byte by[]=b_to_arr(in);      
        praw(by);
        System.out.println("Entropia in : "+ent(in));
        for(int i=0;i<by.length;i++){
            x =(char)by[i];
            String temp = window+s;
            if((!temp.contains(s+x))||(s.length()==255)){
                if(s.equals("")){
                    output.write(0);
                    output.write(by[i]);
                    window = grow(window, Character.toString(x));
                }
                else{
                    int k = lookback(window, s);
                    output.write(k);
                    output.write(s.length());
                    window = grow(window, s);
                    s = "";
                    i -= 1;
                }
            }
            else{
                s+=x;
            }
        }
        if (!s.equals("")){
            int k = lookback(window,s); 
            output.write(k);
            output.write(s.length());
        }
        output.close();
        System.out.println("Dlugosc pliku in : "+in.length()/1000+"kB "+(in.length()%1000)+"B");
        System.out.println("Dlugosc pliku out : "+out.length()/1000+"kB "+(out.length()%1000)+"B");
        System.out.println("Stopien kompresji "+(((double)out.length()/(double)in.length())));

    }
    public void dekompresja(File in,File out) throws FileNotFoundException, IOException{
        String result="";
        licznik = new int[256];
        output = new BufferedOutputStream(new FileOutputStream(out));
        byte[]by=b_to_arr(in);
        praw(by);
        System.out.println("Entropia out : "+ent(in));
        for(int i=0;i<by.length;i+=2){
            if(Byte.toUnsignedInt(by[i])==0){
                output.write(by[i+1]);
                result+=(char)by[i+1];          
            }
            else{
                int len = Byte.toUnsignedInt(by[i+1]);
                int p = Byte.toUnsignedInt(by[i]);
                for(int j=0;j<len;j++){
                    output.write(result.charAt(result.length()-p));
                    result+=result.charAt(result.length()-p);
                }                     
            }
            result = grow(result,"");
        }
        output.close();
    }
    private String grow(String window, String x){        
        window+=x;
        if(window.length()>n){
            window=window.substring(window.length()-n, window.length());
        }
        return window;
    }
    private int lookback(String window,String s){
        String temp = window+s;
        return window.length() - temp.indexOf(s);
    }
    
    protected  byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        byte [] arr = new byte[(int)file.length()];
        try(FileInputStream fis = new FileInputStream(file)){
            fis.read(arr);
            fis.close();
        }
        return arr;
    }
    /*
        Do liczenia Entropi etc
    */
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