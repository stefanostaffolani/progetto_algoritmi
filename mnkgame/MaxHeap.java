package mnkgame;

// Main class
public class MaxHeap {

    HeuValue[] array;
    int last;
    int m;
    int n;
    
    MaxHeap(HeuValue[][] matrix, int m, int n){
        this.m = m;
        this.n = n;
        array = new HeuValue[m*n+1];
        last = 0;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(matrix[i][j].val != 0 && matrix[i][j].val != -1 && matrix[i][j].val != -2){   // prendo solo i valori di rank
                    last++;
                    array[last] = new HeuValue(i, j);
                    array[last].val = matrix[i][j].val;
                }
            }
        }
        heapify(1);
    }

    public HeuValue extract_max(){
        HeuValue tmp = array[1];
        array[1] = array[last];
        array[last] = tmp;
        last--;
        fix_heap(1);
        return tmp;
    }

    public void fix_heap(int i){
        int max = 2 * i; // mette il figlio sinistro come max, variabile d'appoggio
        if(2 * i > last) return;
        if(2 * i + 1 <= last && array[2*i].val < array[2*i + 1].val)
            max = 2*i+1;
        if(array[i].val < array[max].val){
            HeuValue tmp = array[max];
            array[max] = array[i];
            array[i] = tmp;
            fix_heap(max);
        }
    }

    public void heapify(int i){
        if(i > m*n+1) return;
        heapify(2*i);
        heapify(2*i + 1);
        fix_heap(i);
    }

    public void print(){
        for(int i = 1; i < last+1; i++)
            System.out.print(array[i].val + ": " + array[i].i + "," + array[i].j + "\n");
        System.out.println();
    }
}