package kodowanie10;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 *
 * @author kgb
 */
public class Hamming {
    OutputStream output;
    
    public void koder(File in,File out) throws IOException{
        output = new BufferedOutputStream(new FileOutputStream(out));
        byte[] arr = b_to_arr(in);
        for(byte b:arr){
            String a = Integer.toBinaryString(Byte.toUnsignedInt(b));
            while(a.length()!=8){ a="0"+a;}
            String b1 = a.substring(0, 4),b2 = a.substring(4);
            encoder(b1);
            encoder(b2);
        }
        output.close();
        System.out.println("Koniec kodowania");
    }
    public void dekoder(File in,File out) throws FileNotFoundException, IOException{   
        int dwa_bledy=0,w=0,result=0;
        output = new BufferedOutputStream(new FileOutputStream(out));
        byte[] arr = b_to_arr(in);
        int[] h= new int[8];
        for(byte b:arr){
            int j=0;
            String a = Integer.toBinaryString(Byte.toUnsignedInt(b));   
            //System.out.println(a);
            while(a.length()!=8){ a="0"+a;}
            for(int i=0;i<8;i++){
                h[i]=Integer.parseInt(a.charAt(i)+"");
            }
            int c0 = h[0]^h[2]^h[4]^h[6];
            int c1 = h[1]^h[2]^h[5]^h[6];
            int c2 = h[3]^h[4]^h[5]^h[6];
            for(int i=0;i<7;i++){
                if(h[i]==1)
                    j++;
            }
            if(j%2!=h[7]){
                int index = (c0+(2*c1)+(4*c2))-1;
                index =(index==-1)?7:index;
                h[index] = flp(index);
                //System.out.println("blad : "+index);               
            }
            else{
                int p1 = h[2]^h[4]^h[6];
                int p2 = h[2]^h[5]^h[6];
                int p3 = h[4]^h[5]^h[6];
                if((p1==h[0])&&(p2==h[1])&&(p3==h[3])){
                    //System.out.println("Bez bledow");
                }
                else{
                    //System.out.println("Dwa bledy");
                    dwa_bledy++;                  
                }
            }        
            if(w==1){
                result+=8*h[2];
                result+=4*h[4];
                result+=2*h[5];
                result+=1*h[6];
               // System.out.println(result+" "+h[2]+" "+h[4]+" "+h[5]+" "+h[6]);
                output.write(result);
                result=0;
                w=0;
            }
            else{
                result+=128*h[2];
                result+=64*h[4];
                result+=32*h[5];
                result+=16*h[6];
                w=1;
            }
        }
        output.close();
        System.out.println(dwa_bledy+" : razy znalazlem 2 bledy");
        System.out.println("Koniec dekodowania");
    }
       
    public void encoder(String a) throws IOException{
        int[] h= new int[8];
        int m=128,result=0;
        int j=0;
        //System.out.println(a);
        for(int i=0;i<7;i++){
            if(!power2(i+1)){
               h[i]=Integer.parseInt(a.charAt(j)+"");          
               j++;
            }      
        }
        h[0]=h[2]^h[4]^h[6];
        h[1]=h[2]^h[5]^h[6];
        h[3]=h[4]^h[5]^h[6];
        h[7]=h[0]^h[1]^h[2]^h[3]^h[4]^h[5]^h[6];
        for(int i=0;i<8;i++){
            result+=h[i]*m;
            m/=2;
        }
        //System.out.println(Integer.toBinaryString(result));
        output.write(result);
    }
     
    private int flp(int i){return (i==1)?0:1;}
    
    private  boolean power2(int x) {return x > 0 && (x & -x) == x;}
    
    private  byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        byte [] arr = new byte[(int)file.length()];
        try(FileInputStream fis = new FileInputStream(file)){
            fis.read(arr);
            fis.close();
        }
        return arr;
    }
    public int sprawdz(File in1,File in2) throws IOException{
        int result =0;
        byte[]arr1=b_to_arr(in1);
        byte[]arr2=b_to_arr(in2);
        
        int len=(arr1.length>arr2.length)?arr2.length:arr1.length;
        
        for(int i=0;i<len;i++){
            String a = Integer.toBinaryString(Byte.toUnsignedInt(arr1[i]));
            String b = Integer.toBinaryString(Byte.toUnsignedInt(arr2[i]));          
            while(a.length()!=8){ a="0"+a;}
            while(b.length()!=8){ b="0"+b;}
            String a1 = a.substring(0, 4),a2 = a.substring(4);
            String b1 = b.substring(0, 4),b2 = b.substring(4);
            System.out.println(a1+" "+b1+" "+a2+" "+b2);
            if(!a1.equals(b1)){result++;}
            if(!a2.equals(b2)){result++;}
        }
        return result;
    }
    public void szum(int p,File in,File out) throws FileNotFoundException, IOException{
        StringBuilder temp;       
        output = new BufferedOutputStream(new FileOutputStream(out));
        byte[]arr=b_to_arr(in);
        for(byte b:arr){
            int result=0,m=128;
            String a = Integer.toBinaryString(Byte.toUnsignedInt(b));
            while(a.length()!=8){ a="0"+a;}
            temp = new StringBuilder(a);
            for(int i=0;i<a.length();i++){
                char v =(a.charAt(i)=='1')?'0':'1';
                if(p>new Random().nextInt(100)){
                   // System.out.println("Zmienilem");
                    temp.setCharAt(i, v);
                }
            }
            a = temp.toString();
            for(int i=0;i<8;i++){
                result+=Integer.parseInt(a.charAt(i)+"")*m;
                m/=2;
            }
            output.write(result);
        }
        output.close();
    }
}
