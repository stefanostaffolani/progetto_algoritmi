/* 
 * questo player è uno sfaso che funziona con base alpha_beta_pruning
 * e mi serve come prova per implementare cose 
 */


package mnkgame;

public class tryHeuristicPlayer extends AlphaBetaPruning {
    
    // Default empty constructor
    public tryHeuristicPlayer() {}

    @Override
    public double alphaBeta(boolean isMaximising, double a, double b){
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
                double score = alphaBeta(false, a, b);
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
                double score = alphaBeta(true, a, b);
                B.unmarkCell();
                bestScore = min(score, bestScore);
                b = min(b, bestScore);
                if(b <= a) break;
            }
            return bestScore;
        }
        
    }
    
}


