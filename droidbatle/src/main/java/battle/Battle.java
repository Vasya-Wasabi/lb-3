package battle;

import droids.Droid;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Абстрактний клас {@code Battle} визначає базову логіку проведення одного ходу бою
 * між двома дроїдами. Він відповідає за взаємодію з користувачем під час вибору дії
 * (атака або навичка), виконання вибраної дії та формування звіту про результати.
 *
 * Класи {@link Duel} та {@link TeamBattle} наслідують цей клас і реалізують
 * конкретну поведінку для своїх режимів бою.
 */
public abstract class Battle {

    /** Об'єкт для зчитування введення користувача з консолі. */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Проводить один хід для дроїда-виконавця проти вказаної цілі.
     *
     * Запитує у користувача вибір дії (1 — атака, 2 — навичка),
     * перевіряє можливість використання навички, виконує дію
     * та формує текстовий звіт про хід.
     *
     * @param performer дроїд, який виконує дію (атаку або навичку)
     * @param target дроїд, який є ціллю дії
     * @return текстовий звіт із описом дії, результатів і поточного стану дроїдів
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
     * Виконує дію (атаку або навичку) та повертає короткий опис результату.
     * Реалізується у підкласах {@link Duel} і {@link TeamBattle}.
     *
     * @param performer дроїд, який виконує дію
     * @param target дроїд, на якого спрямована дія
     * @param choice вибір дії (1 — атака, 2 — навичка)
     * @return короткий опис результату дії (наприклад, "DMG: 20" або "SKILL [HEAL: 25]")
     */
    protected abstract String executeActionAndGetDescription(Droid performer, Droid target, int choice);

    /**
     * Формує текстовий звіт про дію дроїда у зручному форматі.
     * Реалізується у підкласах {@link Duel} і {@link TeamBattle}.
     *
     * @param performer дроїд, який виконує дію
     * @param target ціль дії
     * @param actionDescription короткий опис дії
     * @return відформатований текстовий звіт про хід
     */
    protected abstract String buildActionLog(Droid performer, Droid target, String actionDescription);
}
