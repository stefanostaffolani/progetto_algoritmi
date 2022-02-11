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

    TranspositionTable table;

    // copy of M N 
    public int m;
    public int n;
    public int k;

    public int depth;
    public int transp_depth;

    // Default empty constructor
    public TryHeuristicPlayer() {}

    public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
        B       = new MNKBoard(M,N,K);

        myWin   = first ? MNKGameState.WINP1 : MNKGameState.WINP2;
        yourWin = first ? MNKGameState.WINP2 : MNKGameState.WINP1;

        TIMEOUT = timeout_in_secs;

        m       = M;
        n       = N;
        k       = K;

        table   = new TranspositionTable(m,n);
        matrix  = new HeuValue[m][n];

        depth = 10;
        if(depth >= 3)
            transp_depth = depth-1;
        else
            transp_depth = depth;

        // System.out.println(Integer.MIN_VALUE);
    }

    public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
        start   = System.currentTimeMillis();
        
        // System.out.println(table.get_size());

        MNKCell ret_value = new MNKCell(m/2,n/2);           // ret_value store the value to return and it's updated during the execution 
        int score = Integer.MIN_VALUE;

        if(MC.length == 0 && (m != 3 && n != 3 && k != 3)){
            B.markCell(ret_value.i, ret_value.j);
            return ret_value;
        }
        else if(MC.length == 0 && (m == 3 && n == 3 && k == 3)){
            HeuValue bruno = new HeuValue(0,0);
            ret_value = new MNKCell(bruno.i, bruno.j);
            B.markCell(ret_value.i, ret_value.j);
            return ret_value;
        }

        if(MC.length > 0) {
            MNKCell c = MC[MC.length-1];    // Recover the last move from MC
            B.markCell(c.i,c.j);            // Save the last move in the local MNKBoard
        }
        
        HeuValue tab_val = table.get_val(B.getMarkedCells());
        if(tab_val.val > Integer.MIN_VALUE){
            B.markCell(tab_val.i, tab_val.j);
            return new MNKCell(tab_val.i, tab_val.j);
        }   // dovrebbe essere ok qui

        //List<HeuValue> list = new ArrayList<HeuValue>();
        int bestScore = Integer.MIN_VALUE;
        init_matrix(MC);  // 0 if it is a free cell -1 if P1, -2 if P2

        MaxHeap max_heap = new MaxHeap(matrix, m, n);
        
        Heuristic heu = new Heuristic(matrix, max_heap, k, n, m, B);
        for(int i = 1; i < max_heap.last+1; i++){
            if(max_heap.array[i].val != 0 && max_heap.array[i].val != -1 && max_heap.array[i].val != -2){
                int A = heu.eval_the_single_pos(max_heap.array[i], -1);
                int B = heu.eval_the_single_pos(max_heap.array[i], -2);
                if(A != -1 && A != -2)
                    max_heap.array[i].val = A + Math.abs(B);
                else
                    max_heap.array[i].val = A;
            }
        }

        // max_heap.print();
        max_heap.heapify(1);
        // max_heap.print();

        int i = 1;
        // ciclo di analisi mosse, termina quando termina il tempo per selectCell 
        while((System.currentTimeMillis()-start)/1000.0 <= TIMEOUT*(99.0/100.0) && score <= 0 && i <= 5){
            HeuValue e = max_heap.array[i];

            // if(e.i == 2 && e.j == 1)
            //     System.out.println(e.i +" "+ e.j);
            // if((e.i == 2 && e.j == 4)||(e.i == 5 && e.j == 1))
            //     System.out.println(e.i +" "+ e.j + ": ev= " + e.val);
    
            if(e.val != -1 && e.val != -2 && e.val != 0){

                // System.out.println("valutazione " + cont + ":");
                // System.out.println(e.i + ", " + e.j);


                B.markCell(e.i, e.j);
                update_matrix(e, true);

                // System.out.println(B.getMarkedCells());

                tab_val = table.get_val(B.getMarkedCells());
                if(tab_val.val > Integer.MIN_VALUE){
                    // System.out.println("dio cane?");
                    if(tab_val.val == 250){
                        return new MNKCell(tab_val.i, tab_val.j);
                    }else{
                        if(score > bestScore){
                            bestScore = score;
                            ret_value = new MNKCell(e.i, e.j);
                        }
                    }
                }
                else{
                    score = alphaBeta(false, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
                    // System.out.println("score = " + score);
                    HeuValue tmp = new HeuValue(0, 0);
                    tmp.val = score;
                    table.add2tab(B.getMarkedCells(), tmp);
                    if(score > bestScore){
                        bestScore = score;
                        ret_value = new MNKCell(e.i, e.j);
                    }
                }
                update_matrix(e, false);
                B.unmarkCell();
            }
            i++;
            // printMatrix();

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
            matrix[e.i][e.j].val = 0;
            for(int i = -1; i < 2; i++){
                for(int j = -1; j < 2; j++){
                    // controllo di non andare fuori dai bounds e evitare di aumentare quando i = j = 0
                    if(e.i + i < m && e.i + i >= 0
                    && e.j + j < n && e.j + j >= 0) {
                
                        // controllo se la cella è libera
                        if(matrix[e.i + i][e.j + j].val == -1 || matrix[e.i + i][e.j + j].val == -2)
                            matrix[e.i][e.j].val++;
                    }  
        
                }
            }
        }
    }

    public String playerName() {
        return "BRUNO";
    }

    public int alphaBeta(boolean isMaximising, int a, int b, int depth){
        
        MNKCell[] MC = B.getMarkedCells();
        MNKCell c = MC[MC.length-1];
        MaxHeap max_heap = new MaxHeap(matrix, m, n);
       
        Heuristic heu = new Heuristic(matrix, max_heap, k, n, m, B);
        for(int i = 1; i < max_heap.last+1; i++){
            if(max_heap.array[i].val != 0 && max_heap.array[i].val != -1 && max_heap.array[i].val != -2){
                int A = heu.eval_the_single_pos(max_heap.array[i], -1);
                int B = heu.eval_the_single_pos(max_heap.array[i], -2);
                if(A != -1 && A != -2)
                    max_heap.array[i].val = A + Math.abs(B);
                else
                    max_heap.array[i].val = A;
            }
        }
        max_heap.heapify(1);

    
        // check if there's a win 
        // MNKCell[] MC = B.getMarkedCells();
        // MNKCell c = MC[MC.length-1];
        B.unmarkCell();
        if(B.markCell(c.i, c.j) == myWin)
            return 1000000;
        else{
            B.unmarkCell();
            if(B.markCell(c.i, c.j) == yourWin)
                return -1000000;
        } 


        // check if draw 
        if(B.getFreeCells().length == 0)
            return 0;
        

        // if it runs out of time or depth is 0 return 0
        if((System.currentTimeMillis()-start)/1000.0 > TIMEOUT*(99.0/100.0) || depth == 0) {
            Heuristic heu_board = new Heuristic(matrix, max_heap, k, n, m, B);
            int bruno = heu_board.evaluate(isMaximising);
            // System.out.println(bruno);
            return bruno;
        }
        
        // if(depth == 0){
        //     Heuristic heu = new Heuristic(matrix, max_heap, k, n, B);
        //     return heu.evaluate();
        // }

        int i = 1;
        // search for the win 
        if(isMaximising) { 
            int bestScore = Integer.MIN_VALUE;
            while(i <= 3){
                HeuValue e = max_heap.array[i];
                if(e.val != -1 && e.val != -2 && e.val != 0){

                    B.markCell(e.i, e.j);
                    update_matrix(e, true);

                    HeuValue tab_val = table.get_val(B.getMarkedCells());
                    if(tab_val.val > Integer.MIN_VALUE){  //fare prove!!!
                        update_matrix(e, false);
                        B.unmarkCell();
                        if(tab_val.val == 250){//vinco
                            return 250;
                        }else{
                            if(bestScore < tab_val.val){
                                bestScore = tab_val.val;
                            }
                        }
                    }
                    //vedere se serve B.unmark()
                    else{
                        int score = alphaBeta(false, a, b, depth-1);
                        HeuValue tmp = new HeuValue(0, 0);
                        tmp.val = score;
                        if(depth > transp_depth){   // aggiungo alla table se ho fatto almeno 15 step in depth
                            table.add2tab(B.getMarkedCells(), tmp);
                        }
                        if(score > bestScore){
                            bestScore = score;
                        }
                        a = max(a, bestScore);
                        update_matrix(e, false);
                        B.unmarkCell();
                        if(b <= a) break;
                    }
                }
                i++;
            } 
            return bestScore;
        }
        else {
            int bestScore = Integer.MAX_VALUE;
            
            while(i <= 3){
                HeuValue e = max_heap.array[i];
                if(e.val != -1 && e.val != -2 && e.val != 0){

                    B.markCell(e.i, e.j);
                    update_matrix(e, true);
        
                    HeuValue tab_val = table.get_val(B.getMarkedCells());
                    if(tab_val.val > Integer.MIN_VALUE){  //fare prove!!!
                        update_matrix(e, false);
                        B.unmarkCell();

                        if(tab_val.val == -250){//perdo
                            return -250;
                        }else{
                            if(bestScore < tab_val.val){
                                bestScore = tab_val.val;
                            }
                        }
                    }
                    //vedere se serve B.unmark()
                    else{
                        int score = alphaBeta(true, a, b, depth-1);
                        HeuValue tmp = new HeuValue(0, 0);
                        tmp.val = score;
                        if(depth > transp_depth){   // aggiungo alla table se ho fatto almeno 15 step in depth
                            table.add2tab(B.getMarkedCells(), tmp);
                        }
                        if(score < bestScore){
                            bestScore = score;
                        }
                        b = min(b, bestScore);
                        update_matrix(e, false);
                        B.unmarkCell();
                        if(b <= a) break;

                    }
                    i++;
                }
            }
            return bestScore; 
        }
    }

    public int max(int a, int b) { if(a > b) return a; else return b; }
    public int min(int a, int b) { if(a < b) return a; else return b; }






    // for debugging 
    public void printMatrix(){
        System.out.println("\n");
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++)
                System.out.print(matrix[i][j].val+"\t");
            System.out.println();
        }
    }

}
