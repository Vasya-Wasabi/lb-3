package battle;

import java.io.*;

/**
 * Клас FileManager відповідає за створення, запис, зчитування та закриття файлів журналу бою.
 *
 * Усі дії під час битви (ходи, результати, повідомлення) записуються у файл формату .txt
 * у папці "BattleRecords". Також усі записи дублюються у консоль.
 */
public class FileManager {

    /** Поточний активний об’єкт для запису у файл. */
    private static FileWriter writer = null;

    /** Шлях до поточного файлу журналу бою. */
    private static String currentFile = null;

    /**
     * Створює новий файл для запису журналу бою.
     * Якщо файл з таким іменем вже існує — він перезаписується.
     *
     * @param filename ім’я файлу без розширення (наприклад, "duel1" або "team_battle")
     */
    public static void setFile(String filename) {
        try {
            currentFile = "BattleRecords\\" + filename + ".txt";
            writer = new FileWriter(currentFile, false); // створення нового файлу
            System.out.println("Battle log file set: " + currentFile);
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
            currentFile = null;
            writer = null;
        }
    }

    /**
     * Записує один рядок тексту у файл журналу бою та дублює його у консоль.
     * Якщо файл ще не створено (writer == null), то запис у файл пропускається.
     *
     * @param log текстовий рядок, який потрібно додати до журналу бою
     */
    public static void saveBattle(String log) {
        // Завжди виводимо лог у консоль
        System.out.println(log);

        // Якщо файл не встановлено — пропускаємо запис
        if (writer == null) return;

        try {
            writer.write(log + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Закриває поточний файл журналу бою після завершення бою.
     *
     * Цей метод звільняє ресурси FileWriter, що запобігає втраті даних.
     * Викликається після закінчення дуелі або командної битви.
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
     *
     * @param filename ім’я файлу (разом із розширенням .txt або без нього),
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
