package mnkgame;

public class TranspositionTable {

    Zobrist zobrist;
    LinearProbingHashTable transposition_table;

    TranspositionTable(int m, int n){
        zobrist = new Zobrist(m, n);
        transposition_table = new LinearProbingHashTable((int) Math.pow((m*n),2));
    }

    public void add2tab(MNKCell MC[], HeuValue positions){
        int hash = zobrist.hash(MC);
        if(transposition_table.isFull()){
            if(transposition_table.get(hash).val < positions.val){
                transposition_table.remove(hash);
                transposition_table.insert(hash, positions);           
            }
        }else{
            transposition_table.insert(hash, positions);
        }
    }

    public HeuValue get_val(MNKCell[] MC){
        int hash = zobrist.hash(MC);
        return transposition_table.get(hash);
    }
}