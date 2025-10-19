package battle;
import droids.*;

import java.util.*;

/**
 * Клас {@code TeamBattle} реалізує логіку командного бою між двома групами дроїдів.
 * Під час бою команди по черзі виконують дії: обирають дроїдів для атаки або
 * використання навички. Бій триває доти, поки всі дроїди однієї з команд не будуть знищені.
 * Після завершення бою результат записується у файл через {@link FileManager}.
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
     * Запускає командний бій між двома командами.
     * Визначає випадковим чином, яка команда починає атаку,
     * після чого команди по черзі виконують дії, доки
     * не залишиться жодного живого дроїда в одній із них.
     * Після завершення бою записує переможця у лог.
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
     * Виводить склад команди у консоль та записує його у файл логу.
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
     * Виконує хід однієї команди: вибір атакуючого дроїда та його цілі.
     *
     * @param team1Name назва команди, що атакує
     * @param team1 список дроїдів команди, що атакує
     * @param team2Name назва команди-захисника
     * @param team2 список дроїдів команди-захисника
     * @return текстовий опис дії або {@code null}, якщо хід неможливий
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

    /** Список для збереження поточної команди, до якої належить медик. */
    private List<Droid> medicsTeam = new ArrayList<>();

    /**
     * Виконує дію дроїда (атака або навичка) та повертає короткий опис результату.
     *
     * @param performer дроїд, який виконує дію
     * @param target дроїд, на якого спрямована дія
     * @param choice вибір дії (1 — атака, 2 — навичка)
     * @return текстовий опис результату дії
     */
    protected String executeActionAndGetDescription(Droid performer, Droid target, int choice) {
        switch (choice) {
            case 1:
                performer.attack(target);
                performer.setCanUseSkill(true);
                return " DMG: " + target.getIncomingDamage();
            case 2:
                if ((performer instanceof Medic)) {
                    String teamName = droidTeams.get(performer);

                    if (teamName.equals(teamName1)) {
                        medicsTeam = team1;
                    } else {
                        medicsTeam = team2;
                    }

                    ((Medic) performer).skill(medicsTeam, true);
                    performer.setCanUseSkill(false);
                    return " SKILL [HEAL: 25] ";
                } else {
                    performer.attack(target);
                    performer.skill();
                    performer.setCanUseSkill(false);
                    return " SKILL [DMG: "  + target.getIncomingDamage() + "]";
                }
            default:
                return " Invalid choice. Skipping turn!";
        }
    }

    /**
     * Формує текстовий звіт про дію дроїда під час бою.
     *
     * @param performer дроїд, який виконує дію
     * @param target дроїд, який зазнає дії
     * @param actionDescription короткий опис дії (наприклад, "attacks", "heals")
     * @return відформатований рядок з повним описом дії
     */
    protected String buildActionLog(Droid performer, Droid target, String actionDescription) {
        String actionHeader;
        String statePart;

        Droid ally = null;
        for (Droid d : medicsTeam) {
            if (d != performer && d.isAlive()) {
                ally = d;
                break;
            }
        }

        if (performer instanceof Medic && !performer.isCanUseSkill() && ally != null && ally.isAlive()) {
            actionHeader = "| " + performer.getName() + "( " + getTeamName(performer) + " ) -> "
                    + ally.getName() + "( " + getTeamName(ally) + " ) | ";
            statePart = performer + "; " + ally + " |";
        } else {
            actionHeader = "| " + performer.getName() + "( " + getTeamName(performer) + " ) -> "
                    + target.getName() + "( " + getTeamName(target) + " ) | ";
            statePart = performer + "; " + target + " |";
        }

        String actionPart = actionDescription + " | ";

        return actionHeader + actionPart + statePart;
    }

    /**
     * Повертає назву команди, до якої належить конкретний дроїд.
     *
     * @param d дроїд
     * @return назву команди або "Unknown", якщо дроїда не знайдено
     */
    private String getTeamName(Droid d) {
        return droidTeams.getOrDefault(d, "Unknown");
    }

    /**
     * Перевіряє, чи є живі дроїди в команді.
     *
     * @param team список дроїдів
     * @return true, якщо в команді є хоча б один живий дроїд
     */
    private boolean isTeamAlive(ArrayList<Droid> team) {
        for (Droid d : team) {
            if (d.isAlive()) return true;
        }
        return false;
    }

    /**
     * Дозволяє користувачу обрати дроїда для дії.
     * Перевіряє правильність індексу та чи дроїд живий.
     *
     * @param team список дроїдів, серед яких здійснюється вибір
     * @return вибраний дроїд або null, якщо вибір неможливий
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
