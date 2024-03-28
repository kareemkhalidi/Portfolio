/*
 * Author: Ben Brewer, Kareem Khalidi, and Joey Mauriello
 * File: HuffmanGUI.java
 * Date: 4/11/2023
 * Purpose: Graphical User Interface for the Huffman Encoding algorithm
 */

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class HuffmanGUI extends JPanel{
    private File file;
    private JTextArea textArea;
    private JTextArea codeArea;
    private HuffmanEncodingTree t;
    public HuffmanGUI() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame mainFrame = new JFrame("Huffman Encoder");
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(new GridLayout(3, 3, 5, 5));

        JLabel text = new JLabel("Text:");
        JLabel code = new JLabel("Code:");

        textArea = new JTextArea(10,10);
        codeArea = new JTextArea(10, 10);
        textArea.setLineWrap(true);
        codeArea.setLineWrap(true);

        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener((event) -> {
            if (t != null) {
                String originalText = textArea.getText();
                String originalCode = codeArea.getText();

                if (!originalText.isEmpty() && originalCode.isEmpty()) {
                    String newCode = t.Encode(originalText);
                    codeArea.setText(newCode);
                }
                else if (originalText.isEmpty() && !originalCode.isEmpty()) {
                    String newText = t.Decode(originalCode);
                    textArea.setText(newText);
                }
            }
        });

        JButton fileButton = new JButton("Select file");
        fileButton.addActionListener((event) -> {
            JFileChooser j = new JFileChooser();
            int returnVal = j.showOpenDialog(HuffmanGUI.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = j.getSelectedFile();
                try {
                    t = new HuffmanEncodingTree(file.getAbsolutePath());
                } catch (IOException | EmptyQueueException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        mainFrame.add(text);
        mainFrame.add(textArea);
        mainFrame.add(code);
        mainFrame.add(codeArea);
        mainFrame.add(fileButton);
        mainFrame.add(convertButton);
        mainFrame.setVisible(true);

    }
}

