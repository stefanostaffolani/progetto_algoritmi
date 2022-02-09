package mnkgame;

import java.util.Random;

public class Zobrist{
    int[][][] table;
    Random rand;
    int MAX_VAL;
    //Hashtable <Integer, Integer> transposition_table;

    Zobrist(int m, int n){
        table =  new int[m][n][2];
        //transposition_table = new Hashtable<>(m*n);
        rand = new Random();
        MAX_VAL = 1024*1024;   // verificare se considera 20 bit...
        init_zobrist(m, n);
    }

    public void init_zobrist(int M, int N){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
		//System.out.println(i + j);
                table[i][j][0] = rand.nextInt(MAX_VAL);
                table[i][j][1] = rand.nextInt(MAX_VAL);
            }
        }
    }

    //public void add2tab(int h, int n){
    //    transposition_table.put(h, n);
    //}

    //public void print_transposition_table(){
    //    System.out.println(transposition_table);
    //    System.out.println(transposition_table.size());
    //}

    public int hash(MNKCell[] MC){
        int h = 0;
        for(MNKCell d : MC){
            if(d.state == MNKCellState.P1){
                h ^= table[d.i][d.j][0];
            }
            else{
                h ^= table[d.i][d.j][1];
            }
        }

        // System.out.println(h);

        return h;
    }
    
}

