package droids;

/**
 * Клас Tank представляє важкого бойового дроїда з великою кількістю здоров’я
 * і високим рівнем захисту. Його роль — приймати основні удари супротивника.
 * Особливість танка полягає у здатності зменшувати отриману шкоду та
 * іноді суттєво блокувати атаки ворога.
 * Основні характеристики:
 *  - Здоров’я: 150
 *  - Базова шкода: 20
 *  - Є 30% шанс зменшити вхідну шкоду до 30% від звичайної.
 *  - Навичка: зменшує завдану й отриману шкоду вдвічі протягом одного ходу
 */
public class Tank extends Droid {

    /** Множник для підсилення або ослаблення атаки. */
    private double attackMultiplier = 1.0;

    /** Множник для регулювання отриманої шкоди. */
    private double damageReceivedMultiplier = 1.0;

    /**
     * Створює нового танка з указаним ім’ям.
     *
     * @param name ім’я дроїда
     */
    public Tank(String name) {
        super(name, 150, 20);
    }

    /**
     * Виконує атаку по цілі. Якщо активовано навичку,
     * сила атаки зменшується наполовину.
     * Після атаки множник скидається до 1.0.
     *
     * @param target ціль, по якій здійснюється атака
     */
    @Override
    public void attack(Droid target) {
        target.takeDamage((int)(damage * attackMultiplier));
        attackMultiplier = 1.0;
    }

    /**
     * Обробляє отримання шкоди.
     * Є 30% шанс зменшити вхідну шкоду до 30% від звичайної.
     * Якщо активовано навичку, загальна отримана шкода зменшується вдвічі.
     * Після цього множник скидається до 1.0.
     *
     * @param incomingDamage кількість отриманої шкоди
     */
    @Override
    public void takeDamage(double incomingDamage) {
        double chance = Math.random();
        double finalIncomingDamage = incomingDamage * damageReceivedMultiplier;
        setIncomingDamage(finalIncomingDamage);

        if (damageReceivedMultiplier == 1.0 && chance < 0.3)
            finalIncomingDamage = finalIncomingDamage * 0.3;

        health -= finalIncomingDamage;
        if (health < 0) health = 0;

        damageReceivedMultiplier = 1.0;
    }

    /**
     * Активує спеціальну навичку танка.
     * Зменшує як силу атаки, так і отриману шкоду вдвічі
     * для наступного ходу.
     */
    @Override
    public void skill() {
        attackMultiplier = 0.5;
        damageReceivedMultiplier = 0.5;
    }
}
