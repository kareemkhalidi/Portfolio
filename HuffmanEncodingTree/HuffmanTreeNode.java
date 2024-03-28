/*
 * Author: Ben Brewer, Kareem Khalidi, and Joey Mauriello
 * File: HuffmanTreeNode.java
 * Date: 4/11/2023
 * Purpose: Node object for the Huffman Encoding Tree
 */

public class HuffmanTreeNode {

    public char c;
    public int freq;
    public HuffmanTreeNode left;
    public HuffmanTreeNode right;

    public HuffmanTreeNode(char c, int freq){
        this.c = c;
        this.freq = freq;
    }

}
