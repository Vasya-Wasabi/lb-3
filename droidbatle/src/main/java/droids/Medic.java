package droids;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Клас Medic представляє бойового дроїда-медика.
 * Медик має середній рівень здоров’я і невелику базову шкоду,
 * але компенсує це здатністю відновлювати здоров’я.
 * Він може лікувати себе після атаки або використовувати
 * спеціальну навичку для відновлення здоров’я собі чи союзнику.
 * Характеристики:
 * - Максимальне здоров’я: 80
 * - Базова шкода: 10
 * - 30% шанс зцілитися на 10 одиниць після атаки
 * - Навичка: миттєве відновлення 25 одиниць здоров’я
 */
public class Medic extends Droid {

    /**
     * Створює нового дроїда-медика з указаним ім’ям.
     *
     * @param name ім’я дроїда
     */
    public Medic(String name) {
        super(name, 80, 10);
    }

    /**
     * Виконує атаку по ворожому дроїду.
     * Після нанесення шкоди існує 30% шанс,
     * що медик вилікує себе на 10 одиниць здоров’я.
     *
     * @param target ціль, по якій здійснюється атака
     */
    @Override
    public void attack(Droid target) {
        target.takeDamage(damage);
        double chance = Math.random();

        if (chance < 0.3) heal(10, this);
    }

    /**
     * Обробляє отримання шкоди від ворога.
     * Здоров’я зменшується на задану кількість одиниць,
     * але не може впасти нижче нуля.
     *
     * @param incomingDamage кількість отриманої шкоди
     */
    @Override
    public void takeDamage(double incomingDamage) {
        setIncomingDamage(incomingDamage);
        health -= incomingDamage;
        if (health < 0) health = 0;
    }

    /**
     * Відновлює певну кількість здоров’я вказаному дроїду.
     * Якщо після лікування здоров’я перевищує максимальне,
     * воно обмежується значенням getMaxHealth().
     *
     * @param amount кількість одиниць здоров’я для відновлення
     * @param target дроїд, якому застосовується лікування
     */
    private void heal(int amount, Droid target) {
        target.health += amount;
        if (target.health > target.getMaxHealth()) target.health = target.getMaxHealth();
    }

    /**
     * Активує спеціальну навичку медика.
     * Навичка миттєво відновлює 25 одиниць здоров’я.
     * У командному бою медик може вибрати, кого лікувати:
     * себе або союзника (якщо він живий).
     * Якщо бій не командний або союзників немає,
     * медик лікує себе автоматично.
     *
     * @param team список дроїдів у поточній команді
     * @param isTeamBattle true, якщо це командний бій;
     *                     false, якщо одиночний
     */
    public void skill(List<Droid> team, boolean isTeamBattle) {
        Droid ally = null;
        for (Droid d : team) {
            if (d != this && d.isAlive()) {
                ally = d;
                break;
            }
        }

        if (!isTeamBattle || ally == null || !(ally.isAlive())) {
            heal(25, this);
            return;
        }

        final Scanner scanner = new Scanner(System.in);
        System.out.print("Which heal? 1) Yourself 2) Teammate: -> ");

        int choice;

        while (true) {
            try{
                choice = scanner.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Error! Please enter a valid number.\n");
                scanner.nextLine();
                continue;
            }

            scanner.nextLine();

            if (choice == 1) {
                heal(25, this);
            }
            else if (choice == 2) {
                heal(25, ally);
            }
            else {
                System.out.println("Error! Enter 1 or 2.");
                continue;
            }
            break;
        }
    }
}
