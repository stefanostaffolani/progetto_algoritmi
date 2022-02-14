package mnkgame;

public class TranspositionTable {

    Zobrist zobrist;
    LinearProbingHashTable transposition_table;

    TranspositionTable(int m, int n){
        zobrist = new Zobrist(m, n);
        transposition_table = new LinearProbingHashTable((int) Math.pow((m*n),2));
    }

    public void remove_from_tab(MNKCell MC[]){
        Integer hash = zobrist.hash(MC);
        transposition_table.remove(hash);
    }

    public void add2tab(MNKCell MC[], HeuValue positions){ 
        int hash = zobrist.hash(MC);
        if(transposition_table.isFull())
            transposition_table.makeEmpty();        
        transposition_table.insert(hash, positions);
    }

    public HeuValue get_val(MNKCell MC[]){
        int hash = zobrist.hash(MC);
        return transposition_table.get(hash);
    }

    public int get_size(){
        return transposition_table.getSize();
    }
}
