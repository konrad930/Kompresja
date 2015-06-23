package kodowanie_7;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author kgb_putin
 */
public class LZW {
    int slownik_l=256;
    ArrayList<Integer> fib = new ArrayList();
    private InputStream input;
    private OutputStream output;
    private int currentByte,numBitsInCurrentByte;
    private int nextBits,numBitsRemaining;
    boolean isEndOfStream=false;
        
    public void kompresja(File in,File out) throws IOException, IOException, IOException{
        fib_add();
        String result;
        output = new BufferedOutputStream(new FileOutputStream(out));
        byte[] arr = b_to_arr(in);
        Map<String,Integer> slownik = new HashMap<>();
        //tworzenie poczatkowego slownika    
        for (int i=0;i<256;i++)
            slownik.put("" + (char)i, i+1);   
        String w="";
        for(int i=0;i<arr.length;i++){
            char c = (char)Byte.toUnsignedInt(arr[i]);
            String wc = w+c;
            if(slownik.containsKey(wc))
                w=wc;
            else{
                slownik.put(wc,slownik.size()+1);
                result = fib_enc(slownik.get(w));            
                for(int j=0;j<result.length();j++){
                    write(Integer.parseInt(Character.toString(result.charAt(j))));
                }
                w =""+c;
            }        
        }
        if (!w.equals("")){
            result = fib_enc(slownik.get(w));
            for(int j=0;j<result.length();j++)
                write(Integer.parseInt(Character.toString(result.charAt(j))));
        }  
        close();
        System.out.println("Kompresja skonczona");
        System.out.println("Dlugosc pliku in : "+in.length()/1000+"kB "+(in.length()%1000)+"B");
        System.out.println("Dlugosc pliku out : "+out.length()/1000+"kB "+(out.length()%1000)+"B");
        System.out.println("Stopien kompresji "+(((double)out.length()/(double)in.length())));
    }
    public void dekompresja(File in,File out) throws IOException{
        fib_add();
        input = new BufferedInputStream(new FileInputStream(in));
        output = new BufferedOutputStream(new FileOutputStream(out));
        int zero=0;
        Map<Integer,String> slownik = new HashMap<>();
        //tworzenie poczatkowego slownika
        for (int i=0;i<256;i++)
            slownik.put(i+1,""+(char)i);
        ArrayList<Integer> com = fib_dec(); 
        int f = com.remove(0)-1;
        output.write(f);
        String w = (char)f+"";
        for (int k : com){
            String entry;
            if (slownik.containsKey(k)){
                entry = slownik.get(k);
                slownik.put(slownik.size()+1,w+entry.charAt(0));
            }
            else{
                entry = w+w.charAt(0);
                slownik.put(slownik.size()+1,entry);
            }              
            for(int j=0;j<entry.length();j++)
                output.write(entry.charAt(j));
            w = entry;    
        }
        close();
        System.out.println("Dlugosc pliku po dekompresji : "+out.length()/1000+"kB "+(out.length()%1000)+"B");
        System.out.println("Dekompresja skonczona");     
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
    
    private byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        byte[] b_arr = new byte[(int)file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(b_arr);
            fis.close();
        }
        return b_arr;
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
    private void fib_add(){
        int size = fib.size();
        if(size==0)
            fib.add(1);
        else if(size==1)
            fib.add(2);
        else
            fib.add(fib.get(size-1)+fib.get(size-2));
        
    }
    private String fib_enc(int index){
        String temp="",res="";
        int fib_num=0;
        int p=0,fn=1,lfn=1;
        
        while(fib.get(p)<=index){         
            p++;
            if(fib.size()<=p)
                fib_add();
        }
        p--;
        while(p>=0){
            fn<<=1;
            if(fib.get(p)<=index){
                fn = fn|1;
                index-=fib.get(p);
            }
            lfn++;
            p--;
        }     
        temp = Integer.toBinaryString(fn);
        for(int i=temp.length()-1;i>=0;i--){
            res+=temp.charAt(i);
        }
        return res;
    }
    private int liczba(ArrayList<Integer> temp){
        int liczba=0;
        for(int i=0;i<temp.size();i++){
            if(fib.size()==i)
                fib_add();
            if(temp.get(i)==1)
                liczba+=fib.get(i);
        }
        return liczba;
    }
    private ArrayList<Integer> fib_dec() throws IOException{
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();
        while(true){
            int x = bit();
            if(x==-1)
                break;
            else if(x==0)
                temp.add(0);
            else{
                if(temp.size()<1){
                    temp.add(1);
                }
                else{
                    if(temp.get(temp.size()-1)==1){
                        result.add(liczba(temp));
                        temp = new ArrayList<>();
                    }
                    else{
                        temp.add(1);
                    }
                }
            }
        }       
        return result;
    }
    public static void kompresor(File in,File out) throws IOException{
        new LZW().kompresja(in, out);
    }
    public static void dekompresor(File in, File out) throws IOException{
        new LZW().dekompresja(in, out);
    }
}
