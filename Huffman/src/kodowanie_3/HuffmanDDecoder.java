package kodowanie_3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
/**
 *
 * @author kgb
 */
public class HuffmanDDecoder {
      
    private OutputStream output;
    private InputStream input;
    private HuffmanDTree ht;
    private int currentByte,nextBits,numBitsRemaining;
    boolean isEndOfStream=false;
    
    public void dekompresja(File in, File out) throws IOException{    
        output = new BufferedOutputStream(new FileOutputStream(out));
        input = new BufferedInputStream(new FileInputStream(in));
        ht = new HuffmanDTree(in);
        ht.b_to_arr(in);  
        int symbol;
        int count=0;
        while(true){
            symbol = read();
            if (symbol == 256)  // EOF
                break;      
            output.write(symbol);
            count++;
            ht.nodes.get(symbol).licznik++;
            if (power2(count)){  // Update tree
                ht.buildTree(); 
                ht.inOrder(ht.node, new ArrayList<>());
            }           
        }   
        input.close();
        output.close();
    }
    
  private int read() throws IOException {		
	HuffmanNode currentNode = ht.node;
	while (true) {
            int temp = bit();
            HuffmanNode nextNode;
            nextNode =(temp == 0) ? currentNode.left:currentNode.right;
            if(!nextNode.pusty){       
                return nextNode.znak;
            }
            else
                currentNode = nextNode;
        }
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
    private static boolean power2(int x) {return x > 0 && (x & -x) == x;}
}
