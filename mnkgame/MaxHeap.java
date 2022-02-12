package mnkgame;

import java.util.Random;

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
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                array[i*n+j+1] = new HeuValue(i, j);
                array[i*n+j+1].val = matrix[i][j].val;
            }
        }
        last = m*n;
        heapify(1);
    }

    public void heap_sort()
    {
        heapify(1);
        // One by one extract an element from heap
        for (int i=last-1; i>=0; i--)
        {
            // Move current root to end
            int temp = array[0].val;
            array[0].val = array[i].val;
            array[i].val = temp;
  
            // call max heapify on the reduced heap
            heapify(1);
        }
    }

    public HeuValue extract_max(){
        HeuValue tmp = array[1];
        array[1] = array[last];
        array[last] = tmp;
        last--;
        //System.out.println(last);
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
        // System.out.println();
        // for(int i = 1; i < last+1; i++)
        //     System.out.print(array[i].i+", "+ array[i].j + "\t");
        System.out.println();

    }

    public static void main(String[] args) {
        Random rand = new Random();
        HeuValue[][] m = new HeuValue[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                m[i][j] = new HeuValue(i, j);
                m[i][j].val = rand.nextInt(100);
                System.out.print(m[i][j].val + "\t");
            }
            System.out.println();
        }

        System.out.println("now i gonna put the element in a heap: ");
        MaxHeap heap = new MaxHeap(m, 4, 4);
        heap.print();
        heap.heap_sort();
        System.out.println("now i gonna sort the fucking element: ");
        heap.print();

    }
}