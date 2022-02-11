package mnkgame;

public class Heuristic {
    
    HeuValue[][] matrix;
    MaxHeap max_heap;
    int k;
    int n;
	int m;
    MNKBoard B;
	

    Heuristic(HeuValue[][] mat, MaxHeap mh, int k, int n, int m, MNKBoard B){
        this.matrix = mat;
        this.max_heap = mh;
        this.k = k;
        this.n = n;
		this.m = m;
		this.B = B;
    }

    public void set_max_heap(MaxHeap mh){ max_heap = mh; }
    public void set_matrix(HeuValue[][] mat){ matrix = mat; }

    public int evaluate(boolean isMaximising){
		//if(isMaximising)
			return eval_pos(-1) + eval_pos(-2);	// evaluate the pos for player 1
		// else
		// 	return eval_pos(-2);	// evaluate the pos for player 2
	}

    public int eval_pos(int p1){
	
		int ret_sum = 0; // somma di valori da ritornare
		
		for (int i = 1; i < max_heap.last + 1; i++) {	// chiedere a ste se è giusto
			if(max_heap.array[i].val != -1 && max_heap.array[i].val != -2){
                ret_sum += evaluate_row(p1, max_heap.array[i]);
			    ret_sum += evaluate_column(p1, max_heap.array[i]);
			    ret_sum += evaluate_diagonal1(p1, max_heap.array[i]);
				ret_sum += evaluate_diagonal2(p1, max_heap.array[i]);
            }
		}
		
		return ret_sum;
	
	}

	public int eval_the_single_pos(HeuValue e, int p1){
		int ret_sum = e.val;
		if(e.val != -2 && e.val != -1){
			ret_sum += evaluate_row(p1, e);
			ret_sum += evaluate_column(p1, e);
			ret_sum += evaluate_diagonal1(p1, e);
			ret_sum += evaluate_diagonal2(p1, e);
		}
		return ret_sum;
	}


	public int get_heuristic_value(boolean free_cell_1, boolean free_cell_2, int cont, int p1){
		
		if(cont + 1 == k){
			if(p1 == -1)
				return 1000000;
			else
				return -1000000;
		}	
		
		// se la mia serie di mosse è estendibile sia destra che a sinistra e arrivo a k-1 ho vinto
		if(free_cell_1 && free_cell_2 && (cont + 1 == k-1)){
			if(p1 == -1)
				return 10000;
			else
				return -10000;
		} 
		if(free_cell_1 && free_cell_2 && (cont + 1 == k-2)){
			if(p1 == -1)
				return 100;
			else
				return -100;
		} 
		
		// se la mia serie di mosse è estendibile e arrivo a k-2 o k-1
		if(free_cell_1 || free_cell_2){

			if(cont + 1 == k-2){
				if(p1 == -1)
					return 20;
				else
					return -20;
			}
			else if(cont + 1 == k-1){
				if(p1 == -1)
					return 10000;
				else
					return -10000;	
			}
		
		}

		// se la mia serie non è estendibile allora tutto a mucchio
		return 0;
	}

    public int evaluate_row(int p1, HeuValue e){
        boolean free_cell_1 = false;	// controllo free_cell a sinistra
		boolean free_cell_2 = false;	// controllo free_cell a destra
		int cont = 0;
		int iter = 1;
	
		// cont left:
		while(true){
			//se sbatto contro il muro a sinistra
			if(e.j - iter < 0) break;
			
			//se finisco contro una casella avversaria
			if(matrix[e.i][e.j-iter].val != p1 && B.B[e.i][e.j-iter] != MNKCellState.FREE) break;
			
			// se finisco contro una cella libera & non è ancora accaduto 
			if(B.B[e.i][e.j-iter] == MNKCellState.FREE && !free_cell_1){
				free_cell_1 = true;
				break;
			}
			
			cont += 1;
            iter += 1;
		}
		
		// cont right
		iter = 1;
		while(true){
			//se sbatto contro il muro a sinistra
			if(e.j + iter > n-1) break;
			
			//se finisco contro una casella avversaria
			if(matrix[e.i][e.j+iter].val != p1 && B.B[e.i][e.j+iter] != MNKCellState.FREE) break;
			
			// se finisco contro una cella libera  
			if(B.B[e.i][e.j+iter] == MNKCellState.FREE && !free_cell_2){
				free_cell_2 = true;
				break;
			}
			
			cont += 1;
			iter += 1;
		}
		
		return get_heuristic_value(free_cell_1, free_cell_2, cont, p1);
    }

    public int evaluate_column(int p1, HeuValue e){
		boolean free_cell_1 = false;	// controllo free_cell a sinistra
		boolean free_cell_2 = false;	// controllo free_cell a destra
		int cont = 0;
		int iter = 1;
	
		// cont alto:
		while(true){
			//se sbatto contro il muro in alto
			if(e.i - iter < 0) break;
			
			//se finisco contro una casella avversaria
			if(matrix[e.i-iter][e.j].val != p1 && B.B[e.i-iter][e.j] != MNKCellState.FREE) break;
			
			// se finisco contro una cella libera & non è ancora accaduto 
			if(B.B[e.i-iter][e.j] == MNKCellState.FREE && !free_cell_1){
				free_cell_1 = true;
				break;
			}
			
			cont += 1;
            iter += 1;
		}
		
		// cont basso
		iter = 1;
		while(true){
			//se sbatto contro il muro basso
			if(e.i + iter > m-1) break;
			
			//se finisco contro una casella avversaria
			if(matrix[e.i+iter][e.j].val != p1 && B.B[e.i+iter][e.j] != MNKCellState.FREE) break;
			
			// se finisco contro una cella libera & non è ancora accaduto 
			if(B.B[e.i+iter][e.j] == MNKCellState.FREE && !free_cell_2){
				free_cell_2 = true;
				break;
			}
			
			cont += 1;
			iter += 1;
		}
		
		return get_heuristic_value(free_cell_1, free_cell_2, cont, p1);
    }

    public int evaluate_diagonal1(int p1, HeuValue e){
		boolean free_cell_1 = false;	// controllo free_cell a sinistra
		boolean free_cell_2 = false;	// controllo free_cell a destra
		int cont = 0;
		int iter = 1;
	
		// cont in alto a destra:
		while(true){
			//se sbatto contro il muro in alto a destra
			if(e.j + iter > n-1 || e.i - iter < 0) break;
			
			//se finisco contro una casella avversaria
			if(matrix[e.i-iter][e.j+iter].val != p1 && B.B[e.i-iter][e.j+iter] != MNKCellState.FREE) break;
			
			// se finisco contro una cella libera & non è ancora accaduto 
			if(B.B[e.i-iter][e.j+iter] == MNKCellState.FREE && !free_cell_1){
				free_cell_1 = true;
				break;
			}
			
			cont += 1;
            iter += 1;
		}
		
		// cont right
		iter = 1;
		while(true){
			//se sbatto contro il muro in basso a sinstra
			if(e.j - iter < 0 || e.i + iter > m-1) break;
			
			//se finisco contro una casella avversaria
			if(matrix[e.i+iter][e.j-iter].val != p1 && B.B[e.i+iter][e.j-iter] != MNKCellState.FREE) break;
			
			// se finisco contro una cella libera & non è ancora accaduto 
			if(B.B[e.i+iter][e.j-iter] == MNKCellState.FREE && !free_cell_2){
				free_cell_2 = true;
				break;
			}
			
			cont += 1;
			iter += 1;
		}
		
		return get_heuristic_value(free_cell_1, free_cell_2, cont, p1);
    }

	public int evaluate_diagonal2(int p1, HeuValue e){
		boolean free_cell_1 = false;	// controllo free_cell a sinistra
		boolean free_cell_2 = false;	// controllo free_cell a destra
		int cont = 0;
		int iter = 1;
	
		// cont in alto a sinistra
		while(true){
			//se sbatto contro il muro in alto a sinistra
			if(e.j - iter < 0 || e.i - iter < 0) break;
			
			//se finisco contro una casella avversaria
			if(matrix[e.i-iter][e.j-iter].val != p1 && B.B[e.i-iter][e.j-iter] != MNKCellState.FREE) break;
			
			// se finisco contro una cella libera & non è ancora accaduto 
			if(B.B[e.i-iter][e.j-iter] == MNKCellState.FREE && !free_cell_1){
				free_cell_1 = true;
				break;
			}
			
			cont += 1;
            iter += 1;
		}
		
		// cont in basso a destra
		iter = 1;
		while(true){
			//se sbatto contro il muro in basso a sinstra
			if(e.j + iter > n-1 || e.i + iter > m-1) break;
			
			//se finisco contro una casella avversaria
			if(matrix[e.i+iter][e.j+iter].val != p1 && B.B[e.i+iter][e.j+iter] != MNKCellState.FREE) break;
			
			// se finisco contro una cella libera & non è ancora accaduto 
			if(B.B[e.i+iter][e.j+iter] == MNKCellState.FREE && !free_cell_2){
				free_cell_2 = true;
				break;
			}
			
			cont += 1;
			iter += 1;
		}
		
		return get_heuristic_value(free_cell_1, free_cell_2, cont, p1);
	}

}
