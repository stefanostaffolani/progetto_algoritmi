package mnkgame;

public class MinimaxPlayer implements MNKPlayer {
    
    private MNKBoard B;
    private MNKGameState myWin;
    private MNKGameState yourWin;
    private int TIMEOUT;
    private long start;

	// Default empty constructor
    public MinimaxPlayer() {}

    public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
        B       = new MNKBoard(M,N,K);
        myWin   = first ? MNKGameState.WINP1 : MNKGameState.WINP2;
        yourWin = first ? MNKGameState.WINP2 : MNKGameState.WINP1;
        TIMEOUT = timeout_in_secs;
        start   = System.currentTimeMillis();
    }
 
    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
        MNKCell ret_value = null;

        //System.out.println("FC.len = " + FC.length);

        if(MC.length > 0) {
			MNKCell c = MC[MC.length-1]; // Recover the last move from MC
			B.markCell(c.i,c.j);         // Save the last move in the local MNKBoard
		}

        //System.out.println("B.FC.len = " + B.getFreeCells().length);

        double bestScore = Double.POSITIVE_INFINITY*(-1);
        for(MNKCell d : FC) {
			B.markCell(d.i, d.j);
            double score = minimax(false);
            B.unmarkCell();
            if(score > bestScore){
                bestScore = score;
                ret_value = d;
            }
        }

        B.markCell(ret_value.i, ret_value.j);
        return ret_value;
    }

    public String playerName() {
        return "MinimaxPlayer";
    }

    public double minimax(boolean isMaximising){
        if((System.currentTimeMillis()-start)/1000.0 > TIMEOUT*(99.0/100.0)) 
            return 0;
            

        // check if tie
        if(B.getFreeCells().length == 0)
            return 0;

        // check if ther's a win
        MNKCell[] MC = B.getMarkedCells();
        MNKCell c = MC[MC.length-1];
        B.unmarkCell();
        if(B.markCell(c.i, c.j) == myWin)
            return 10;
        else{
            B.unmarkCell();
            if(B.markCell(c.i, c.j) == yourWin)
                return -10;
        }

        // search for the win
        if(isMaximising) { 
            double bestScore = Double.POSITIVE_INFINITY * (-1);
            for(MNKCell d : B.getFreeCells()) {
                B.markCell(d.i, d.j);
                double score = minimax(false);
                B.unmarkCell();
                bestScore = max(score, bestScore);
            }
            return bestScore;
        }
        else {
            double bestScore = Double.POSITIVE_INFINITY;
            for(MNKCell d : B.getFreeCells()) {
                B.markCell(d.i, d.j);
                double score = minimax(true);
                B.unmarkCell();
                bestScore = min(score, bestScore);
            }
            return bestScore;
        }
    }

    public double max(double a, double b) { if(a > b) return a; else return b; }
    public double min(double a, double b) { if(a < b) return a; else return b; }
}
