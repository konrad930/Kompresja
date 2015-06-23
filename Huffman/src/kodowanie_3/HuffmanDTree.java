package kodowanie_3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public final class HuffmanDTree {
    File file;
    HuffmanNode node = new HuffmanNode(129,true);
    public  ArrayList<HuffmanNode>  nodes = new ArrayList<>();
    public ArrayList<List<Integer>> codes = new ArrayList<>();
    byte[] b_arr;
   
    public HuffmanDTree(File file) throws IOException{
        this.file=file;
        for(int i=0;i<=256;i++){
            nodes.add(new HuffmanNode(i,false));
            codes.add(null);
        }
        for(HuffmanNode n:nodes){n.licznik=1;}
        buildTree();
        b_arr = b_to_arr(file);
        inOrder(node,new ArrayList<>());
    }  
   
    public  byte[] b_to_arr(File file) throws FileNotFoundException, IOException{
        b_arr = new byte[(int)file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(b_arr);
            fis.close();
        }
        return b_arr;
    }
    
    public void buildTree(){
        HuffmanNode h1,h2;   
        PriorityQueue<HuffmanNode> trees = new PriorityQueue<>(); 
        for (HuffmanNode ht1 : nodes) {
            ht1.kod=null;
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
    private int suma(HuffmanNode h1,HuffmanNode h2){return h1.licznik+h2.licznik;}
        
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
    private void rodzic(HuffmanNode h1,HuffmanNode h2,HuffmanNode node){
        h1.rodzic=node;
        h2.rodzic=node;
        node.left=h1;
        node.right=h2;
    }
    public List<Integer> getCode(int symbol){  
        return codes.get(symbol);
    }   
}
