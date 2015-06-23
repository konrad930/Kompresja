package kodowanie_3;

import java.util.List;

/**
 *
 * @author kgb
 */
public class HuffmanNode implements Comparable<HuffmanNode>{
    
    public HuffmanNode left,right,rodzic;
    public boolean pusty;
    public int znak,licznik;
    public List<Integer> kod;
       
    public HuffmanNode(int znak,boolean pusty){
        this.pusty=pusty;
        this.znak=znak;
        licznik=0;
    }

    @Override
    public int compareTo(HuffmanNode ht) {
        int result = ht.licznik;
        return this.licznik-result;
    }
    
    
}
