package mnkgame;

public class TranspositionTable {

    Zobrist zobrist;
    LinearProbingHashTable transposition_table;

    TranspositionTable(int m, int n){
        zobrist = new Zobrist(m, n);
        transposition_table = new LinearProbingHashTable((int) 3);
    }

    public void remove_from_tab(MNKCell MC[]){
        Integer hash = zobrist.hash(MC);
        transposition_table.remove(hash);
    }

    public void add2tab(MNKCell MC[], HeuValue positions){
        int hash = zobrist.hash(MC);
        // System.out.println("aggiungo " + hash);
        if(transposition_table.isFull())
            transposition_table.makeEmpty();
            // transposition_table.insert(hash, positions);
      
            
        
        transposition_table.insert(hash, positions);
        // System.out.println("aggiungo " + hash);
        // transposition_table.printHashTable();
    }

    public HeuValue get_val(MNKCell MC[]){
        // System.out.println("gud");
        int hash = zobrist.hash(MC);

        //System.out.println(hash);
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