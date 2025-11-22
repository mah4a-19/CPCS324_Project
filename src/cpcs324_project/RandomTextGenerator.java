
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
    public static String execute(int length) {
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
        
        System.out.println("[Done]File created and saved: " + fileName + 
                           " | Character Count: " + content.length() + " chars");
    }
}
