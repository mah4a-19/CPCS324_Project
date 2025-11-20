/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpcs324_project;

/**
 *
 * @author MahaAlbrakati
 */



// Implements the Fixed-Length Encoding scheme (Baseline).
public class FixedLengthEncoder {

    // Fixed code length: 8 bits per character (1 byte).
    public static final int BITS_PER_CHAR = 8;

    /**
     * Calculates the total encoded length in bits.
     */
    public long getEncodedBitsLength(String text) {
        // Encoded size in bits is simply (number of characters * 8)
        return (long) text.length() * BITS_PER_CHAR;
    }

    /**
     * Calculates the compressed file size in bytes.
     */
    public long getCompressedSizeInBytes(String text) {
        // Compressed size equals the original size in bytes.
        return (long) text.length();
    }
    
    /**
     * Decoding: Returns the original text for verification purposes.
     */
    public String decode(String text) {
        return text;
    }
}