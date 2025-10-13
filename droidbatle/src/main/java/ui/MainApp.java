package ui;

import droids.*;
import battle.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Droid> droidList = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("\n| Battle of the droid |");

        while (true) {
            menu();

            int choice = scanner.nextInt();

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

    private static void showDroidCreationMenu() {
        System.out.println("\nCREATION OF THE DROID:");

        while (true) {
            System.out.print("Choose type: 1) Stormtrooper 2) Medic 3) Sniper 4) Tank 0) Exit to Menu: -> ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            String name = "";
            if(choice != 0) {
                System.out.print("Enter name: -> ");
                name = scanner.nextLine();
            }

            Droid d = null;

            switch (choice) {
                case 1: d = new Stormtrooper(name); break;
                case 2: d = new Medic(name); break;
                case 3: d = new Sniper(name); break;
                case 4: d = new Tank(name); break;
                case 0: return;
                default: System.out.println("Error! Unknown command");
            }

            droidList.add(d);
            System.out.println("CREATED " + d);
        }
    }

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

    private static Droid cloneDroid(Droid d) {
        if(d instanceof Stormtrooper) return new Stormtrooper(d.getName());
        if(d instanceof Medic) return new Medic(d.getName());
        if(d instanceof Sniper) return new Sniper(d.getName());
        if(d instanceof Tank) return new Tank(d.getName());
        return new Stormtrooper(d.getName());
    }

    private static void starDuel() {
        showDroidList();
        System.out.println();

        Droid[] chosen = chooseTwoDroids();
        if (chosen == null) return;

        Droid d1 = chosen[0];
        Droid d2 = chosen[1];

        Duel duel = new Duel(d1, d2);
        duel.run();
        FileManager.closeFile();
    }

    private static void startTeamBattle() {
        ArrayList<Droid> team1 = new ArrayList<>();
        ArrayList<Droid> team2 = new ArrayList<>();

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
    }

    private static void chooseDroidsForTeam(ArrayList<Droid> team) {
        Droid[] chosen = chooseTwoDroids();
        if (chosen == null) return;

        Droid d1 = chosen[0];
        Droid d2 = chosen[1];

        team.add(d1);
        team.add(d2);
    }

    private static Droid[] chooseTwoDroids() {
        System.out.print("Choose first droid: -> ");
        int a = scanner.nextInt();

        System.out.print("Choose second droid: -> ");
        int b = scanner.nextInt();

        if (a < 0 || a >= droidList.size() || b < 0 || b >= droidList.size()) {
            System.out.println("Error! Invalid index selected.");
            return null;
        }

        Droid d1 = cloneDroid(droidList.get(a));
        Droid d2 = cloneDroid(droidList.get(b));

        return new Droid[]{d1, d2};
    }

    private static void RecordToFile() {
        scanner.nextLine(); // очистка буфера
        System.out.print("Do you want to record the next battle? (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (!answer.equals("yes")) {
            System.out.println("Recording disabled.");
            FileManager.setFile(null);
            return;
        }

        System.out.print("Enter the filename to save the battle log: ");
        String filename = scanner.nextLine().trim();

        FileManager.setFile(filename);
        System.out.println("Recording enabled. Logs will be saved to: " + filename);
    }

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
        scanner.nextLine(); // clear buffer
        String filename = scanner.nextLine().trim();

        if (filename.isEmpty()) {
            System.out.println("No file entered. Aborting.");
            return;
        }

        // Ensure extension .txt
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        FileManager.readBattle(filename);
    }


}
