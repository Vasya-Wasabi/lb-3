package battle;

import droids.*;

/**
 * Клас Duel відповідає за проведення поєдинку між двома дроїдами.
 *
 * У цьому режимі бою дроїди по черзі виконують дії (атаку або використання навички),
 * доки один із них не загине. Усі дії та результати бою записуються у файл через FileManager.
 */
public class Duel extends Battle {

    /** Перший дроїд, який бере участь у дуелі. */
    private final Droid d1;

    /** Другий дроїд, який бере участь у дуелі. */
    private final Droid d2;

    /**
     * Створює новий об'єкт дуелі між двома дроїдами.
     *
     * @param d1 перший дроїд
     * @param d2 другий дроїд
     */
    public Duel(Droid d1, Droid d2) {
        this.d1 = d1;
        this.d2 = d2;
    }

    /**
     * Запускає поєдинок між двома дроїдами.
     *
     * Випадковим чином визначається, хто ходить першим.
     * У кожному раунді обидва дроїди виконують по одній дії (якщо залишаються живими).
     * Після завершення бою визначається переможець і зберігається фінальний результат.
     */
    public void run() {
        Droid attacker;
        Droid defender;

        // Випадковий вибір, хто ходить першим
        if (Math.random() < 0.5) {
            attacker = d1;
            defender = d2;
        } else {
            attacker = d2;
            defender = d1;
        }

        // Початок бою — запис у лог
        FileManager.saveBattle("\nDUEL START: " + d1.getName() + " VS " + d2.getName() + "\n");
        FileManager.saveBattle(attacker.getClass().getSimpleName() + " " + attacker);
        FileManager.saveBattle(defender.getClass().getSimpleName() + " " + defender + "\n");

        int countOfRound = 1;

        // Основний цикл бою
        while (attacker.isAlive() && defender.isAlive()) {
            String log1 = collectTurnLog(attacker, defender);
            if (!defender.isAlive()) break;

            String log2 = collectTurnLog(defender, attacker);
            if (!attacker.isAlive()) break;

            FileManager.saveBattle("\n| ROUND " + countOfRound++ + " |");
            FileManager.saveBattle(log1);
            FileManager.saveBattle(log2);
            FileManager.saveBattle("");
        }

        // Завершення бою — визначення переможця
        FileManager.saveBattle("\n| DUEL ENDED |");
        if (attacker.isAlive()) {
            FileManager.saveBattle("WINNER: " + attacker.getName() + "!");
        } else {
            FileManager.saveBattle("WINNER: " + defender.getName() + "!");
        }
    }
}
