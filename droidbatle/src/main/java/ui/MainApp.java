package ui;

import droids.*;
import battle.*;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Головний клас програми MainApp.
 * <p>
 * Реалізує консольний інтерфейс для управління грою "Battle of the Droid".
 * Дозволяє користувачу створювати дроїдів, переглядати їх список,
 * проводити дуелі та командні бої, а також записувати та відтворювати бої з файлу.
 * </p>
 *
 * Основні функції:
 * <ul>
 * <li>Створення дроїдів різних типів.</li>
 * <li>Перегляд списку дроїдів.</li>
 * <li>Проведення дуелі між двома дроїдами.</li>
 * <li>Проведення командного бою між двома командами дроїдів.</li>
 * <li>Запис боїв у файл.</li>
 * <li>Відтворення збережених боїв.</li>
 * </ul>
 * @author Taras Trymbulskyi
 * @version 1.0
 */
public class MainApp {

    /** Сканер для введення користувача. */
    private static final Scanner scanner = new Scanner(System.in);

    /** Список усіх створених дроїдів. */
    private static final ArrayList<Droid> droidList = new ArrayList<>();

    /**
     * Точка входу у програму, що запускає головний цикл меню.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {

        System.out.println("\n| Battle of the droid |");

        while (true) {
            menu();

            int choice;

            try {
                choice = scanner.nextInt();
            } catch(InputMismatchException e) {
                System.out.println("Error! Invalid choice " + e.getMessage());
                scanner.nextLine();
                continue;
            }

            switch(choice) {
                case 1: showDroidCreationMenu(); break;
                case 2: showDroidList(); break;
                case 3: starDuel(); break;
                case 4: startTeamBattle(); break;
                case 5: RecordToFile(); break;
                case 6: replayBattle(); break;
                case 0: System.out.println("Exit..."); return;
                default: System.out.println("Error! Unknown command");
            }
        }
    }

    /**
     * Відображає головне меню програми в консолі.
     */
    private static void menu() {
        System.out.println("\nMENU:");
        System.out.println("1. Creat droids");
        System.out.println("2. Show list of droids");
        System.out.println("3. Start a duel");
        System.out.println("4. Start Team battle");
        System.out.println("5. Save battle");
        System.out.println("6. Replay battle");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: -> ");
    }

    /**
     * Відображає меню створення дроїдів різних типів.
     * <p>
     * Користувач вибирає тип дроїда, вводить його ім'я, після чого
     * новий об'єкт додається до глобального списку {@link #droidList}.
     * Цикл створення триває, доки користувач не вирішить вийти в головне меню.
     */
    private static void showDroidCreationMenu() {
        System.out.println("\nCREATION OF THE DROID:");

        while (true) {
            System.out.print("Choose type: 1) Stormtrooper 2) Medic 3) Sniper 4) Tank 0) Exit to Menu: -> ");

            int choice;

            try {
                choice = scanner.nextInt();
            } catch(InputMismatchException e) {
                System.out.println("Error! Invalid choice " + e.getMessage() + "\n");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();

            if(choice == 0) {
                return;
            }
            else if (choice > 4) {
                System.out.println("Error! Unknown command");
                scanner.nextLine();
                return;
            }

            System.out.print("Enter name: -> ");
            String name  = scanner.nextLine();

            Droid d = null;

            switch (choice) {
                case 1 -> d = new Stormtrooper(name);
                case 2 -> d = new Medic(name);
                case 3 -> d = new Sniper(name);
                case 4 -> d = new Tank(name);
            }

            droidList.add(d);
            System.out.println("CREATED " + d);
        }
    }

    /**
     * Відображає в консолі нумерований список усіх створених дроїдів.
     * Якщо список порожній, виводить відповідне повідомлення.
     */
    private static void showDroidList() {
        System.out.println("\nLIST OF DROIDS:");

        if(droidList.isEmpty()) {
            System.out.println("List is empty");
            return;
        }

        for(byte i = 0; i < droidList.size(); ++i) {
            System.out.println(i + ") " + droidList.get(i));
        }

    }

    /**
     * Створює глибоку копію дроїда для безпечної участі у бою.
     * <p>
     * Клонування необхідне, щоб зміни стану дроїда (здоров'я, статус навичок)
     * під час бою не впливали на оригінальний екземпляр у {@link #droidList}.
     *
     * @param d оригінальний дроїд зі списку.
     * @return новий об'єкт-копія того ж типу з тим самим ім'ям.
     */
    private static Droid cloneDroid(Droid d) {
        if(d instanceof Stormtrooper) return new Stormtrooper(d.getName());
        if(d instanceof Medic) return new Medic(d.getName());
        if(d instanceof Sniper) return new Sniper(d.getName());
        if(d instanceof Tank) return new Tank(d.getName());
        return new Stormtrooper(d.getName());
    }

    /**
     * Ініціює та проводить дуель між двома обраними дроїдами.
     * <p>
     * Спочатку викликає {@link #showDroidList()}, потім дозволяє вибрати
     * двох бійців через {@link #chooseTwoDroids()}. Після цього створює
     * екземпляр {@link Duel} і запускає бій.
     */
    private static void starDuel() {
        showDroidList();
        if(droidList.isEmpty()) {
            return;
        }

        System.out.println();

        Droid[] chosen = chooseTwoDroids();

        Droid d1 = chosen[0];
        Droid d2 = chosen[1];

        Duel duel = new Duel(d1, d2);
        duel.run();
        FileManager.closeFile();
    }

    /**
     * Ініціює та проводить командний бій 2 на 2.
     * <p>
     * Користувач вводить назви команд, після чого вибирає по два дроїди
     * для кожної з них. Створюється екземпляр {@link TeamBattle} і запускається бій.
     */
    private static void startTeamBattle() {
        ArrayList<Droid> team1 = new ArrayList<>();
        ArrayList<Droid> team2 = new ArrayList<>();

        if(droidList.isEmpty()) {
            showDroidList();
            return;
        }

        scanner.nextLine();
        System.out.print("\nEnter the name of the first Team: -> ");
        String teamName1 = scanner.nextLine();

        System.out.print("Enter the name of the second Team: -> ");
        String teamName2 = scanner.nextLine();

        showDroidList();
        System.out.println();

        System.out.println("Choose 2 droids for team " + teamName1 + ": ");
        chooseDroidsForTeam(team1);

        System.out.println("\nChoose 2 droids for team " + teamName2 + ": ");
        chooseDroidsForTeam(team2);

        TeamBattle battleTeam = new TeamBattle(team1, teamName1, team2, teamName2);
        battleTeam.run();
        FileManager.closeFile();
    }

    /**
     * Допоміжний метод для наповнення команди дроїдами.
     * Використовує логіку {@link #chooseTwoDroids()} для вибору пари дроїдів.
     *
     * @param team список дроїдів команди, який потрібно наповнити.
     */
    private static void chooseDroidsForTeam(ArrayList<Droid> team) {
        Droid[] chosen = chooseTwoDroids();

        Droid d1 = chosen[0];
        Droid d2 = chosen[1];

        team.add(d1);
        team.add(d2);
    }

    /**
     * Надає інтерфейс для вибору двох дроїдів зі списку за їх індексами.
     *
     * @return Масив із двох клонованих екземплярів обраних дроїдів.
     */
    private static Droid[] chooseTwoDroids() {
        int a, b;

        while (true) {
            try {
                System.out.print("Choose first droid: -> ");
                a = scanner.nextInt();

                System.out.print("Choose second droid: -> ");
                b = scanner.nextInt();

                if (a < 0 || a >= droidList.size() || b < 0 || b >= droidList.size()) {
                    System.out.println("Error! Invalid index selected.\n");
                    continue;
                }

                break;

            } catch (InputMismatchException e) {
                System.out.println("Error! Please enter a valid number.\n");
                scanner.nextLine();
            }
        }

        Droid d1 = cloneDroid(droidList.get(a));
        Droid d2 = cloneDroid(droidList.get(b));

        return new Droid[]{d1, d2};
    }


    /**
     * Керує налаштуваннями запису наступного бою у файл.
     * <p>
     * Запитує у користувача, чи потрібно записувати бій. Якщо так,
     * просить ввести ім'я файлу і налаштовує {@link FileManager} для запису.
     * </p>
     */
    private static void RecordToFile() {
        scanner.nextLine();
        System.out.print("Do you want to record the next battle? (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (!answer.equals("yes")) {
            System.out.println("Recording disabled.");
            return;
        }

        System.out.print("Enter the filename to save the battle log: ");
        String filename = scanner.nextLine().trim();

        FileManager.setFile(filename);
        System.out.println("Recording enabled. Logs will be saved to: " + filename);
    }

    /**
     * Відтворює збережений лог бою з файлу.
     * <p>
     * Метод сканує директорію {@code BattleRecords}, показує список доступних
     * логів і дозволяє користувачу вибрати один для відтворення.
     * </p>
     */
    private static void replayBattle() {
        File dir = new File("BattleRecords");

        if (!dir.exists()) {
            System.out.println("No battle records folder found.");
            return;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No battle records found.");
            return;
        }

        System.out.println("Available battle logs:");
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("- " + file.getName());
            }
        }

        System.out.print("Enter the name of the battle log to replay: ");
        scanner.nextLine();
        String filename = scanner.nextLine().trim();

        if (filename.isEmpty()) {
            System.out.println("No file entered. Aborting.");
            return;
        }

        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        FileManager.readBattle(filename);
    }
}
