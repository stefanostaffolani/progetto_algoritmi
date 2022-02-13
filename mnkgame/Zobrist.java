package mnkgame;

import java.util.Random;

public class Zobrist{
    int[][][] table;
    Random rand;
    int MAX_VAL;

    Zobrist(int m, int n){
        table =  new int[m][n][2];
        rand = new Random();
        MAX_VAL = 1024*1024;   // verificare se considera 20 bit...
        init_zobrist(m, n);
    }

    public void init_zobrist(int M, int N){  // inizializza la table con valori interi random
        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
                table[i][j][0] = rand.nextInt(MAX_VAL);
                table[i][j][1] = rand.nextInt(MAX_VAL);
            }
        }
    }

    public int hash(MNKCell[] MC){
        int h = 0;
        for(MNKCell d : MC){
            if(d.state == MNKCellState.P1){
                h ^= table[d.i][d.j][0];   // xor bitwise
            }
            else{
                h ^= table[d.i][d.j][1];
            }
        }
        return h;
    }   
}