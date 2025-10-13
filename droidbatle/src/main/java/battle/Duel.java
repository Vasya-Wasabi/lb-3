package battle;
import droids.*;

public class Duel extends Battle {

    private final Droid d1;
    private final Droid d2;

    public Duel(Droid d1, Droid d2) {
        this.d1 = d1;
        this.d2 = d2;
    }

    public void run() {

        Droid attacker;
        Droid defender;

        if(Math.random() < 0.5) {
            attacker = d1;
            defender = d2;
        } else {
            attacker = d2;
            defender = d1;
        }

        FileManager.saveBattle("\nDUEL START: " + d1.getName() + " VS " + d2.getName());
        FileManager.saveBattle(attacker.getClass().getSimpleName() + " " + attacker);
        FileManager.saveBattle(defender.getClass().getSimpleName() + " " + defender);

        int countOfRound = 1;
        while(attacker.isAlive() && defender.isAlive()) {

            String log1 = collectTurnLog(attacker, defender);
            if(!defender.isAlive()) break;

            String log2 = collectTurnLog(defender, attacker);
            if(!attacker.isAlive()) break;

            FileManager.saveBattle("\n| ROUND " + countOfRound++ +  " |");
            FileManager.saveBattle(log1);
            FileManager.saveBattle(log2);
            FileManager.saveBattle("");
        }

        FileManager.saveBattle("\n| DUEL ENDED |");
        if (attacker.isAlive()) {
            FileManager.saveBattle("WINNER: " + attacker.getName() + "!");
        } else {
            FileManager.saveBattle("WINNER: " + defender.getName() + "!");
        }
    }
}
