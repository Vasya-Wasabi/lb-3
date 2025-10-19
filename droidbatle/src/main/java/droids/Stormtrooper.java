package droids;

/**
 * Клас Stormtrooper представляє універсального бойового дроїда,
 * який поєднує високу витривалість зі збалансованою атакою.
 * Штурмовик може тимчасово підвищити свою силу завдяки навичці,
 * що збільшує шкоду як при атаці, так і при отриманні удару.
 * Основні характеристики:
 *  - Здоров’я: 105
 *  - Базова шкода: 25
 *  - Навичка: збільшує всі дії (атака та отримання шкоди) на 20% та 30% протягом одного ходу
 */
public class Stormtrooper extends Droid {

    /** Множники для підсилення або ослаблення ефектів у бою. */
    private double attackMultiplier = 1.0;
    private double damageReceivedMultiplier = 1.0;

    /**
     * Створює нового штурмовика з указаним ім’ям.
     *
     * @param name ім’я дроїда
     */
    public Stormtrooper(String name) {
        super(name, 105, 25);
    }

    /**
     * Виконує атаку по цілі.
     * Під час дії навички шкода збільшується на 20%.
     * Після атаки множник повертається до 1.0.
     *
     * @param target ціль, по якій здійснюється атака
     */
    @Override
    public void attack(Droid target) {
        double actualDamage = damage * attackMultiplier;
        target.takeDamage(actualDamage);
        attackMultiplier = 1.0;
    }

    /**
     * Обробляє отримання шкоди.
     * Якщо активовано навичку, то шкода, яку отримує дроїд, також збільшується на 20%.
     * Після цього множник скидається до 1.0.
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
     * Активує спеціальну навичку штурмовика.
     * Підвищує ефективність дій на 20% для наступного ходу.
     */
    @Override
    public void skill() {
        attackMultiplier = 1.2;
        damageReceivedMultiplier = 1.3;
    }
}
