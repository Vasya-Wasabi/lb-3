package battle;

import java.io.*;

/**
 * Клас {@code FileManager} відповідає за роботу з файлами журналів бою.
 * Він забезпечує створення, запис, зчитування та закриття текстових файлів
 * у папці "BattleRecords". Усі дії, що відбуваються під час бою
 * (ходи, результати, повідомлення), записуються у файл і дублюються в консоль.
 */
public class FileManager {

    /** Поточний активний об’єкт для запису у файл. */
    private static FileWriter writer = null;

    /** Шлях до поточного файлу журналу бою. */
    private static String currentFile = null;

    /**
     * Створює новий файл для запису журналу бою.
     * Якщо файл із таким іменем уже існує, він буде перезаписаний.
     *
     * @param filename ім’я файлу без розширення (наприклад, "duel1" або "team_battle")
     */
    public static void setFile(String filename) {
        try {
            currentFile = "BattleRecords\\" + filename + ".txt";
            writer = new FileWriter(currentFile, false);
            System.out.println("Battle log file set: " + currentFile);
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
            currentFile = null;
            writer = null;
        }
    }

    /**
     * Записує рядок тексту у файл журналу бою та дублює його у консоль.
     * Якщо файл ще не створено ({@code writer == null}), запис у файл пропускається.
     *
     * @param log текстовий рядок, який потрібно додати до журналу бою
     */
    public static void saveBattle(String log) {
        System.out.println(log); // дублювання у консоль

        if (writer == null) return; // файл не створено

        try {
            writer.write(log + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Закриває поточний файл журналу бою після завершення бою.
     * Метод звільняє ресурси {@link FileWriter}, запобігаючи втраті даних.
     * Викликається після завершення дуелі або командної битви.
     */
    public static void closeFile() {
        if (writer != null) {
            try {
                writer.close();
                System.out.println("Battle log file closed successfully: " + currentFile);
            } catch (IOException e) {
                System.out.println("Error closing file: " + e.getMessage());
            } finally {
                writer = null;
                currentFile = null;
            }
        }
    }

    /**
     * Зчитує вміст файлу журналу бою та виводить його у консоль.
     * Якщо файл не знайдено — виводить відповідне повідомлення.
     *
     * @param filename ім’я файлу (з розширенням .txt або без нього),
     *                 який знаходиться у папці "BattleRecords"
     */
    public static void readBattle(String filename) {
        String fullPath = "BattleRecords\\" + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            System.out.println("\nReplaying battle from file: " + fullPath);
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Battle file '" + fullPath + "' not found!");
        }
    }
}
