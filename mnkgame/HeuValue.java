package mnkgame;

public class HeuValue {
    //public int k;   // valore chiave
    public int val; // valore di euristica
    public int i;
    public int j;
    HeuValue(int i, int j){
        // k = unique value taken from i & j
        //k = i * m + j;
        this.i = i;
        this.j = j;
    }
}