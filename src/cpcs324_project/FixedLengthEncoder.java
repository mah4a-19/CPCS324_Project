package cpcs324_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Implements the baseline Fixed-Length Encoding scheme.
 *
 * This class provides:
 * - Encoding where every character is represented by a fixed 8-bit codeword.
 * - Decoding by converting each 8-bit block back to its original character.
 * - Methods to compute encoded size in bits and bytes.
 *
 */

public class FixedLengthEncoder {

    // Fixed code length: 8 bits per character (1 byte).
    public static final int BITS_PER_CHAR = 8;

    // Convert character to binary string, pad with leading zeros to ensure it is exactly 8 bits
    // Example: 'A' → "1000001" → padded to "01000001"
    public String to8BitBinary(char c) {
        // Convert the character to its binary representation
        String bin = Integer.toBinaryString(c);

        // Calculate how many leading zeros are needed to make the binary string 8 bits
        int pad = 8 - bin.length();
        
        // If the binary string is shorter than 8 bits, add leading zeros
        if (pad > 0) {
            return "0".repeat(pad) + bin;
        }
    
    // If already 8 bits or more, return as is
    return bin;
}

    // Encodes the content of a file using fixed 8-bit coding
    public String encode(String fileName) throws IOException {
        
        // Read file content
        String text = new String(Files.readAllBytes(Paths.get(fileName)));

        StringBuilder encodedBitstream = new StringBuilder();

        for (char c : text.toCharArray()) {
            encodedBitstream.append(to8BitBinary(c));
        }

        return encodedBitstream.toString();
    }

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
     * Decodes the bitstream by reading 8 bits at a time
     * and converting each block back to a character.
     */
    public String decode(String encodedBitstream) {
        StringBuilder decodedText = new StringBuilder();
        String codeword;
        // Process stream in block of 8 bits
        for (int i = 0; i < encodedBitstream.length(); i = i + 8) {
            codeword = encodedBitstream.substring(i, i + 8);
            decodedText.append(from8BitBinary(codeword));
        }

        return decodedText.toString();
    }

    // Convert an 8-bit binary string back to its corresponding character
    // Example: "01000001" → 65 → 'A'
    public char from8BitBinary(String bits) {
        return (char) Integer.parseInt(bits, 2);
    }
}
