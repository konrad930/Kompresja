package kodowanie_3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class HuffmanDecoder {
    private OutputStream output;
    private InputStream input;
    private HuffmanTree ht;
    private int currentByte,nextBits,numBitsRemaining;
    boolean isEndOfStream=false;
    
    public void dekompresja(File inputFile,File outputFile) throws IOException{
        input = new BufferedInputStream(new FileInputStream(inputFile));
        output = new BufferedOutputStream(new FileOutputStream(outputFile));

        ht = new HuffmanTree(inputFile);
        ArrayList<HuffmanNode> nodes = new ArrayList<>();
        int symbol;
        for(int i=0;i<=256;i++){
            nodes.add(new HuffmanNode(i,false));
            byte[] b = new byte[4];
            symbol = input.read(b);
            int num = ByteBuffer.wrap(b).getInt();
            nodes.get(i).licznik=num;
        }      
        ht.nodes=nodes;
        ht.buildTree();
        while(true){
            symbol = read();
            if (symbol == 256)  // EOF symbol
                break;
            output.write(symbol);

        }   
        input.close();
        output.close();
    }
    
    public int read() throws IOException {		
	HuffmanNode currentNode = ht.node;
	while (true) {
            int temp = bit();
            HuffmanNode nextNode;
            nextNode =(temp == 0) ? currentNode.left:currentNode.right;
            if(!nextNode.pusty)
                return nextNode.znak;
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
    
}
