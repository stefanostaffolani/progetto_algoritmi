package mnkgame;


class LinearProbingHashTable {
    private int currentSize, maxSize;
    private Integer[] keys;
    private HeuValue[] vals;
 
    // Costruttore 
    public LinearProbingHashTable(int capacity){
        currentSize = 0;
        maxSize = capacity;
        keys = new Integer[maxSize];
        vals = new HeuValue[maxSize];

        for(int i = 0; i < maxSize; i++)
            keys[i] = null;
    }
 
    // Svuota la table
    public void makeEmpty(){
        currentSize = 0;
        keys = new Integer[maxSize];
        vals = new HeuValue[maxSize];
        for(int i = 0; i < maxSize; i++)
            keys[i] = null;
    }
 
    public int getSize() { return currentSize; }
 
    public boolean isFull(){ return currentSize == maxSize; }
 
    public boolean isEmpty() { return getSize() == 0; }
 
    public boolean contains(Integer key){
        HeuValue fail = new HeuValue(0,0);
        fail.val = Integer.MIN_VALUE;
        return get(key).val != Integer.MIN_VALUE;
    }
 
    private int hash(Integer key) { return key % maxSize; }
 
    public void insert(Integer key, HeuValue val){
        int tmp = hash(key);
        int i = tmp;
        do { // Per le celle deleted usiamo Integer.MIN_VALUE
            if (keys[i] == null || keys[i] == Integer.MIN_VALUE) {  // scorri fino a null o fino a MIN_VALUE
                keys[i] = key;
                vals[i] = val;
                currentSize++;
                return;
            }
            i = (i + 1) % maxSize;
        } while (i != tmp);   //scorri fino a che non sei tornato sulla stessa cella
    }
 
    public HeuValue get(Integer key){
        int tmp = hash(key);
        int i = tmp;
        do{
            if (keys[i] != null && keys[i].compareTo(key) == 0)  
                return vals[i];
            i = (i + 1) % maxSize;

        } while(keys[i] != null && i != tmp);    //scorri fino a NULL o fino a che non sei tornato sulla stessa cella
        HeuValue fail = new HeuValue(0,0);
        fail.val = Integer.MIN_VALUE;
        return fail;
    }
 
    public void remove(Integer key){
        if (!contains(key))   // se non e' presente fermati
            return;

        int i = hash(key);
        while (key.compareTo((keys[i])) != 0)   // scorri fino alla posizione della key
            i = (i + 1) % maxSize;
        keys[i] = Integer.MIN_VALUE;   // setta a Delete
        HeuValue fail = new HeuValue(0,0);
        fail.val = Integer.MIN_VALUE;
        vals[i] = fail;
        currentSize--;
    }
 
    public void printHashTable(){
        System.out.println("\nHash Table: ");
        for (int i = 0; i < maxSize; i++)
            if (keys[i] != null && keys[i] != Integer.MIN_VALUE)
                System.out.println(keys[i] + " " + vals[i].val);
        System.out.println();
    }
}