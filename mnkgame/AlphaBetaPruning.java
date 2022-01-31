package mnkgame;

public class AlphaBetaPruning implements MNKPlayer {
     
    public MNKBoard B;
    public MNKGameState myWin;
    public MNKGameState yourWin; 
    public int TIMEOUT;
    public long start;

    // Default empty constructor
    public AlphaBetaPruning() {}

    public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
        B       = new MNKBoard(M,N,K);
        myWin   = first ? MNKGameState.WINP1 : MNKGameState.WINP2;
        yourWin = first ? MNKGameState.WINP2 : MNKGameState.WINP1;
        TIMEOUT = timeout_in_secs;
    }

    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
        start   = System.currentTimeMillis();
        MNKCell ret_value = null;
        //System.out.println("FC.len = " + FC.length);

        if(MC.length > 0) {
            MNKCell c = MC[MC.length-1]; // Recover the last move from MC
            B.markCell(c.i,c.j);         // Save the last move in the local MNKBoard
        }

        //System.out.println("B.FC.len = " + B.getFreeCells().length);

        double bestScore = Double.NEGATIVE_INFINITY;
        for(MNKCell d : FC) {
            B.markCell(d.i, d.j);
            double score = alphaBeta(false, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 30);
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
        return "abPruning_player";
    }

    public double alphaBeta(boolean isMaximising, double a, double b, int depth){
        // if it runs out of time or depth is 0 return 0
        if((System.currentTimeMillis()-start)/1000.0 > TIMEOUT*(99.0/100.0) || depth == 0) 
            return 0;
            
        // check if there's a win ----------------------------------
        MNKCell[] MC = B.getMarkedCells();
        MNKCell c = MC[MC.length-1];
        B.unmarkCell();
        if(B.markCell(c.i, c.j) == myWin)
            return 10;
        else{
            B.unmarkCell();
            if(B.markCell(c.i, c.j) == yourWin)
                return -10;
        } // -------------------------------------------------------


        // check if draw -------------------------------------------
        if(B.getFreeCells().length == 0)
            return 0;
        // ---------------------------------------------------------



        // search for the win --------------------------------------
        if(isMaximising) { 
            double bestScore = Double.NEGATIVE_INFINITY;
            for(MNKCell d : B.getFreeCells()) {
                B.markCell(d.i, d.j);
                double score = alphaBeta(false, a, b, depth-1);
                B.unmarkCell();
                bestScore = max(score, bestScore);
                a = max(a, bestScore);
                if(b <= a) break;
            }
            return bestScore;
        }
        else {
            double bestScore = Double.POSITIVE_INFINITY;
            for(MNKCell d : B.getFreeCells()) {
                B.markCell(d.i, d.j);
                double score = alphaBeta(true, a, b, depth-1);
                B.unmarkCell();
                bestScore = min(score, bestScore);
                b = min(b, bestScore);
                if(b <= a) break;
            }
            return bestScore;
        } // --------------------------------------------------------
    }

    public double max(double a, double b) { if(a > b) return a; else return b; }
    public double min(double a, double b) { if(a < b) return a; else return b; }
}
