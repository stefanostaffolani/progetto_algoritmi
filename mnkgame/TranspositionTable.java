package mnkgame;

import javax.swing.plaf.synth.SynthStyle;

public class TranspositionTable {

    Zobrist zobrist;
    LinearProbingHashTable transposition_table;

    TranspositionTable(int m, int n){
        zobrist = new Zobrist(m, n);
        transposition_table = new LinearProbingHashTable((int) Math.pow((m*n),5));
    }

    public void add2tab(MNKCell MC[], HeuValue positions){
        int hash = zobrist.hash(MC);
        // System.out.println("aggiungo " + hash);
        if(transposition_table.isFull()){
            // transposition_table.makeEmpty();
            // transposition_table.insert(hash, positions);
            if(transposition_table.get(hash).val < positions.val){
                transposition_table.remove(hash);
                transposition_table.insert(hash, positions);           
            }
        }else{
            transposition_table.insert(hash, positions);
        }

        // System.out.println("aggiungo " + hash);
        // transposition_table.printHashTable();
    }

    public HeuValue get_val(MNKCell MC[]){
        // System.out.println("gud");
        int hash = zobrist.hash(MC);
        // System.out.println(hash);
        // System.out.println("get " + hash);
        HeuValue bruno = transposition_table.get(hash);
        // if(bruno.val > Integer.MIN_VALUE)
        //     System.out.println("bru: " + bruno.val);
        return bruno;
    }

    public int get_size(){
        return transposition_table.getSize();
    }
}