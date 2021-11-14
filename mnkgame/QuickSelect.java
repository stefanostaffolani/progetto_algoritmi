
package mnkgame;

class QuickSelect {
    public HeuValue kthGreatest(HeuValue[][] matrix, int k) {
        // treat 2D matrix simply as 1D array and perform quick select
        if(k > matrix.length * matrix.length) return matrix[0][0];
        return partition(matrix, 0, matrix.length * matrix.length - 1, k);
    }

    // To convert from 1D index back to 2D (rid, cid):
    // rid = index / cols
    // cid = index % cols
    private HeuValue partition(HeuValue[][] matrix, int start, int end, int k) {
        int cols = matrix[0].length;
        if (start == end)   {
            return matrix[start / cols][start % cols];
        }

        // use middle element as the pivot
        int mid = start + (end - start) / 2;
        int pivot = matrix[mid / cols][mid % cols].val;

        int l = start, r = end;
        while (l <= r) {
            while (l <= r && matrix[l / cols][l % cols].val > pivot)    l++;
            while (l <= r && matrix[r / cols][r % cols].val < pivot)    r--;
            if (l <= r) {
                // scambio di val
                int temp = matrix[l / cols][l % cols].val;
                matrix[l / cols][l % cols].val = matrix[r / cols][r % cols].val;
                matrix[r / cols][r % cols].val = temp;
                // scambio di pos i
                temp = matrix[l / cols][l % cols].i;
                matrix[l / cols][l % cols].i = matrix[r / cols][r % cols].i;
                matrix[r / cols][r % cols].i = temp;
                // scambio di pos j 
                temp = matrix[l / cols][l % cols].j;
                matrix[l / cols][l % cols].j = matrix[r / cols][r % cols].j;
                matrix[r / cols][r % cols].j = temp;

                l++;
                r--;
            }
        }

        // decide which part kth smallest element falls to
        if (start + k - 1 <= r) return partition(matrix, start, r, k);
        if (start + k - 1 >= l) return partition(matrix, l, end, k - l + start);
        return matrix[(r + 1) / cols][(r + 1) % cols];
    }
}
