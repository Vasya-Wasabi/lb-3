package droids;

/**
 * Клас Sniper представляє дроїда-снайпера, який спеціалізується на потужних і точних атаках.
 * Снайпер має шанс завдати критичної шкоди, а також здатен активувати навичку,
 * що дозволяє уникнути шкоди, але зменшує силу його атаки.
 * Основні характеристики:
 *  - Здоров’я: 65
 *  - Базова шкода: 30
 *  - 30% шанс критичної атаки (подвійна шкода)
 *  - Навичка: 70% шанс активувати режим фокусування, який
 *    знижує отриману шкоду до нуля на один хід,
 *    але зменшує власну атаку до 60% від звичайної.
 */
public class Sniper extends Droid {

    /** Множник шкоди, яку наносить снайпер. */
    private double attackMultiplier = 1.0;

    /** Множник шкоди, яку отримує снайпер. */
    private double damageReceivedMultiplier = 1.0;

    /**
     * Створює нового дроїда-снайпера з вказаним ім’ям.
     *
     * @param name ім’я дроїда
     */
    public Sniper(String name) {
        super(name, 65, 30);
    }

    /**
     * Виконує атаку по цілі.
     * Існує 30% шанс нанести подвійну шкоду.
     * Після атаки множник шкоди скидається до 1.0.
     *
     * @param target ціль, по якій здійснюється атака
     */
    @Override
    public void attack(Droid target) {
        double actualDamage = damage * attackMultiplier;

        double chance = Math.random();

        if (attackMultiplier == 1.0 && chance < 0.3) {
            target.takeDamage(actualDamage * 2);
        } else {
            target.takeDamage(actualDamage);
        }

        attackMultiplier = 1.0;
    }

    /**
     * Обробляє отримання шкоди від ворога.
     * Отримана шкода множиться на коефіцієнт damageReceivedMultiplier.
     * Після цього множник повертається до 1.0.
     *
     * @param incomingDamage кількість отриманої шкоди
     */
    @Override
    public void takeDamage(double incomingDamage) {
        double finalIncomingDamage = incomingDamage * damageReceivedMultiplier;
        setIncomingDamage(finalIncomingDamage);
        health -= finalIncomingDamage;
        if (health < 0) health = 0;

        damageReceivedMultiplier = 1.0;
    }

    /**
     * Активує навичку снайпера — режим фокусування.
     * З імовірністю 70% снайпер не отримує шкоди протягом одного ходу,
     * але сила його атаки знижується до 60% від звичайної.
     */
    @Override
    public void skill() {
        double chance = Math.random();
        if (chance < 0.7) {
            attackMultiplier = 0.6;
            damageReceivedMultiplier = 0.0;
        }
    }
}
