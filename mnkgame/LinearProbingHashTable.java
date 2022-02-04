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
import java.util.Scanner;
 
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
    }
 
    // Method 1
    // Function to clear hash table
    public void makeEmpty()
    {
        currentSize = 0;
        keys = new Integer[maxSize];
        vals = new HeuValue[maxSize];
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
        HeuValue schifo = new HeuValue(0,0);
        schifo.val = Integer.MIN_VALUE;
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
            if (keys[i] == Integer.MIN_VALUE) {
                keys[i] = key;
                vals[i] = val;
                currentSize++;
                return;
            }
 
            if (keys[i].equals(key)) {
                vals[i] = val;
                return;
            }
 
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
        int i = hash(key);
        while (keys[i] != Integer.MIN_VALUE) {
            if (keys[i].equals(key))
                return vals[i];
            i = (i + 1) % maxSize;
        }
        HeuValue schifo = new HeuValue(0,0);
        schifo.val = Integer.MIN_VALUE;
        return schifo;

    }
 
    // Method 9
    // Function to remove key and its value
    public void remove(Integer key)
    {
        if (!contains(key))
            return;
 
        // Find position key and delete
        int i = hash(key);
        while (!key.equals(keys[i]))
            i = (i + 1) % maxSize;
        keys[i] = Integer.MIN_VALUE;
        HeuValue schifo = new HeuValue(0,0);
        schifo.val = Integer.MIN_VALUE;
        vals[i] = schifo;
 
        // rehash all keys
        for (i = (i + 1) % maxSize; keys[i] != Integer.MIN_VALUE;
             i = (i + 1) % maxSize) {
            Integer tmp1 = keys[i];
            HeuValue tmp2 = vals[i];
            keys[i] = Integer.MIN_VALUE;
            vals[i] = schifo;
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
            if (keys[i] != Integer.MIN_VALUE)
                System.out.println(keys[i] + " " + vals[i]);
        System.out.println();
    }
}