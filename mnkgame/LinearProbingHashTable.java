// Java Program to Implement Hash Tables with Linear Probing
 
// Importing all classes from
// java.util package
package mnkgame;

// Importing all input output classes
import java.io.*;
import java.util.*;
// Importing Scanner class as in do-while
// inputs are entered at run-time when
// menu is popped to user to perform desired action
 
// Helper class - LinearProbingHashTable
class LinearProbingHashTable {
    // Member variables of this class
    private int currentSize, maxSize;
    private Integer[] keys;
    private HeuValue[] vals;
 
    // Constructor of this class
    public LinearProbingHashTable(int capacity)
    {
        currentSize = 0;
        maxSize = capacity;
        keys = new Integer[maxSize];
        vals = new HeuValue[maxSize];

        for(int i = 0; i < maxSize; i++)
            keys[i] = null;
    }
 
    // Method 1
    // Function to clear hash table
    public void makeEmpty()
    {
        currentSize = 0;
        keys = new Integer[maxSize];
        vals = new HeuValue[maxSize];
        for(int i = 0; i < maxSize; i++)
            keys[i] = null;
    }
 
    // Method 2
    // Function to get size of hash table
    public int getSize() { return currentSize; }
 
    // Method 3
    // Function to check if hash table is full
    public boolean isFull()
    {
        return currentSize == maxSize;
    }
 
    // Method 4
    // Function to check if hash table is empty
    public boolean isEmpty() { return getSize() == 0; }
 
    // Method 5
    // Function to check if hash table contains a key
    public boolean contains(Integer key)
    {
        HeuValue fail = new HeuValue(0,0);
        fail.val = Integer.MIN_VALUE;
        return get(key).val != Integer.MIN_VALUE;
    }
 
    // Method 6
    // Function to get hash code of a given key
    private int hash(Integer key)
    {
        return key % maxSize; 
    }
 
    // Method 7
    // Function to insert key-value pair
    public void insert(Integer key, HeuValue val)
    {
        int tmp = hash(key);
        int i = tmp;
        
        // Do-while loop
        // Do part for performing actions
        do {
            if (keys[i] == null || keys[i] == Integer.MIN_VALUE) {
                keys[i] = key;
                vals[i] = val;
                currentSize++;
                return;
            }

            // if (keys[i].equals(key)) {
            //     vals[i] = val;
            //     return;
            // }

            i = (i + 1) % maxSize;

        }  

        // Do-while loop
        // while part for condition check
        while (i != tmp);

        
    }
 
    // Method 8
    // Function to get value for a given key
    public HeuValue get(Integer key)
    {
        int tmp = hash(key);
        int i = tmp;
        // System.out.println(tmp + "| key = " + key);

        do{
            // System.out.println(keys[i] + " | i = " + i);
            if (keys[i] != null && keys[i].compareTo(key) == 0)
                return vals[i];
            i = (i + 1) % maxSize;

        }while(keys[i] != null && i != tmp);


        HeuValue fail = new HeuValue(0,0);
        fail.val = Integer.MIN_VALUE;
        return fail;

    }
 
    // Method 9
    // Function to remove key and its value
    public void remove(Integer key)
    {
        if (!contains(key))
            return;
 
        // Find position key and delete
        int i = hash(key);
        while (key.compareTo((keys[i])) != 0)
            i = (i + 1) % maxSize;
        keys[i] = Integer.MIN_VALUE;
        HeuValue fail = new HeuValue(0,0);
        fail.val = Integer.MIN_VALUE;
        vals[i] = fail;
        System.out.println("sto rimuovendo cose!!");
        // rehash all keys
        for (i = (i + 1) % maxSize; keys[i] != null && keys[i].compareTo(Integer.MIN_VALUE) != 0;
             i = (i + 1) % maxSize) {
            Integer tmp1 = keys[i];
            HeuValue tmp2 = vals[i];
            keys[i] = Integer.MIN_VALUE;
            vals[i] = fail;
            currentSize--;
            insert(tmp1, tmp2);
        }
        currentSize--;
    }
 
    // Method 10
    // Function to print HashTable
    public void printHashTable()
    {
        System.out.println("\nHash Table: ");
        for (int i = 0; i < maxSize; i++)
            if (keys[i] != null && keys[i] != Integer.MIN_VALUE)
                System.out.println(keys[i] + " " + vals[i].val);
        System.out.println();
    }

    public static void main(String[] args) {
        LinearProbingHashTable l1 = new LinearProbingHashTable(10);
        
        int n = 10;
        int[] l_keys = new int[n];

        System.out.println("stampo chiavi:");
        for(int i = 0; i < n; i++){
            HeuValue bruno = new HeuValue(0, 0);
            bruno.val = i+3;
            l_keys[i] = i*7+2;
            System.out.print("\t"+l_keys[i]);
            l1.insert(i*7+2, bruno);
        }

        System.out.println("");
        l1.printHashTable();

        // rimuovo cazzate
        for(int i = 0; i < 10; i+=2){
            l1.remove(l_keys[i]);
            // HeuValue bruno = new HeuValue(0, 0);
            // bruno.val = i+3;
            // l1.insert(i*7+2, bruno);
        }

        System.out.println("");
        l1.printHashTable();


    }


}