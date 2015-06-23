package kodowanie_7;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.log;
/**
 *
 * @author kgb
 */
public class JPEGLS {
    static int  [] licznik = new int[256];
    private int height,width,predykcja;
    private byte[] naglowek,stopka;
    private Color[][] bitmapa;
    private double ent=8.0,ent_r=8.0,ent_g=8.0,ent_b=8.0;
    /*
        Tworzenie naglowka stopki i bitmapy
    */
    public void liczarka(File in) throws IOException{
        byte by[]=b_to_arr(in);
        naglowek = new byte[18];
        stopka = new byte[26];
        height = 256*Byte.toUnsignedInt(by[13])+Byte.toUnsignedInt(by[12]);
        width = 256*Byte.toUnsignedInt(by[15])+Byte.toUnsignedInt(by[14]);
        bitmapa = new Color[width][height];
        int j=0,w=0,h=0;
        for(int i=0;i<by.length;i++){        
            if(i<18){
                naglowek[i]=by[i];
            }
            else if(i>=by.length-26){
                stopka[j]=by[i];
                j++;
            }
            else{
                int r = Byte.toUnsignedInt(by[i]);
                int g = Byte.toUnsignedInt(by[i+1]);
                int b = Byte.toUnsignedInt(by[i+2]);
                bitmapa[w][h] = new Color(r,g,b);
                if(h+1==height){
                    h=0;
                    w++;
                }
                else{
                    h++;
                }
                i+=2;
            }
        }
    }
    public void schemat(){
        System.out.println(width+"x"+height);
        double ent_arr[] = new double[8];
        Predykcja pred[] = new Predykcja[8];
        
        pred[0] = new Predykcja(predykcja(0,1));
        pred[1] = new Predykcja(predykcja(1,0));
        pred[2] = new Predykcja(predykcja(1,1));
        pred[3] = new Predykcja(predykcja4(0));
        pred[4] = new Predykcja(predykcja4(1));
        pred[5] = new Predykcja(predykcja4(2));
        pred[6] = new Predykcja(predykcja4(3));
        pred[7] = new Predykcja(predykcja4(7));
             
        for(int i=0;i<ent_arr.length;i++){
             ent_arr[i] = entropia(pred[i].pred,3,3);
        }        
        System.out.println("Entropia_in "+entropia(bitmapa,3,3));
        System.out.println("Entropia_in red "+entropia(bitmapa,0,1));
        System.out.println("Entropia_in green "+entropia(bitmapa,1,1));
        System.out.println("Entropia_in blue "+entropia(bitmapa,2,1));
        System.out.println();
        for(int i=0;i<ent_arr.length;i++){
            if(ent_arr[i]<ent){
                ent=ent_arr[i];
                ent_r=entropia(pred[i].pred,0,1);
                ent_g=entropia(pred[i].pred,1,1);
                ent_b=entropia(pred[i].pred,2,1);
                
                predykcja=i;
            }  
        }   
        System.out.println("Predykcja nr "+(predykcja+1)+" Entropia : "+ent);
        System.out.println("Entropia red "+ent_r);
        System.out.println("Entropia green "+ent_g);
        System.out.println("Entropia blue "+ent_b);
    }
    private Color[][] predykcja(int w,int h){
        Color[][] temp = new Color[width][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                try{
                   Color x = bitmapa[i][j],y=bitmapa[i-w][j-h];
                   temp[i][j] = new Color(x.getRed()-y.getRed(),x.getGreen()-y.getGreen(),x.getBlue()-y.getBlue());
                }
                catch(ArrayIndexOutOfBoundsException e){
                    temp[i][j]=bitmapa[i][j];
                }
            }
        }
        return temp;
    }
     private Color[][] predykcja4(int arg){
        Color[][] temp = new Color[width][height];
        Color a,b,c,x;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                x = bitmapa[i][j];
                if(j>0){
                    a=bitmapa[i][j-1];
                }
                else{
                    a=new Color(0,0,0);
                }
                if(i>0){
                    b=bitmapa[i-1][j];
                }
                else{
                    b=new Color(0,0,0);
                }
                if((i>0)&&(j>0)){
                    c=bitmapa[i-1][j-1];
                }
                else{
                    c=new Color(0,0,0);
                }  
                if(arg==0)
                    temp[i][j] = new Color(x.getRed()-(a.getRed()+b.getRed()-c.getRed()),x.getGreen()-(a.getGreen()+b.getGreen()-c.getGreen()),x.getBlue()-(a.getBlue()+b.getBlue()-c.getBlue()));
                else if(arg==1){
                    temp[i][j] = new Color(x.getRed()-(a.getRed()+((b.getRed()-c.getRed())/2)),x.getGreen()-(a.getGreen()+((b.getGreen()-c.getGreen())/2)),x.getBlue()-(a.getBlue()+((b.getBlue()-c.getBlue())/2)));
                }
                else if(arg==2){
                    temp[i][j] = new Color(x.getRed()-(b.getRed()+((a.getRed()-c.getRed())/2)),x.getGreen()-(b.getGreen()+((a.getGreen()-c.getGreen())/2)),x.getBlue()-(b.getBlue()+((a.getBlue()-c.getBlue())/2)));
                }
                else if(arg==3){
                    temp[i][j] = new Color(x.getRed()-((a.getRed()+b.getRed())/2),x.getGreen()-((a.getGreen()+b.getGreen())/2),x.getBlue()-((a.getBlue()+b.getBlue())/2));
                }
                else{
                    int r_c,g_c,b_c;
                    
                    if(c.getRed()>=Integer.max(a.getRed(),b.getRed())){ 
                        r_c = Integer.min(a.getRed(), b.getRed());
                    }
                    else if(c.getRed()<=Integer.min(a.getRed(),b.getRed())) {
                        r_c = Integer.max(a.getRed(), b.getRed());
                    }
                    else{
                        r_c=a.getRed()+b.getRed()-c.getRed();
                    }
                    if(c.getGreen()>=Integer.max(a.getGreen(),b.getGreen())){ 
                        g_c = Integer.min(a.getGreen(), b.getGreen());
                    }
                    else if(c.getGreen()<=Integer.min(a.getGreen(),b.getGreen())) {
                        g_c = Integer.max(a.getGreen(), b.getGreen());
                    }
                    else{
                        g_c=a.getGreen()+b.getGreen()-c.getGreen();
                    }
                    if(c.getBlue()>=Integer.max(a.getBlue(),b.getBlue())){ 
                        b_c = Integer.min(a.getBlue(), b.getBlue());
                    }
                    else if(c.getBlue()<=Integer.min(a.getBlue(),b.getBlue())) {
                        b_c = Integer.max(a.getBlue(), b.getBlue());
                    }
                    else{
                        b_c=a.getBlue()+b.getBlue()-c.getBlue();
                    }
                    Color y = new Color(r_c,g_c,b_c);
                    temp[i][j] = new Color(x.getRed()-y.getRed(),x.getGreen()-y.getGreen(),x.getBlue()-y.getBlue());
                }
            }
        }
        return temp;
    }
    private double entropia(Color[][] pred,int arg,int len){
        byte[] temp = new byte[width*height*len];
        licznik = new int[256];
        int q=0;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                if(arg==0)
                    temp[q]=(byte)pred[i][j].getRed();
                else if(arg==1){
                    temp[q]=(byte)pred[i][j].getGreen();
                }
                else if(arg==2){
                    temp[q]=(byte)pred[i][j].getBlue();
                }
                else{
                   temp[q]=(byte)pred[i][j].getRed();
                   temp[q+1]=(byte)pred[i][j].getGreen();
                   temp[q+2]=(byte)pred[i][j].getBlue(); 
                   q+=2;
                }
                q++;
            }
        }
        praw(temp);
        return ent(len);
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
    private double ent(int x){
        double entr =0;
        for(int i=0;i<256;i++){            
            double p = (double)licznik[i]/ (height*width*x);
            if (p>0) 
                entr -= (double) p*(log(p)/log(2)); 
        }
        return entr;
    }
    private static void praw(byte[] b_arr){
        for(int i=0;i<b_arr.length;i++){
            licznik[Byte.toUnsignedInt(b_arr[i])]++;
        }
    }
}
