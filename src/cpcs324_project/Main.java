package cpcs324_project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Main {

    // Define the required test files
    private static final String[] FILE_NAMES = {
        "file_50KB.txt",
        "file_200KB.txt",
        "file_1MB.txt"
    };

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);
        int userOption; // Variable to store user's menu choice
        Scanner input = new Scanner(System.in);

        // Project Title Display
        System.out.println("*************************************");
        System.out.println(" Algorithms and Data Structures (II)");
        System.out.println("       Group Project - Part 2");

        // Main program loop (keeps running until user exits)
        do {
            System.out.println("*************************************");
            // Display the main menu options to the user
            System.out.print("1. Encode a file with  Huffman Coding\n"
                    + "2. Start Compression Algorithm Testing\n"
                    + "   Huffman Coding vs. Baseline Fixed-Length Coding\n"
                    + "3. Exit \n Select one option: ");
            userOption = input.nextInt();
            System.out.println("--------------------------------------");
            switch (userOption) {

                // Option 1: Encode a file with  Huffman Coding
                case 1: {
                    
                    // Prompt user to enter desired file size in KB
                    System.out.print("Enter the file size (KB): ");
                    int fileSize = input.nextInt();
                    
                     // Construct file name based on entered size
                    String fileName = "file_" + fileSize + "KB.txt";
                    
                    try {
                        System.out.println("\nStarting random text file generation...");
                        String random_text = RandomTextGenerator.execute(fileSize);
                        RandomTextGenerator.saveToFile(fileName, random_text);
                        System.out.println("Complete Generation");

                    } catch (IOException e) {
                        // Handle any I/O errors during file generation
                        System.err.println("Error saving files during generation: " + e.getMessage());
                    }

                    // Read file content and get original size
                    String originalText = new String(Files.readAllBytes(Paths.get(fileName)));
                    long originalSize = originalText.length(); // Size in Bytes

                    HuffmanEncoder huffmanEncoder = new HuffmanEncoder();

                    // --------- Encoding ------------
                    // Measure time taken for encoding 
                    long startTime = System.nanoTime();
                    String encodedBitstream = huffmanEncoder.encode(fileName);
                    long encodeTime = (System.nanoTime() - startTime) / 1000000; // Time in milliseconds

                    // Calculate compressed size in Bytes using the encoder method
                    long compressedSize = huffmanEncoder.getCompressedSizeInBytes(encodedBitstream);

                    // Calculate Compression Ratio (Compressed Size / Original Size)
                    double compressionRatio = (double) compressedSize / originalSize;

                    // --------- Decoding ------------
                    // Measure time taken to decode the encoded bitstream back to original text
                    startTime = System.nanoTime();
                    String decodedText = huffmanEncoder.decode(encodedBitstream);
                    long decodeTime = (System.nanoTime() - startTime) / 1000000;

                    // Verification check (Req. 2.3)
                    boolean isCorrect = originalText.equals(decodedText);
                    
                    // Display Huffman codes (Req. 1.e)
                    displayHuffmanCodes(huffmanEncoder);
                    
                    // ---------------- Print Results ----------------
                    System.out.println("\n--- Huffman Compression Results ---");
                    System.out.println("Original File Size     : " + originalSize + " bytes");
                    System.out.println("Compressed File Size   : " + compressedSize + " bytes");
                    System.out.println("Compression Ratio      : " + String.format("%.4f", compressionRatio));
                    System.out.println("Encoding Time          : " + encodeTime + " ms");
                    System.out.println("Decoding Time          : " + decodeTime + " ms");
                    System.out.println("Decoding Verification: " + (isCorrect ? "Correct" : "Incorrect"));
                    System.out.println("------------------------------------\n");
                    break;
                }
                // Option 2: Compare Compression Algorithms Huffman Coding vs. Baseline Fixed-Length Coding
                case 2: {
                    compressionAlgorithmTesting();
                    break;
                }
                default: {
                    System.out.println("Exiting program");
                    System.exit(0);
                }
            }
        }while(true); // end of main program loop
    } // end of main method
    

    
    /**
     * Automates compression testing:
     * - Generates random test files (50KB, 200KB, 1MB)
     * - Runs Huffman and Fixed-Length encoding
     * - Measures encode/decode time, compressed size, and ratio
     * - Prints results and saves them to a CSV file
     */
    private static void compressionAlgorithmTesting() throws IOException {
        // Define sizes in bytes as per Requirement 1 (small, medium, large files).
        int size_50KB = 50 * 1024;
        int size_200KB = 200 * 1024;
        int size_1MB = 1024 * 1024;

        try {
            System.out.println("\nStarting random text file generation...");

            // Generate and save the three required test files
            String text_50KB = RandomTextGenerator.execute(size_50KB);
            RandomTextGenerator.saveToFile("file_50KB.txt", text_50KB);

            String text_200KB = RandomTextGenerator.execute(size_200KB);
            RandomTextGenerator.saveToFile("file_200KB.txt", text_200KB);

            String text_1MB = RandomTextGenerator.execute(size_1MB);
            RandomTextGenerator.saveToFile("file_1MB.txt", text_1MB);

            System.out.println("All test files generation complete.\n");

        } catch (IOException e) {
            System.err.println("Error saving files during generation: " + e.getMessage());
        }

        System.out.println("====================================================");
        System.out.println("====== Starting Compression Algorithm Testing ======");
        System.out.println("====================================================");

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

        System.out.println("\nTesting complete. Data is ready for analysis and plotting.");

        // Save results to CSV file for 
        saveResultsToCSV(resultsTable, "compression_results.csv");


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
        System.out.println("Decoding Verification: " + (isCorrect ? "Correct" : "Incorrect"));
    }

    // Helper function to run the Fixed-Length (Baseline) test case
    private static void runFixedLengthTest(String fileName, String originalText, long originalSize, List<String[]> resultsTable) throws IOException {
        FixedLengthEncoder fixedEncoder = new FixedLengthEncoder();

        long startTime, endTime, encodeTime, decodeTime = 0;

        startTime = System.nanoTime();
        String encodedBitstream = fixedEncoder.encode(fileName);
        endTime = System.nanoTime();
        encodeTime = (endTime - startTime) / 1000000;

        // Fixed-Length is trivial; no complex encoding/decoding process needed.
        long compressedSize = fixedEncoder.getCompressedSizeInBytes(originalText);

        // Compression ratio is 1.0 (Compressed Size == Original Size)
        double compressionRatio = (double) compressedSize / originalSize;

        // Time the Decoding process
        startTime = System.nanoTime();
        String decodedText = fixedEncoder.decode(encodedBitstream);
        decodeTime = (System.nanoTime() - startTime) / 1000000;

        // Verification check (Req. 2.3)
        boolean isCorrect = originalText.equals(decodedText);

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
        System.out.println("Decoding Verification: " + (isCorrect ? "Correct" : "Incorrect"));

    }

    // Prints the final results table to the console
    private static void printResultsTable(List<String[]> resultsTable) {
        System.out.println("\n\n==============================================================");
        System.out.println("==================== Final Results Table =====================");
        System.out.println("==============================================================");

        // Print with fixed-width formatting for alignment
        for (String[] row : resultsTable) {
            System.out.printf("%-15s | %-10s | %-18s | %-22s | %-20s | %-18s | %-20s\n",
                    row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
        }
    }

    // Displays the Huffman codewords and frequencies for the first file (Req. 2.e)
    private static void displayHuffmanCodes(HuffmanEncoder encoder) {
        System.out.println("\n------- Huffman Code Table  -------");
        System.out.println("Char\t| Freq\t\t| Codeword");
        System.out.println("-------------------------------------");

        Map<Character, String> codes = encoder.getHuffmanCodes();
        Map<Character, Integer> freqs = encoder.getCharFrequencies();

        // Display a limited number of characters to avoid excessive output
        int count = 0;
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s\t| %d\t\t| %s\n", entry.getKey(), freqs.get(entry.getKey()), entry.getValue());
            count++;
        }

    }

    // Save results table into CSV file for Analysis and Visualization
    private static void saveResultsToCSV(List<String[]> resultsTable, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String[] row : resultsTable) {
                writer.write(String.join(";", row));
                writer.write("\n");
            }
            System.out.println("\nResults saved to: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving CSV file: " + e.getMessage());
        }
    }
}
