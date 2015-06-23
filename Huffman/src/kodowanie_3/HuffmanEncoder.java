package kodowanie_3;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kgb
 */
public class HuffmanEncoder {
    private OutputStream output;
    private HuffmanTree ht;
    private ArrayList<HuffmanNode> nodes;
    private int currentByte,numBitsInCurrentByte;

    
    public void kompresja(File in,File out) throws IOException{
        output = new BufferedOutputStream(new FileOutputStream(out));
        ht = new HuffmanTree(in);
        ht.b_to_arr(in);
        ht.praw();
        ArrayList<HuffmanNode> abc = new ArrayList<>();
        for(HuffmanNode hn:ht.nodes){
            abc.add(hn);         
        }      
        ht.remove();
        nodes = ht.getTree();
        for (HuffmanNode ht1 : abc) {
            byte[] bytes = ByteBuffer.allocate(8).putInt(ht1.licznik).array();
            output.write(bytes);
        }      
        ht.buildTree();
        ht.inOrder(ht.node, new ArrayList<>());
        List<Integer> bits;
        
        for(int i=0;i<=ht.b_arr.length;i++){
            bits = (i==ht.b_arr.length)?ht.getCode(256):ht.getCode(Byte.toUnsignedInt(ht.b_arr[i]));
            for(int b : bits){
                write(b);
            }           
        }   
        close();
        System.out.println("Entropia :"+ht.ent());
        double avg_bit=0;
        for(int i=0;i<=256;i++){
            try{
                avg_bit+=ht.codes.get(i).size()*((double)abc.get(i).licznik/in.length());
            }
            catch(NullPointerException e){}// symbol nie zostal uzyty lecimy dalej                    
        }
        System.out.println("Srednia liczba bitow na symbol :"+avg_bit);
        double st = 1-((double)out.length()/(double)in.length());
        System.out.println("Stopien kompresji :"+st);

    }
    private void write(int b) throws IOException{
        currentByte = (b==1) ? currentByte*2+1 : currentByte*2;
        numBitsInCurrentByte++; 
         if (numBitsInCurrentByte == 8) {
            output.write(currentByte);
             //System.out.println(currentByte);

            currentByte=0;
            numBitsInCurrentByte=0;
        }
    }
    private void close() throws IOException {
        while (numBitsInCurrentByte != 0)
            write(0);
        output.close();
    }
}
