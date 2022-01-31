/* 
 * questo player è uno sfaso che funziona con base alpha_beta_pruning
 * e mi serve come prova per implementare cose 
 */

package mnkgame;

public class TryHeuristicPlayer implements MNKPlayer{

    // a copy of the board
    public MNKBoard B;
    
    // to check if p1 or p2 win 
    public MNKGameState myWin;
    public MNKGameState yourWin; 

    // execution time limit
    public int TIMEOUT;
    public long start;

    // HeuristicMatrix
    public HeuValue[][] matrix;

    // copy of M N 
    public int m;
    public int n;

    // Default empty constructor
    public TryHeuristicPlayer() {}

    public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
        B       = new MNKBoard(M,N,K);

        myWin   = first ? MNKGameState.WINP1 : MNKGameState.WINP2;
        yourWin = first ? MNKGameState.WINP2 : MNKGameState.WINP1;

        TIMEOUT = timeout_in_secs;

        m       = M;
        n       = N;
        matrix  = new HeuValue[m][n];
    }

    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
        start   = System.currentTimeMillis();
        
        MNKCell ret_value = null;           // ret_value store the value to return and it's updated during the execution 

        if(MC.length > 0) {
            MNKCell c = MC[MC.length-1];    // Recover the last move from MC
            B.markCell(c.i,c.j);            // Save the last move in the local MNKBoard
        }

        //List<HeuValue> list = new ArrayList<HeuValue>();
        double bestScore = Double.NEGATIVE_INFINITY;
        init_matrix(MC);  // 0 if it is a free cell -1 if P1, -2 if P2
        MaxHeap max_heap = new MaxHeap(matrix, m, n);

        // ciclo di analisi mosse, termina quando termina il tempo per selectCell 
        while((System.currentTimeMillis()-start)/1000.0 <= TIMEOUT*(99.0/100.0) && max_heap.last > 0){
            HeuValue e = max_heap.extract_max();

            if(e.val != -1 && e.val != -2){
                B.markCell(e.i, e.j);
                update_matrix(e, true);

                double score = alphaBeta(false, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 10);
                
                B.unmarkCell();
                update_matrix(e, false);

                if(score > bestScore){
                    bestScore = score;
                    ret_value = new MNKCell(e.i, e.j);
                }
            }
        } 

        B.markCell(ret_value.i, ret_value.j);
        return ret_value;
    }

    // inizializza la matrice
    public void init_matrix(MNKCell[] MC){
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                matrix[i][j] = new HeuValue(i, j);
                if(B.B[i][j] == MNKCellState.FREE)
                    matrix[i][j].val = 0;
                else if(B.B[i][j] == MNKCellState.P1)
                    matrix[i][j].val = -1;
                else if(B.B[i][j] == MNKCellState.P2)
                    matrix[i][j].val = -2;
            }
        }
        set_matrix(MC);  // setMatrix increment the value of free cell that is near a marked cell
    }

    // da valori di euristica alla matrice
    public void set_matrix(MNKCell[] MC){
        for(MNKCell d : MC){
            
            // controllo tutte le celle adiacenti a d
            for(int i = -1; i < 2; i++){
                for(int j = -1; j < 2; j++){
                
                    // controllo di non andare fuori dai bounds e evitare di aumentare quando i = j = 0
                    if(    d.i + i < m && d.i + i >= 0
                        && d.j + j < n && d.j + j >= 0) {
                        
                        // controllo se la cella è libera
                        if(B.B[d.i + i][d.j + j] == MNKCellState.FREE)
                            matrix[d.i + i][d.j + j].val++;
                    }  
                }
            }
        }
    }

    // aggiorna i valori di euristica vicino alla cella e
    public void update_matrix(HeuValue e, boolean add){
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
            
                // controllo di non andare fuori dai bounds e evitare di aumentare quando i = j = 0
                if(    e.i + i < m && e.i + i >= 0
                    && e.j + j < n && e.j + j >= 0) {
                    
                    // controllo se la cella è libera
                    if(B.B[e.i + i][e.j + j] == MNKCellState.FREE){
                        if(add)
                            matrix[e.i + i][e.j + j].val++;
                        else 
                            matrix[e.i + i][e.j + j].val--;
                    }
                }  
            }
        }
        if(add) {
            if(B.B[e.i][e.j] == MNKCellState.P1)
                matrix[e.i][e.j].val = -1;
            else
                matrix[e.i][e.j].val = -2;
        }
        else{
            for(int i = -1; i < 2; i++){
                for(int j = -1; j < 2; j++){
                    // controllo di non andare fuori dai bounds e evitare di aumentare quando i = j = 0
                    if(e.i + i < m && e.i + i >= 0
                    && e.j + j < n && e.j + j >= 0) {
                
                        // controllo se la cella è libera
                        if(B.B[e.i + i][e.j + j] == MNKCellState.FREE)
                            matrix[e.i + i][e.j + j].val++;
                    }  
        
                }
            }
            matrix[e.i][e.j].val = 0;
        }
    }

    public String playerName() {
        return "abPruning_player";
    }

    public double alphaBeta(boolean isMaximising, double a, double b, int depth){
        
        MNKCell[] MC = B.getMarkedCells();
        MNKCell c = MC[MC.length-1];

        // if it runs out of time or depth is 0 return 0
        if((System.currentTimeMillis()-start)/1000.0 > TIMEOUT*(99.0/100.0) || depth == 0) 
            return 0;
        
        
        // check if there's a win 
        // MNKCell[] MC = B.getMarkedCells();
        // MNKCell c = MC[MC.length-1];
        B.unmarkCell();
        if(B.markCell(c.i, c.j) == myWin)
            return 10;
        else{
            B.unmarkCell();
            if(B.markCell(c.i, c.j) == yourWin)
                return -10;
        } 


        // check if draw 
        if(B.getFreeCells().length == 0)
            return 0;
        

        int k = 1;                     // k valori da cercare

        MaxHeap max_heap = new MaxHeap(matrix, m, n);

        // search for the win 
        if(isMaximising) { 
            double bestScore = Double.NEGATIVE_INFINITY;
            while(k < 5 && max_heap.last > 0){
                HeuValue e = max_heap.extract_max();

                if(e.val != -1 && e.val != -2){

                    B.markCell(e.i, e.j);
                    update_matrix(e, true);

                    double score = alphaBeta(false, a, b, depth-1);
                    
                    B.unmarkCell();
                    update_matrix(e, false);

                    bestScore = max(score, bestScore);
                    a = max(a, bestScore);
                    if(b <= a) break;
                }

                k++;
            } 
            return bestScore;
        }
        else {
            double bestScore = Double.POSITIVE_INFINITY;
            
            while(k < 5 && max_heap.last > 0){
                HeuValue e = max_heap.extract_max();

                if(e.val != -1 && e.val != -2){
                    
                    B.markCell(e.i, e.j);
                    update_matrix(e, true);

                    double score = alphaBeta(true, a, b, depth-1);
                    
                    B.unmarkCell();
                    update_matrix(e, false);

                    bestScore = min(score, bestScore);
                    b = min(b, bestScore);
                    if(b <= a) break;
                }
                k++;
            }
            return bestScore;
        } 
        
    }

    public double max(double a, double b) { if(a > b) return a; else return b; }
    public double min(double a, double b) { if(a < b) return a; else return b; }


    // for debugging 
    public void printMatrix(){
        System.out.println("\n");
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++)
                System.out.print(matrix[i][j].val+"\t");
            System.out.println();
        }
    }

}
