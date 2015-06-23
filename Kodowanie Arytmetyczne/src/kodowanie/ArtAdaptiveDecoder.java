package kodowanie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
/**
 * @author kgb
 */
public class ArtAdaptiveDecoder extends ArtAdaptive{
    private String str="";
    double liczba;
    private byte[] by;
    private OutputStream output;
    private InputStream input;
    private int currentByte,nextBits,numBitsRemaining,count =0;
    private final int buffer=52;
    boolean isEndOfStream=false,dalej=true;
   
    public ArtAdaptiveDecoder(){
        symbole = new int[256];
        temp = new int[256];
        for(int i=0;i<256;i++){
            symbole[i]=i+1;       
        }
        temp = symbole.clone();
    }
    public void dekompresja(File in,File out) throws IOException{
        output = new BufferedOutputStream(new FileOutputStream(out));
        input = new BufferedInputStream(new FileInputStream(in));
        by = b_to_arr(in);
        byte[] b = new byte[8];
        int symbol = input.read(b);       
        len = ByteBuffer.wrap(b).getLong();
        while(str.length()!=buffer){
            str+=bit();
        }
        liczba = str_to_d(str,0);
        int j=0;        
        while(dalej){  
            przedzial(); 
            double range = high-low;
            for(int i=0;i<256;i++){
                if((count+256)%256==0){
                    j=count+256;
                    symbole = temp.clone();
                }                    
                double suma = (i!=0)?symbole[i-1]:0.0;                
                double nlow = ((suma*range)/(j))+low;
                double licznik = symbole[i];            
                double nhigh = nlow+(((licznik-suma)/j)*range); 
                if((liczba<=nhigh)&&(liczba>=nlow)){              
                    low=nlow;
                    high=nhigh;                                    
                    count++;
                    output.write(i);                   
                    inc(i);
                    break;
                }                
            }
            if(count==len){
                dalej=false;
                System.out.println("Skonczylem dekompresje");
            }    
        }
        input.close();
        output.close();      
    }
    private double str_to_d(String s,int k){
        double  result=0;
        for(int i=1+k;i<=buffer+k;i++){
            if(s.charAt(i-1)=='1'){
                result+=Math.pow(2, -i);
            }
        }
        return result;
    }
    private int bit() throws IOException {
        if (isEndOfStream)
            return -1;
	if (numBitsRemaining == 0) {
            nextBits = input.read();
            if (nextBits == -1) {
                isEndOfStream = true;
                    return -1;
            }
            numBitsRemaining = 8;
	}
	numBitsRemaining--;
	return (nextBits >>> numBitsRemaining) & 1;
    }
    
    private void przedzial() throws IOException{       
        while(true){
            if(high<0.5){               
            }
            else if((low>=0.5)&&(high<1.0)){
                low-=0.5;
                high-=0.5;
                liczba-=0.5;
                continue;
            }
            else if((low>=0.25)&&(high<0.75)){
                low -=0.25;
                high -=0.25;
                liczba-=0.25;
                continue;
            }
            else{
                break;
            }
            liczba*=2.0;
            if(bit()==1)
                liczba +=Math.pow(2,-buffer);
            low*=2.0;
            high*=2.0;
        }      
    }
}
