/*
 * Authors: Ben Brewer, Kareem Khalidi, and Joey Mauriello
 * File: HuffmanEncodingTest.java
 * Date: 4/11/23
 * Purpose: This file is responsible for testing the Huffman Encoding
 * algorithm in a real world scenario. It reads in a command line argument
 * telling it how many testcases to run. The code expects that each test case
 * is in a file named testX.txt where X is the test case number. For example, if
 * the program recieves the command line argument 3, it will try to run test1.txt,
 * test2.txt, and test3.txt. The code will print out the original text, the encoding
 * map, the decoding map, the huffman encoding tree, the encoded text, the original
 * text size, the encoded text size, and the space saved. The code will also print
 * out the test case number.
 *
 * The code also reads in a second command line argument (y/n) that tells the code
 * whether to begin an interactive session. If the user enters y, the code
 * will generate a GUI that allows the user to enter text and see the encoded text
 * and vice versa. If the user enters n, the code will simply run the test cases
 * and exit.
 */

import java.io.*;
import java.util.*;
public class HuffmanEncodingTest
{

    public static void main(String[] args) throws IOException, EmptyQueueException {
        for(int i = 1; i <= Integer.parseInt(args[0]); i++){
            runTestCaseX(i, "test" + i + ".txt");
        }
        if(args[1].equals("y")){
            HuffmanGUI gui = new HuffmanGUI();
        }
    }

    /*
     * Method: runTestCaseX
     * Purpose: This method is responsible for running a test case
     * Parameters: int x - the test case number
     *            String fileName - the name of the file to be used
     * Returns: void
     * */
    public static void runTestCaseX(int x, String fileName) throws EmptyQueueException, IOException {
        System.out.println("*** TEST CASE " + x + " ***");
        HuffmanEncodingTree testTree = new HuffmanEncodingTree(fileName);
        String test1Text = parseText(fileName);
        System.out.println("ORIGINAL TEXT\n" + test1Text);
        System.out.println("ENCODING MAP\n" + testTree.getEncodingMap());
        System.out.println();
        System.out.println("DECODING MAP\n" + testTree.getDecodingMap());
        System.out.println();
        System.out.println("HUFFMAN ENCODING TREE");
        testTree.printTree();
        System.out.println();
        System.out.println("ENCODED TEXT\n" + testTree.Encode(test1Text));
        System.out.println();
        int originalBits = test1Text.length() * 8;
        int encodedBits = testTree.Encode(test1Text).length();
        System.out.println("ORIGINAL TEXT SIZE: " + originalBits + " bits");
        System.out.println("ENCODED TEXT SIZE: " + encodedBits + " bits");
        System.out.println("SPACE SAVED: " + (originalBits - encodedBits) + " bits (" + ((originalBits - encodedBits) / (double)originalBits) * 100 + "%)");
        System.out.println();
    }

    /*
     * Method: parseText
     * Purpose: This method is responsible for parsing a text file
     * Parameters: String fileName - the name of the file to be parsed
     * Returns: String - the text from the file
     * */
    public static String parseText(String fileName) throws FileNotFoundException
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

}