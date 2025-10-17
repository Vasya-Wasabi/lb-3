package battle;
import droids.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Клас {@code TeamBattle} реалізує логіку командного бою між двома групами дроїдів.
 *
 * Під час бою кожна команда по черзі обирає дроїдів для атаки.
 * Бій триває доти, поки всі дроїди однієї з команд не будуть знищені.
 *
 * Вся інформація про бій записується у файл через {@link FileManager}.
 * Клас наслідує базову функціональність від {@link Battle}.
 */
public class TeamBattle extends Battle {

    /** Список дроїдів першої команди. */
    private final ArrayList<Droid> team1;

    /** Список дроїдів другої команди. */
    private final ArrayList<Droid> team2;

    /** Назва першої команди. */
    private final String teamName1;

    /** Назва другої команди. */
    private final String teamName2;

    /** Об’єкт для зчитування вибору користувача. */
    private static final Scanner scanner = new Scanner(System.in);

    /** Відображення: дроїд → назва команди, до якої він належить. */
    private final Map<Droid, String> droidTeams = new HashMap<>();

    /**
     * Створює новий об’єкт {@code TeamBattle} із двома командами дроїдів.
     *
     * @param team1 список дроїдів першої команди
     * @param teamName1 назва першої команди
     * @param team2 список дроїдів другої команди
     * @param teamName2 назва другої команди
     */
    public TeamBattle(ArrayList<Droid> team1, String teamName1, ArrayList<Droid> team2, String teamName2) {
        this.team1 = team1;
        this.team2 = team2;
        this.teamName1 = teamName1;
        this.teamName2 = teamName2;

        // Прив’язка кожного дроїда до його команди
        for (Droid d : team1) droidTeams.put(d, teamName1);
        for (Droid d : team2) droidTeams.put(d, teamName2);
    }

    /**
     * Основний метод запуску командного бою.
     *
     * Визначає, яка команда атакує першою, і проводить раунди,
     * доки хоча б один дроїд із протилежної команди живий.
     *
     * Після завершення бою записує результат (переможця) у лог.
     */
    public void run() {

        ArrayList<Droid> attackerTeam;
        ArrayList<Droid> defenderTeam;
        String attackerName;
        String defenderName;

        // Випадковий вибір, хто почне атаку
        if (Math.random() < 0.5) {
            attackerTeam = team1;
            attackerName = teamName1;
            defenderTeam = team2;
            defenderName = teamName2;
        } else {
            attackerTeam = team2;
            attackerName = teamName2;
            defenderTeam = team1;
            defenderName = teamName1;
        }

        FileManager.saveBattle("\nTEAM BATTLE: " + teamName1 + " VS " + teamName2 + "\n");

        int countOfRound = 1;

        // Головний цикл бою
        while (isTeamAlive(team1) && isTeamAlive(team2)) {

            FileManager.saveBattle("| Attackers " + attackerName + " |");
            printTeam(attackerTeam);
            FileManager.saveBattle("| Defenders " + defenderName + " |");
            printTeam(defenderTeam);

            String log1 = performTurn(attackerName, attackerTeam, defenderName, defenderTeam);
            String log2 = performTurn(defenderName, defenderTeam, attackerName, attackerTeam);

            FileManager.saveBattle("\n| ROUND " + countOfRound++ +  " |");
            if (log1 != null) FileManager.saveBattle(log1);
            if (log2 != null) FileManager.saveBattle(log2);
            FileManager.saveBattle("");
        }

        FileManager.saveBattle("\n| TEAM BATTLE ENDED |");
        if (isTeamAlive(attackerTeam)) {
            FileManager.saveBattle("WINNER: " + attackerName + "!");
        } else  {
            FileManager.saveBattle("WINNER: " + defenderName + "!");
        }
    }

    /**
     * Виводить склад команди у консоль і записує у лог.
     *
     * @param team список дроїдів команди
     */
    private void printTeam(ArrayList<Droid> team) {
        int i = 0;
        for (Droid d : team) {
            FileManager.saveBattle("| " + (i++) + ") " + d + " |");
        }
    }

    /**
     * Виконує хід однієї команди — вибір атакуючого та цілі.
     *
     * @param team1Name назва команди, що атакує
     * @param team1 список дроїдів команди, що атакує
     * @param team2Name назва команди-захисника
     * @param team2 список дроїдів команди-захисника
     * @return текстовий лог дії або {@code null}, якщо хід неможливий
     */
    private String performTurn(String team1Name, ArrayList<Droid> team1,
                               String team2Name, ArrayList<Droid> team2) {

        if (!isTeamAlive(team1) || !isTeamAlive(team2)) return null;

        System.out.print("\nChoose droid from team " + team1Name + " for action: -> ");
        Droid attacker = chooseDroid(team1);
        System.out.print("Choose droid from team " + team2Name + ": -> ");
        Droid defender = chooseDroid(team2);

        if (attacker == null || defender == null) return null;

        return collectTurnLog(attacker, defender);
    }

    /**
     * Формує текстовий звіт про дію дроїда.
     *
     * @param performer дроїд, який виконує дію
     * @param target дроїд, який зазнає дії
     * @param actionDescription опис дії (наприклад, "attacks", "heals" тощо)
     * @return відформатований рядок із повним описом дії
     */
    @Override
    protected String buildActionLog(Droid performer, Droid target, String actionDescription) {
        String actionHeader = "| " + performer.getName() + "( " + getTeamName(performer) + " ) -> "
                + target.getName() + "( " + getTeamName(target) + " ) | ";

        String actionPart = actionDescription + " | ";
        String statePart = performer + "; " + target + " |";

        return actionHeader + actionPart + statePart;
    }

    /**
     * Повертає назву команди, до якої належить заданий дроїд.
     *
     * @param d об’єкт дроїда
     * @return назва команди або "Unknown", якщо дроїд не знайдений у мапі
     */
    private String getTeamName(Droid d) {
        return droidTeams.getOrDefault(d, "Unknown");
    }

    /**
     * Перевіряє, чи жива хоча б одна одиниця в команді.
     *
     * @param team список дроїдів
     * @return {@code true}, якщо в команді є живі дроїди
     */
    private boolean isTeamAlive(ArrayList<Droid> team) {
        for (Droid d : team) {
            if (d.isAlive()) return true;
        }
        return false;
    }

    /**
     * Дозволяє користувачу вибрати дроїда з команди.
     * Перевіряє, чи дроїд живий і чи індекс правильний.
     *
     * @param team список дроїдів для вибору
     * @return обраний дроїд або {@code null}, якщо вибір неможливий
     */
    private Droid chooseDroid(ArrayList<Droid> team) {
        Droid chosen = null;
        while (true) {
            if (!isTeamAlive(team)) break;
            int choice = scanner.nextInt();
            if (choice < 0 || choice >= team.size()) {
                System.out.print("Invalid choice! please try again: -> ");
                continue;
            }
            chosen = team.get(choice);
            if (!chosen.isAlive()) {
                System.out.print("This droid is dead! Enter another droid: -> ");
                continue;
            }
            break;
        }
        return chosen;
    }
}
