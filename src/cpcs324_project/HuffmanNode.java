
package cpcs324_project;

/**
 * Represents a node in the Huffman Tree.
 * Must implement Comparable to allow the PriorityQueue to sort nodes by frequency.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    
    public char data;         // The character value (only meaningful for leaf nodes)
    public int frequency;     // The frequency count (or sum of frequencies for internal nodes)
    public HuffmanNode left, right; // Child nodes (left = 0, right = 1)

    /**
     * Constructor for a Leaf Node (contains an actual character).
     */
    public HuffmanNode(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    /**
     * Constructor for an Internal Node (combines two sub-trees).
     */
    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.data = 0; // Null or non-character value for internal nodes
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    /**
     * Comparison method required for PriorityQueue.
     * Nodes with lower frequency have higher priority (will be polled first).
     */
    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency; 
    }
}
