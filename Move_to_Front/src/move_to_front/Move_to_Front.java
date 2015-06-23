package move_to_front;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Math.log;
import java.util.ArrayList;


/**
 *
 * @author kgb
 */
public class Move_to_Front {
    private OutputStream output;
    ArrayList<Integer> slownik = new ArrayList();
    int  [] licznik = new int[256];
    
    public void move_to_front(File in,File out) throws IOException{
        output = new BufferedOutputStream(new FileOutputStream(out));
        for(int i=0;i<256;i++){
            slownik.add(i);
        }
        byte[] arr = b_to_arr(in);
        System.out.println("Entropia in "+ent(in));
        for(int i=0;i<arr.length;i++){
            int c = Byte.toUnsignedInt(arr[i]);
            int index = slownik.indexOf(c);
            slownik.remove(index);
            slownik.add(0,c);
            output.write(index);
        }
        output.close();
        System.out.println("Entropia out "+ent(out));
    }
    
    private byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        byte[] b_arr = new byte[(int)file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(b_arr);
            fis.close();
        }
        return b_arr;
    }
    private  double ent(File file) throws IOException{
        praw(b_to_arr(file));
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
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/kgb/test.tga");
        File file2 = new File("/Users/kgb/test.mtf");      
        new Move_to_Front().move_to_front(file, file2);
        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<26;i++){
            list.add((char)(i+97)+"");
        }
        String test = "bananaaa";
        for(int i=0;i<test.length();i++){
            char c = test.charAt(i);
            int index = list.indexOf(c+"");
            list.remove(index);
            list.add(0,c+"");
            System.out.print(index+" ");
        }
    }
    
}
