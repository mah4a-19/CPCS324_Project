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


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainProjectRunner {

    // Define the required test files
    private static final String[] FILE_NAMES = {
        "file_50KB.txt", 
        "file_200KB.txt", 
        "file_1MB.txt"
    };

    public static void main(String[] args) throws IOException {
        System.out.println("=================================================");
        System.out.println("====== Starting Compression Algorithm Testing ======");
        System.out.println("=================================================");
        
        // List to store all benchmark results for final table printing (Req. 3)
        List<String[]> resultsTable = new ArrayList<>();
        
        // Add table header row
        resultsTable.add(new String[]{"File Name", "Method", "Original Size (B)", 
                                     "Compressed Size (B)", "Compression Ratio", 
                                     "Encode Time (ms)", "Decode Time (ms)"});

        // Run tests on all defined files
        for (String fileName : FILE_NAMES) {
            // Read file content and get original size
            String originalText = new String(Files.readAllBytes(Paths.get(fileName)));
            long originalSize = originalText.length(); // Size in Bytes

            // 1. Run Huffman Coding Test
            runHuffmanTest(fileName, originalText, originalSize, resultsTable);
            
            // 2. Run Fixed-Length Encoding (Baseline) Test
            runFixedLengthTest(fileName, originalText, originalSize, resultsTable);
        }
        
        // Print final results table
        printResultsTable(resultsTable);

        System.out.println("\n✅ Testing complete. Data is ready for analysis and plotting.");
    }
    
    // Helper function to run the Huffman test case
    private static void runHuffmanTest(String fileName, String originalText, long originalSize, List<String[]> resultsTable) throws IOException {
        HuffmanEncoder huffmanEncoder = new HuffmanEncoder();

        // Time the Encoding process (Build Tree + Generate Codes + Encode)
        long startTime = System.nanoTime();
        String encodedBitstream = huffmanEncoder.encode(fileName);
        long encodeTime = (System.nanoTime() - startTime) / 1000000; // Time in milliseconds

        // Calculate compressed size in Bytes using the encoder method
        long compressedSize = huffmanEncoder.getCompressedSizeInBytes(encodedBitstream);
        
        // Calculate Compression Ratio (Compressed Size / Original Size)
        double compressionRatio = (double) compressedSize / originalSize;

        // Time the Decoding process
        startTime = System.nanoTime();
        String decodedText = huffmanEncoder.decode(encodedBitstream);
        long decodeTime = (System.nanoTime() - startTime) / 1000000;

        // Verification check (Req. 2.3)
        boolean isCorrect = originalText.equals(decodedText);
        
        // Store results in the table
        resultsTable.add(new String[]{
            fileName, 
            "Huffman", 
            String.valueOf(originalSize), 
            String.valueOf(compressedSize),
            String.format("%.4f", compressionRatio),
            String.valueOf(encodeTime), 
            String.valueOf(decodeTime)
        });
        
        System.out.println("\n--- Huffman Test for " + fileName + " ---");
        System.out.println("Decoding Verification: " + (isCorrect ? "✅ Correct" : "❌ Incorrect"));
        
        // Display Huffman codes for the first file only (Req. 2.e)
        if (fileName.equals(FILE_NAMES[0])) {
              displayHuffmanCodes(huffmanEncoder);
        }
    }
    
    // Helper function to run the Fixed-Length (Baseline) test case
    private static void runFixedLengthTest(String fileName, String originalText, long originalSize, List<String[]> resultsTable) {
        FixedLengthEncoder fixedEncoder = new FixedLengthEncoder();
        
        // Fixed-Length is trivial; no complex encoding/decoding process needed.
        long compressedSize = fixedEncoder.getCompressedSizeInBytes(originalText);
        long encodeTime = 0; // Assuming minimal processing time for baseline
        long decodeTime = 0;
        
        // Compression ratio is 1.0 (Compressed Size == Original Size)
        double compressionRatio = (double) compressedSize / originalSize;
        
        // Store results in the table
        resultsTable.add(new String[]{
            fileName, 
            "Baseline", 
            String.valueOf(originalSize), 
            String.valueOf(compressedSize),
            String.format("%.4f", compressionRatio),
            String.valueOf(encodeTime), 
            String.valueOf(decodeTime)
        });
        System.out.println("\n--- Fixed-Length (Baseline) Test for " + fileName + " ---");
    }

    // Prints the final results table to the console
    private static void printResultsTable(List<String[]> resultsTable) {
        System.out.println("\n\n=======================================================");
        System.out.println("==================== Final Results Table =====================");
        System.out.println("=======================================================");
        
        // Print with fixed-width formatting for alignment
        for (String[] row : resultsTable) {
            System.out.printf("%-15s | %-10s | %-18s | %-22s | %-20s | %-15s | %-15s\n", 
                             row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
        }
    }
    
    // Displays the Huffman codewords and frequencies for the first file (Req. 2.e)
    private static void displayHuffmanCodes(HuffmanEncoder encoder) {
        System.out.println("\n--- Huffman Code Table for 50KB file ---");
        System.out.println("Char\t| Freq\t\t| Codeword");
        System.out.println("-------------------------------------");
        
        Map<Character, String> codes = encoder.getHuffmanCodes();
        Map<Character, Integer> freqs = encoder.getCharFrequencies();
        
        // Display a limited number of characters to avoid excessive output
        int count = 0;
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            if (count > 15) break; 
            System.out.printf("%s\t| %d\t\t| %s\n", entry.getKey(), freqs.get(entry.getKey()), entry.getValue());
            count++;
        }
        System.out.println("...(Other characters)...");
    }
}