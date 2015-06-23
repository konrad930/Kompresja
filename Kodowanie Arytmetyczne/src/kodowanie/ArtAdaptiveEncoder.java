package kodowanie;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ArtAdaptiveEncoder extends ArtAdaptive{
    private OutputStream output;
    private int liczbaBitow;
    private int currentByte,numBitsInCurrentByte;
   
    public ArtAdaptiveEncoder(){
        symbole = new int[256];
        temp = new int[256];
        for(int i=0;i<256;i++){
            symbole[i]=i+1;       
        }
        temp = symbole.clone();
    }
    
    public void kompresja(File in,File out) throws FileNotFoundException, IOException{
        int index,j=0;   
        output = new BufferedOutputStream(new FileOutputStream(out));
        b_arr = b_to_arr(in);   
        len = b_arr.length;
        byte[] bytes = ByteBuffer.allocate(8).putLong(len).array();
        output.write(bytes);
        for(int i=0;i<b_arr.length;i++){
            index = Byte.toUnsignedInt(b_arr[i]);
            if((i+256)%256==0){
                j=i+256;
                symbole = temp.clone();
            }
            update(index,j);
            dajBity();
            inc(index);  
        } 
        end();
        close();       
        System.out.println("Skonczylem kompresje");
        System.out.println("Stopien kompresji "+(1.0-((double)out.length()/(double)len)));
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
     
    private void dajBity() throws IOException{       
        ArrayList<Integer> bity = new ArrayList<>();
        while(true){
            if(high<0.5){
                bity.add(0);
                for(int i=0;i<liczbaBitow;i++){bity.add(1);}
                for(int x:bity) write(x);
                liczbaBitow=0;          
                bity = new ArrayList<>();
            }
            else if((low>=0.5)&&(high<=1.0)){
                bity.add(1);
                for(int i=0;i<liczbaBitow;i++){bity.add(0);}
                for(int x:bity) write(x);
                liczbaBitow=0;
                low-=0.5;
                high-=0.5;
                bity = new ArrayList<>();
            }
            else if((low>=0.25)&&(high<0.75)){
                low -=0.25;
                high -=0.25;
                liczbaBitow++;
            }
            else
                break;
            low*=2;
            high*=2;
        }
    }
    private void close() throws IOException {
        while (numBitsInCurrentByte != 0)
            write(0);
        output.close();
    }
    
    private  void end() throws IOException{
        liczbaBitow++;
        ArrayList<Integer> bity = new ArrayList<>();
        if(low<0.25){
            bity.add(0);
            for(int i=0;i<liczbaBitow;i++){bity.add(1);}
            for(int x:bity) write(x);
        }
        else{
           bity.add(1);
           for(int i=0;i<liczbaBitow;i++){bity.add(0);}
           for(int x:bity) write(x); 
        }
    }
    
}
