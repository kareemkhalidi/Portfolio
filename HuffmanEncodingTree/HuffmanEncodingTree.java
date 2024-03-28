/*
 * Author: Ben Brewer, Kareem Khalidi, and Joey Mauriello
 * File: HuffmanEncodingTree.java
 * Date: 4/11/2023
 * Purpose: Implement a Huffman Encoding Tree to be used in Huffman Encoding
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class HuffmanEncodingTree
{
    //the minPQ array
    private HuffmanTreeNode[] pq;

    //the size of the minPQ
    private int size = 0;

    //the capacity of the minPQ array
    private int capacity;

    //the root node for the Huffman Encoding Tree
    private HuffmanTreeNode root;

    /*
     * Method: getEncodingMap
     * Purpose: returns the encoding map
     * Parameters: none
     * Returns: the encoding map
     * */
    public HashMap<Character, String> getEncodingMap() {
        return encodingMap;
    }

    /*
     * Method: getDecodingMap
     * Purpose: returns the decoding map
     * Parameters: none
     * Returns: the decoding map
     * */
    public HashMap<String, Character> getDecodingMap() {
        return decodingMap;
    }

    ///HashMap that stores the encoding information for each character
    private HashMap<Character, String> encodingMap;

    ///HashMap that stores the decoding information for each character
    private HashMap<String, Character> decodingMap;

    //HashMap that stores the frequency of each character in the input file
    private HashMap<Character, Integer> charFrequency;

    /*
     * Constructor
     * Purpose: Takes in an inputFile, builds a minPQ using the frequencies of each character in the input file as the
     *          priority, and uses the minPQ to build the Huffman Encoding Tree, as well as the encoding and decoding maps.
     * */
    public HuffmanEncodingTree(String inputFile) throws IOException, EmptyQueueException {
        //get the text from the input file
        String text = parseText(inputFile);
        //build the character frequency map
        charFrequency = buildCharacterFrequencyDict(text);
        capacity = charFrequency.size();
        //build the minPQ
        pq = new HuffmanTreeNode[capacity];
        for (Map.Entry<Character, Integer> entry : charFrequency.entrySet())
        {
            insert(new HuffmanTreeNode(entry.getKey(), entry.getValue()));
        }
        //build the Huffman Encoding Tree and encoding and decoding maps
        this.root = BuildTree();
        this.encodingMap = new HashMap<Character, String>();
        this.decodingMap = new HashMap<String, Character>();
        buildMaps();
        //set the pq and charFrequency to null to free up memory
        pq = null;
        charFrequency = null;
    }

    /*
     * Method: Encode
     * Purpose: encodes the input string
     * Parameters: input - the string to be encoded
     * Returns: the encoded string
     * */
    public String Encode(String input){
        String cur = "";
        for(int i = 0; i < input.length(); i++){
            cur += encodingMap.get(input.charAt(i));
        }
        return cur;
    }

    /*
     * Method: Decode
     * Purpose: decodes the input string
     * Parameters: input - the string to be decoded
     * Returns: the decoded string
     * */
    public String Decode(String input){
        String output = "";
        int currIndex = 0;
        while (currIndex < input.length()) {
            String currCode = "";
            while (!decodingMap.containsKey(currCode)) {
                currCode += input.charAt(currIndex);
                currIndex++;
            }
            if (decodingMap.get(currCode) != null) {
                output += decodingMap.get(currCode);
            }
        }
        return output;
    }

    /*
     * Method: BuildTree
     * Purpose: builds the huffman tree
     * Parameters: none
     * */
    private HuffmanTreeNode BuildTree() throws EmptyQueueException {
        while(size > 1){
            HuffmanTreeNode smallest = delMin();
            HuffmanTreeNode secondSmallest = delMin();
            HuffmanTreeNode newNode = new HuffmanTreeNode('\0', smallest.freq + secondSmallest.freq);
            newNode.left = smallest;
            newNode.right = secondSmallest;
            insert(newNode);
        }
        return(delMin());
    }

    /*
     * Method: buildMaps
     * Purpose: builds the encoding and decoding maps
     * Parameters: none
     * */
    private void buildMaps(){
        buildMaps(root, "");
    }

    /*
     * Method: buildMaps
     * Purpose: builds the encoding and decoding maps
     * Parameters: HuffmanTreeNode cur - the current node
     *             String encoding - the encoding of the current node
     * */
    private void buildMaps(HuffmanTreeNode cur, String encoding){
        if(cur.left == null && cur.right == null){
            encodingMap.put(cur.c, encoding);
            decodingMap.put(encoding, cur.c);
        }
        else{
            buildMaps(cur.left, encoding + "0");
            buildMaps(cur.right, encoding + "1");
        }
    }

    /*
     * Method: parseText
     * Purpose: parses the text from a file
     * Parameters: String fileName - the name of the file to be parsed
     * Returns: String - the text from the file
     * */
    private static String parseText(String fileName) throws FileNotFoundException
    {
        String text = "";
        File file = new File(fileName);
        Scanner input = new Scanner(file);
        while (input.hasNextLine())
        {
            text += input.nextLine();
            text += "\n";
        }
        return text;
    }

    /*
     * Method: getCharFrequency
     * Purpose: returns a dictionary of characters and their frequencies
     * Parameters: String text - the text to be analyzed
     * Returns: HashMap - a dictionary of the characters in text and their frequencies
     * */
    private static HashMap buildCharacterFrequencyDict(String text)
    {
        // create a dictionary of characters and their frequencies
        HashMap<Character, Integer> charFrequency = new HashMap<Character, Integer>();
        for (int i = 0; i < text.length(); i++)
        {
            if (charFrequency.containsKey(text.charAt(i)))
            {
                charFrequency.put(text.charAt(i), charFrequency.get(text.charAt(i)) + 1);
            }
            // char not in dict yet so add it
            else
            {
                charFrequency.put(text.charAt(i), 1);
            }
        }
        return charFrequency;
    }

    /*
     * Method: insert
     * Purpose: inserts a new patient into the MaxPQ
     * Parameters: Patient item - the patient to be inserted
     * Returns: void
     */
    private void insert(HuffmanTreeNode item)
    {
        // best: O(logN), worst: O(N)--for resizing, amortized: O(logN)
        if (pq.length == size)
        {
            resize(2 * size);
        }
        pq[size] = item;
        swim(size);
        size++;
    }

    /*
     * Method: delMax
     * Purpose: deletes and returns the patient with the highest priority
     * Parameters: none
     * Returns: Patient - the patient with the highest priority
     */
    private HuffmanTreeNode delMin() throws EmptyQueueException
    {
        // best: O(logN), worst: O(N)--for resizing amortized: O(logN)
        if (isMinPQEmpty())
        {
            throw new EmptyQueueException();
        }
        HuffmanTreeNode retval = pq[0];
        pq[0] = pq[size - 1];
        sink(0);
        size--;
        if ((size < (pq.length / 4)) && ((pq.length / 2) > capacity))
        {
            resize(pq.length / 2);
        }
        return retval;
    }

    /*
     * Method: isEmpty
     * Purpose: returns true if the MaxPQ is empty, false otherwise
     * Parameters: none
     * Returns: boolean - true if the MaxPQ is empty, false otherwise
     */
    private boolean isMinPQEmpty()
    {
        if (size == 0)
        {
            return true;
        }
        return false;
    }

    /*
     * Method: resize
     * Purpose: resizes the MaxPQ to the capacity of the parameter
     * Parameters: int capacity - the new capacity of the MaxPQ
     * Returns: void
     */
    private void resize(int capacity)
    {
        HuffmanTreeNode[] tempPQ = new HuffmanTreeNode[capacity];
        for (int i = 0; i < size; i++)
        {
            tempPQ[i] = pq[i];
        }
        pq = tempPQ;
    }

    /*
     * Method: swim
     * Purpose: bubbles the node at the index i up the heap until it is in the correct position
     * Parameters: int i - the index of the node to be bubbled up
     * Returns: void
     */
    private void swim(int i)
    {
        int parent = parent(i);
        if(parent > 0 && pq[i].freq - pq[parent].freq < 0){
            swap(i, parent);
            swim(parent);
        }
        if(parent == 0 && pq[i].freq - pq[parent].freq < 0){
            swap(i, parent);
        }
    }

    /*
     * Method: sink
     * Purpose: bubbles the node at the index i down the heap until it is in the correct position
     * Parameters: int i - the index of the node to be bubbled down
     * Returns: void
     */
    private void sink(int i)
    {
        int left = leftChild(i);
        int right = rightChild(i);
        int largest = i;
        if(left < size && pq[left].freq - pq[i].freq < 0) {
            largest = left;
        }
        if(right < size && pq[right].freq - pq[largest].freq < 0){
            largest = right;
        }
        if(largest != i){
            swap(i, largest);
            sink(largest);
        }
    }

    /*
     * Method: swap
     * Purpose: swaps the nodes at the indices i and j
     * Parameters: int i - the index of the first node to be swapped
     *             int j - the index of the second node to be swapped
     * Returns: void
     */
    private void swap(int i, int j)
    {
        HuffmanTreeNode temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    /*
     * Method: parent
     * Purpose: returns the index of the parent of the node at the index i
     * Parameters: int i - the index of the node whose parent is to be returned
     * Returns: int - the index of the parent of the node at the index i
     */
    private int parent(int i)
    {
        return (i - 1) / 2;
    }

    /*
     * Method: leftChild
     * Purpose: returns the index of the left child of the node at the index i
     * Parameters: int i - the index of the node whose left child is to be returned
     * Returns: int - the index of the left child of the node at the index i
     */
    private int leftChild(int i)
    {
        return 2 * i + 1;
    }

    /*
     * Method: rightChild
     * Purpose: returns the index of the right child of the node at the index i
     * Parameters: int i - the index of the node whose right child is to be returned
     * Returns: int - the index of the right child of the node at the index i
     */
    private int rightChild(int i)
    {
        return 2 * i + 2;
    }

    /*
     * Method: printTree
     * Purpose: prints the tree in a readable format
     * Parameters: none
     * Returns: void
     */
    public void printTree(){
        printTree(root, 0, "ROOT");
    }

    /*
     * Method: printTree
     * Purpose: prints the tree in a readable format
     * Parameters: HuffmanTreeNode cur - the current node being printed
     *             int depth - the depth of the current node
     *             String pos - the position of the current node
     * Returns: void
     */
    private void printTree(HuffmanTreeNode cur, int depth, String pos){
        for(int i = 0; i < depth; i++)
            System.out.print("    ");
        if(depth != 0){
            System.out.print(" └──");
        }
        System.out.print(pos);
        if(cur.c != '\0'){
            if(cur.c == '\n'){
                System.out.println(" (\\n:" + cur.freq + ")");
            }
            else{
                System.out.println(" (" + cur.c + ":" + cur.freq + ")");
            }
        }
        else{
            System.out.println(" (" + cur.freq + ")");
        }
        if(cur.left != null)
            printTree(cur.left, depth + 1, "LEFT(0)");
        if(cur.right != null)
            printTree(cur.right, depth + 1, "RIGHT(1)");
    }

}



