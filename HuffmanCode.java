// Vikram Murali

import java.util.*;
import java.io.*;

// The HuffmanCode class constructs and manages a Huffman
// tree for encoding and decoding text based on character frequencies
public class HuffmanCode {
    
    private HuffmanNode root;

    // Represents a node in the Huffman tree.
    private static class HuffmanNode implements Comparable<HuffmanNode>{

        public final int frequency;
        public final char letter;
        public HuffmanNode left;
        public HuffmanNode right;

        // Constructs a HuffmanNode with the specified frequency and 
        // letter, setting left and right children to null
        // Parameters:
        //  frequency: the frequency of the character
        //  letter: the character stored in this node
        public HuffmanNode(int frequency, char letter) {
            this(frequency, letter, null, null);
        }

        // Constructs a HuffmanNode with the specified frequency, letter, 
        // left child, and right child
        // Parameters:
        //  frequency: the frequency of the character
        //  letter: the character stored in this node
        //  left: the left child of this node
        //  right: the right child of this node
        public HuffmanNode(int frequency, char letter, HuffmanNode left, HuffmanNode right){
            this.frequency = frequency;
            this.letter = letter;
            this.left = left;
            this.right = right;
        }

        // Checks if the node is a leaf, meaning it has no children
        // Returns true if the node is a leaf, false otherwise
        public boolean isLeaf(){
            return (this.left == null && this.right == null);
        }

        // Compares this node to another node based on frequency
        // Parameters:
        //  other: the other node to compare to
        // Returns a negative integer, zero, or a positive integer to see if this 
        // node's frequency 
        // is less than, equal to, or greater than the other node's frequency
        public int compareTo(HuffmanNode other){
            return (this.frequency - other.frequency);
        }
    }

    // Constructs a HuffmanCode object using an array of character frequencies
    // Parameters:
    //  frequencies: an array representing the frequency of each character,
    //  where the index corresponds to the character's ASCII value
    public HuffmanCode(int[] frequencies){
        buildTree(frequencies);
    }

    // Builds the Huffman tree using the provided character frequencies
    // Parameters: 
    //  frequencies: an array representing the frequency of each character,
    //  where the index corresponds to the character's ASCII value
    private void buildTree(int[] frequencies) {
        Queue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                priorityQueue.add(new HuffmanNode(frequencies[i], (char) i));
            }
        }
        while (priorityQueue.size() > 1) {
            HuffmanNode min1 = priorityQueue.remove();
            HuffmanNode min2 = priorityQueue.remove();
            HuffmanNode parent = new HuffmanNode(min1.frequency + min2.frequency,
                    '\0', min1, min2);
            priorityQueue.add(parent);
        }
        root = priorityQueue.remove();
    }

    // Saves the Huffman tree structure to a PrintStream
    // Each character and its corresponding path in the tree is saved in the output
    // Parameters:
    //  output: the PrintStream to save the Huffman tree to
    public void save(PrintStream output){
        if(root != null){
            save(root, "", output);
        }
    }

    // Helper method to save the Huffman tree structure to a PrintStream
    // Each character and its corresponding path in the tree is saved in the output
    // Parameters:
    //  node: the current node in the Huffman tree
    //  path: the current path in the tree
    //  output: the PrintStream to save the Huffman tree to
    private void save(HuffmanNode node, String path, PrintStream output){
        if(node.isLeaf()){
            output.println((int) node.letter);
            output.println(path);
        }
        else{
            save(node.left, path + "0", output);
            save(node.right, path + "1", output);
        }
    }

   // Constructs a HuffmanCode object from a Scanner input
   // Each line of the input contains a character's ASCII value 
   // and its path in the tree
   // Parameters:
   //  input: the Scanner containing the Huffman tree structure
    public HuffmanCode(Scanner input) {
        this.root = new HuffmanNode(0, '\0');
        while (input.hasNextLine()) {
            int ascii = Integer.parseInt(input.nextLine());
            String path = input.nextLine();
            addCode(root, path, (char) ascii);
        }
    }

    // Helper method to add a code to the Huffman tree
    // Parameters:
    //  node: the current node
    //  path: the path in the tree
    //  letter: the character to add
    // Returns the updated node
    private HuffmanNode addCode(HuffmanNode node, String path, char letter) {
        if (node == null) {
            node = new HuffmanNode(0, '\0');
        }
        if (path.isEmpty()) {
            return new HuffmanNode(0, letter, node.left, node.right);
        } else {
            char first = path.charAt(0);
            if (first == '0') {
                node.left = addCode(node.left, path.substring(1), letter);
            } else {
                node.right = addCode(node.right, path.substring(1), letter);
            }
        }
        return node;
    }

    // Translates encoded bits to the original text using the Huffman tree
    // Reads bits from the input and writes the decoded characters to the output
    // Parameters:
    //  input: the BitInputStream containing the encoded bits
    //  output: the PrintStream to write the decoded text to
    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode current = root;
        while (input.hasNextBit()) {
            if (current.isLeaf()) {
                output.write(current.letter);
                current = root;
            } else {
                int bit = input.nextBit();
                if (bit == 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
        }
        if (current.isLeaf()) {
            output.write(current.letter);
        }
    }
}