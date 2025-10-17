package battle;

import droids.Droid;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Клас Battle відповідає за проведення одного ходу бою між двома дроїдами.
 *
 * Він зчитує вибір користувача (атака або навичка), виконує відповідну дію
 * та формує текстовий звіт про результати цього ходу.
 */
public class Battle {
    /** Об'єкт для зчитування введення користувача з консолі. */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Проводить хід для певного дроїда (виконавця) проти цілі.
     * Запитує у користувача вибір дії, виконує її та формує звіт про хід.
     *
     * @param performer дроїд, який виконує дію (атака або навичка)
     * @param target дроїд, який є ціллю дії
     * @return текстовий лог з описом ходу, результатів і поточного стану дроїдів
     */
    protected String collectTurnLog(Droid performer, Droid target) {
        boolean skillAllowed = performer.isCanUseSkill();
        int choice;

        while (true) {
            String prompt = "Choose action for " + performer.getName() + ": 1) Attack ";
            if (skillAllowed) {
                prompt += "2) Skill ";
            }
            prompt += "-> ";

            System.out.print(prompt);

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error! Invalid choice " + e.getMessage() + "\n");
                System.out.println();
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();

            if (choice == 2 && !skillAllowed) {
                System.out.println("Skill is not allowed. Automatically attack!");
                choice = 1;
            }

            break;
        }

        String actionDescription = executeActionAndGetDescription(performer, target, choice);
        return buildActionLog(performer, target, actionDescription);
    }

    /**
     * Виконує вибрану дію дроїда (атака або навичка) та повертає короткий опис результату.
     *
     * @param performer дроїд, який виконує дію
     * @param target дроїд, проти якого діє виконавець
     * @param choice вибір дії (1 — атака, 2 — навичка)
     * @return короткий опис дії, наприклад "DMG: 25" або "SKILL"
     */
    private String executeActionAndGetDescription(Droid performer, Droid target, int choice) {
        switch (choice) {
            case 1:
                performer.attack(target);
                performer.setCanUseSkill(true);
                return " DMG: " + performer.getDamage();
            case 2:
                performer.skill();
                performer.setCanUseSkill(false);
                return " SKILL  ";
            default:
                return " Invalid choice. Skipping turn!";
        }
    }

    /**
     * Формує повний текстовий запис про дію дроїда у форматі:
     * | Виконавець -> Ціль | Дія | Поточний стан дроїдів |
     *
     * @param performer дроїд, який виконував дію
     * @param target ціль дії
     * @param actionDescription короткий опис результату дії
     * @return рядок із детальним описом ходу
     */
    protected String buildActionLog(Droid performer, Droid target, String actionDescription) {
        String actionHeader = "| " + performer.getName() + " -> " + target.getName() + " | ";
        String actionPart = actionDescription + " | ";
        String statePart = performer + "; " + target + " |";
        return actionHeader + actionPart + statePart;
    }
}
