package mnkgame;

import java.util.Random;

public class Zobrist{
    public static int[][][] table = new int[10][10][2];
    static Random rand = new Random();
    static long MAX_VAL = 1024*1024; //20 bits


    public static void init_zobrist(int M, int N){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < N; j++){
		//System.out.println(i + j);
                table[i][j][0] = rand.nextInt();
                table[i][j][1] = rand.nextInt();
            }
        }
    }

    public static long hash(MNKCell[] MC){
        long h = 0;
        for(MNKCell d : MC){
            if(d.state == MNKCellState.P1){
                h ^= table[d.i][d.j][0];
            }
            else{
                h ^= table[d.i][d.j][1];
            }
        }
        return h;
    }

    public static void main(String[] args){
        init_zobrist(10,10);
	//System.out.println(rand.nextLong());
        for(int i = 0; i < table.length; i++){
            for(int j = 0; j < table.length; j++){
                System.out.println(table[i][j][0] + "   "  + table[i][j][1] + '\n');
            }
        }
    }
}

