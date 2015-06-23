package kodowanie_3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public final class HuffmanTree {
    File file;
    HuffmanNode node = new HuffmanNode(129,true);
    public  ArrayList<HuffmanNode>  nodes = new ArrayList<>();
    public ArrayList<List<Integer>> codes = new ArrayList<>();
    byte[] b_arr;
   
    public HuffmanTree(File file) throws IOException{
        this.file=file;
        for(int i=0;i<=256;i++){
            nodes.add(new HuffmanNode(i,false));
            codes.add(null);
        }
    }  
   
    public  byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        b_arr = new byte[(int)file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(b_arr);
            fis.close();
        }
        return b_arr;
    }

    public void praw(){
        nodes.get(256).licznik++;
        for(int i=0;i<b_arr.length;i++){
            this.nodes.get(Byte.toUnsignedInt(b_arr[i])).licznik++;
        }
      
    }
    public void remove(){
        for(int i=0;i<nodes.size();i++)
            if(nodes.get(i).licznik==0){
                nodes.remove(i);
                i--;
        }
    }
    
    public void buildTree(){
        HuffmanNode h1,h2;
        ArrayList<HuffmanNode> temp = new ArrayList<>();
        for (HuffmanNode node1 : nodes) {
            if (node1.licznik > 0) {
                temp.add(node1);
            }
        }
        if(temp.size()==1){
            temp.get(0).rodzic=node;
            node.left=temp.get(0);
            return;
        }
        PriorityQueue<HuffmanNode> trees = new PriorityQueue<>(); 
        for (HuffmanNode ht1 : temp) {
            trees.add(ht1);
        }
        while(trees.size()>1){           
            h1 = trees.remove();
            h2 = trees.remove();
            node = new HuffmanNode(0,true);
            rodzic(h1,h2,node);
            node.licznik = suma(h1,h2);
            trees.add(node);
        }
    }
    private int suma(HuffmanNode h1,HuffmanNode h2){
        return h1.licznik+h2.licznik;
    }

    public void inOrder (HuffmanNode root,List<Integer> kod){
        if(root == null) return;
        
        if(root.pusty){
            kod.add(0);
            inOrder(root.left,kod);
            kod.remove(kod.size() - 1);
            kod.add(1);
            inOrder(root.right,kod);
            kod.remove(kod.size() - 1);
        }
        else{
            root.kod = new ArrayList<>(kod);
            codes.set(root.znak, new ArrayList<>(kod));
        }   
    }
    public void write (HuffmanNode root){
        if(root == null) return;
        write( root.left);
        if(!root.pusty)
            System.out.println(root.znak+" "+codes.get(root.znak)+" "+codes.get(root.znak).size());
        write( root.right); 
    }

    public  double ent(){
        double ent =0;
        for (HuffmanNode ht1 : nodes) {
            double p = (double) ht1.licznik / file.length();
            if (p>0)
                ent -= (double) p*(log(p)/log(2)); 
        }
        return ent;
    }

    private void rodzic(HuffmanNode h1,HuffmanNode h2,HuffmanNode node){
        h1.rodzic=node;
        h2.rodzic=node;
        node.left=h1;
        node.right=h2;
    }
    public ArrayList<HuffmanNode> getTree(){
        return this.nodes;
    }
    public List<Integer> getCode(int symbol){  
        return codes.get(symbol);
    }
    
}
