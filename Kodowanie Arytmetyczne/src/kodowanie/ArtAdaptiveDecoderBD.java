package kodowanie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
/**
 * @author kgb
 */
public class ArtAdaptiveDecoderBD extends ArtAdaptive{
    private String str="";
    BigDecimal liczba;
    private final int buffer = 128;
    private byte[] by;
    private OutputStream output;
    private InputStream input;
    boolean dalej = true;
    private int currentByte,nextBits,numBitsRemaining;
    boolean isEndOfStream=false;
    
    public ArtAdaptiveDecoderBD(){
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
        int count =0;    
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
                int p = liczba.compareTo(new BigDecimal(nhigh));
                int q = liczba.compareTo(new BigDecimal(nlow));
                if((q==0||q==1)&&(p==-1)){
                    low=nlow;
                    high=nhigh;                                     
                    count++;
                    if(i==256){dalej=false;}
                    else{
                        output.write(i);  
                    }                 
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
    private BigDecimal str_to_d(String s,int k){
        BigDecimal  result= new BigDecimal(0);
        for(int i=1+k;i<=buffer+k;i++){
            if(s.charAt(i-1)=='1'){
                result.add(new BigDecimal(Math.pow(2, -i)));
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
            else if(low>=0.5){
                low-=0.5;
                high-=0.5;
                liczba.subtract(new BigDecimal(0.5));
            }
            else if((low>=0.25)&&(high<0.75)){
                low -=0.25;
                high -=0.25;
                liczba.subtract(new BigDecimal(0.25));
            }
            else{     
                break;
            }
            liczba.multiply(new BigDecimal(2));
            if(bit()==1)
                liczba.add(new BigDecimal(Math.pow(2, -buffer)));
            low*=2;
            high*=2;
        }      
    }
}
