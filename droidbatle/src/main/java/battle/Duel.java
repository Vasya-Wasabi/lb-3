package battle;

import droids.*;

import java.util.ArrayList;

/**
 * Клас {@code Duel} відповідає за проведення поєдинку між двома дроїдами.
 * У цьому режимі бою дроїди по черзі виконують дії (атаку або використання навички),
 * доки один із них не загине. Усі дії та результати поєдинку
 * записуються у файл журналу через {@link FileManager}.
 */
public class Duel extends Battle {

    /** Перший дроїд, який бере участь у дуелі. */
    private final Droid d1;

    /** Другий дроїд, який бере участь у дуелі. */
    private final Droid d2;

    /**
     * Створює новий об'єкт {@code Duel} між двома дроїдами.
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
     * Випадковим чином визначається, хто починає хід.
     * У кожному раунді обидва дроїди виконують дії (якщо залишаються живими).
     * Після завершення бою визначається переможець, а фінальний результат
     * записується у файл журналу.
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

    /**
     * Виконує вибрану дію дроїда (атаку або навичку)
     * та повертає короткий опис результату.
     *
     * @param performer дроїд, який виконує дію
     * @param target дроїд, проти якого застосовується дія
     * @param choice вибір дії (1 — атака, 2 — навичка)
     * @return короткий опис дії, наприклад "DMG: 25" або "SKILL"
     */
    protected String executeActionAndGetDescription(Droid performer, Droid target, int choice) {
        switch (choice) {
            case 1:
                performer.attack(target);
                performer.setCanUseSkill(true);
                return " DMG: " + target.getIncomingDamage();
            case 2:
                if ((performer instanceof Medic)) {
                    ((Medic) performer).skill(new ArrayList<>(), false);
                    performer.setCanUseSkill(false);
                    return " SKILL [HEAL: 25] ";
                } else {
                    performer.skill();
                    performer.attack(target);
                    performer.setCanUseSkill(false);
                    return " SKILL [DMG: "  + target.getIncomingDamage() + "]";
                }
            default:
                return " Invalid choice. Skipping turn!";
        }
    }

    /**
     * Формує текстовий опис дії дроїда у форматі:
     * | Виконавець -> Ціль | Дія | Поточний стан дроїдів |
     *
     * @param performer дроїд, який виконує дію
     * @param target дроїд, який зазнає дії
     * @param actionDescription короткий опис результату дії
     * @return сформований рядок з описом ходу
     */
    protected String buildActionLog(Droid performer, Droid target, String actionDescription) {
        String actionHeader;
        String statePart;

        if (performer instanceof Medic && !performer.isCanUseSkill()) {
            actionHeader = "| " + performer.getName() + " -> " + performer.getName() + " | ";
            statePart = performer + "; " + performer + " |";
        } else {
            actionHeader = "| " + performer.getName() + " -> " + target.getName() + " | ";
            statePart = performer + "; " + target + " |";
        }

        String actionPart = actionDescription + " | ";
        return actionHeader + actionPart + statePart;
    }
}
