
package cpcs324_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Implements Huffman compression and decompression.
 * This class:
 * - Calculates character frequencies
 * - Builds the Huffman tree
 * - Generates prefix-free codewords
 * - Encodes text into a compressed bitstream
 * - Decodes the bitstream back to the original text
 */

public class HuffmanEncoder {

    private Map<Character, String> huffmanCodes = new HashMap<>(); // Stores the final generated codewords
    private Map<Character, Integer> charFrequencies = new HashMap<>(); // Stores character frequencies
    private HuffmanNode root; // The root of the Huffman Tree

    /**
     * Calculates the frequency of each character in the input text. (Req. 2.a)
     */
    private void calculateFrequencies(String text) {
        for (char character : text.toCharArray()) {
            charFrequencies.put(character, charFrequencies.getOrDefault(character, 0) + 1);
        }
    }

    /**
     * Builds the Huffman Tree using a PriorityQueue (Greedy approach). (Req. 2.b & 2.c)
     */
    private void buildHuffmanTree() {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(); 

        // 1. Add all leaf nodes (characters) to the priority queue
        for (Map.Entry<Character, Integer> entry : charFrequencies.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        
        // 2. Greedily combine the two lowest frequency nodes until only one remains
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll(); // Node with the lowest frequency
            HuffmanNode right = pq.poll(); // Node with the second lowest frequency
            
            // Create a new internal node by summing their frequencies
            int sumFreq = left.frequency + right.frequency;
            HuffmanNode parent = new HuffmanNode(sumFreq, left, right);
            
            pq.add(parent);
        }
        
        this.root = pq.poll(); // The final remaining node is the tree root
    }

    /**
     * Generates the prefix-free Huffman codewords via recursive traversal (DFS). (Req. 2.d)
     * @param node The current node in the tree.
     * @param code The bit string generated so far.
     */
    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) return;

        // If it's a leaf node, we have found the final codeword for the character
        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.data, code);
            return;
        }

        // Traverse left (assign '0') and right (assign '1')
        generateCodes(node.left, code + "0"); 
        generateCodes(node.right, code + "1"); 
    }

    /**
     * Encodes the text file using Huffman's algorithm (Full process).
     * @param fileName The path to the file to be encoded.
     * @return The encoded bitstream (a String of '0's and '1's).
     */
    public String encode(String fileName) throws IOException {
        // Read the entire file content (Java 8 compatible)
        String text = new String(Files.readAllBytes(Paths.get(fileName)));

        // Run the 3 main steps of the Huffman algorithm
        calculateFrequencies(text);
        buildHuffmanTree();
        generateCodes(this.root, "");
        
        // Construct the final encoded bitstream
        StringBuilder encodedBitstream = new StringBuilder();
        for (char character : text.toCharArray()) {
            encodedBitstream.append(huffmanCodes.get(character));
        }
        
        return encodedBitstream.toString();
    }

    /**
     * Decodes the Huffman bitstream back into the original text. (Req. 2.3)
     * @param encodedBitstream The bit string to decode.
     * @return The recovered original text.
     */
    public String decode(String encodedBitstream) {
        StringBuilder decodedText = new StringBuilder();
        HuffmanNode currentNode = this.root; 

        for (char bit : encodedBitstream.toCharArray()) {
            if (bit == '0') {
                currentNode = currentNode.left;
            } else if (bit == '1') {
                currentNode = currentNode.right;
            }

            // If we reach a leaf node, we have successfully decoded one character
            if (currentNode.left == null && currentNode.right == null) {
                decodedText.append(currentNode.data); 
                currentNode = this.root; // Reset to the root for the next character
            }
        }
        return decodedText.toString();
    }
    
    /**
     * Helper method to get the final Huffman codes for display (Req. 2.e).
     */
    public Map<Character, String> getHuffmanCodes() {
        return huffmanCodes;
    }
    
    /**
     * Helper method to get character frequencies for analysis.
     */
    public Map<Character, Integer> getCharFrequencies() {
        return charFrequencies;
    }

    /**
     * Calculates the compressed size of the file in bytes.
     * @param encodedBitstream The bit stream length.
     * @return The size in bytes (rounded up).
     */
    public long getCompressedSizeInBytes(String encodedBitstream) {
        // Size in Bytes = ceil(Total Bits / 8)
        return (long) Math.ceil(encodedBitstream.length() / 8.0);
    }
}
