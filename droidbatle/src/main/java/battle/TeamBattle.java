package battle;
import droids.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TeamBattle extends Battle{

    private final ArrayList<Droid> team1;
    private final ArrayList<Droid> team2;
    private final String teamName1;
    private final String teamName2;
    private static final Scanner scanner = new Scanner(System.in);
    private final Map<Droid, String> droidTeams = new HashMap<>();

    public TeamBattle(ArrayList<Droid> team1, String teamName1, ArrayList<Droid> team2, String teamName2) {
        this.team1 = team1;
        this.team2 = team2;
        this.teamName1 = teamName1;
        this.teamName2 = teamName2;

        for (Droid d : team1) droidTeams.put(d, teamName1);
        for (Droid d : team2) droidTeams.put(d, teamName2);
    }

    public void run() {

        ArrayList<Droid> attackerTeam;
        ArrayList<Droid> defenderTeam;
        String attackerName;
        String defenderName;

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

    private void printTeam(ArrayList<Droid> team) {
        int i = 0;
        for (Droid d : team) {
            FileManager.saveBattle("| " + (i++) + ") " + d + " |");
        }
    }

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


    @Override
    protected  String buildActionLog(Droid performer, Droid target, String actionDescription) {
        String actionHeader = "| " + performer.getName() + "( " + getTeamName(performer) + " ) -> "
                + target.getName() + "( " + getTeamName(target) + " ) | ";

        String actionPart = actionDescription + " | ";

        String statePart = performer + "; " + target + " |";

        return actionHeader + actionPart + statePart;
    }

    private String getTeamName(Droid d) {
        return droidTeams.getOrDefault(d, "Unknown");
    }

    private boolean isTeamAlive(ArrayList<Droid> team) {
        for (Droid d : team) {
            if (d.isAlive()) return true;
        }
        return false;
    }

    private Droid chooseDroid(ArrayList<Droid> team) {
        Droid chosen = null;
        while (true) {
            if (!isTeamAlive(team)) break;
            int choice = scanner.nextInt();
            if (choice < 0 || choice >= team.size()) {
                System.err.print("Invalid choice! please try again: -> ");
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