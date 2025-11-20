/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MahaAlbrakati
 */

package cpcs324_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class RandomTextGenerator {

    // Character set limited to A-Z (uppercase only) 
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generates a random text string of a specified length.
     * @param length The desired number of characters (size in bytes).
     * @return A random string.
     */
    public static String generateRandomText(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length); 

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length()); 
            sb.append(CHARACTERS.charAt(index)); 
        }
        return sb.toString(); 
    }

    /**
     * Saves the generated content to a file.
     * @param fileName The name of the output file.
     * @param content The string content to be saved.
     */
    public static void saveToFile(String fileName, String content) throws IOException {

        Files.write(Paths.get(fileName), content.getBytes());
        
        System.out.println("✅ File created and saved: " + fileName + 
                           " | Character Count: " + content.length() + " chars");
    }

    public static void main(String[] args) {
        // Define sizes in bytes as per Requirement 1 (small, medium, large files).
        int size_50KB = 50 * 1024;       
        int size_200KB = 200 * 1024;     
        int size_1MB = 1024 * 1024;      

        try {
            System.out.println("Starting random text file generation...");
            
            // Generate and save the three required test files
            String text_50KB = generateRandomText(size_50KB);
            saveToFile("file_50KB.txt", text_50KB);

            String text_200KB = generateRandomText(size_200KB);
            saveToFile("file_200KB.txt", text_200KB);

            String text_1MB = generateRandomText(size_1MB);
            saveToFile("file_1MB.txt", text_1MB);
            
            System.out.println("✅ All test files generation complete.");

        } catch (IOException e) {
            System.err.println("Error saving files during generation: " + e.getMessage());
        }
    }
}