package kodowanie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author kgb
 */
public abstract class ArtAdaptive {
    public long len;
    public static byte[] b_arr;
    protected int[] symbole,temp;
    protected double low=0.0,high=1.0;
    protected  byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        byte [] arr = new byte[(int)file.length()];
        try(FileInputStream fis = new FileInputStream(file)){
            fis.read(arr);
            fis.close();
        }
        return arr;
    }
    protected void update(int index,int i){
        double range = (high-low);
        double suma = (index!=0)?symbole[index-1]:0.0;
        double licznik = symbole[index];
        double newlow = ((suma*(high-low))/(i))+low;
        high = newlow+(((licznik-suma)/i)*range);
        low = newlow;
    }
    protected void inc(int index){
        for(int i=index;i<256;i++){
            temp[i]++;
        }
    }  
}
