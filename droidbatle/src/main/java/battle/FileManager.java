package battle;

import java.io.*;

public class FileManager {

    private static FileWriter writer = null;
    private static String currentFile = null;

    // Set the file name before battle
    public static void setFile(String filename) {
        try {

            currentFile = "BattleRecords\\" + filename + ".txt";
            writer = new FileWriter(currentFile, false); // create new file
            System.out.println("Battle log file set: " + currentFile);

        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
            currentFile = null;
            writer = null;
        }
    }

    // Save a single line to the battle log
    public static void saveBattle(String log) {
        // Always print to console
        System.out.println(log);

        // If logging is disabled — skip writing to file
        if (writer == null) return;

        try {
            writer.write(log + System.lineSeparator());
            writer.flush(); // immediately flush to make file readable during battle
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Close the log file after the battle ends
    public static void closeFile() {
        if (writer != null) {
            try {
                writer.close();
                System.out.println("Battle log file closed successfully: " + currentFile);
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            } finally {
                writer = null;
                currentFile = null;
            }
        }
    }

    // Read and replay the battle log
    public static void readBattle(String filename) {
        String fullPath = "BattleRecords\\" + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            System.out.println("\nReplaying battle from file: " + fullPath);
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Battle file '" + fullPath + "' not found!");
        }
    }
}
