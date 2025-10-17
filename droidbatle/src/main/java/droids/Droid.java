package droids;

/**
 * Абстрактний клас Droid описує базову поведінку та характеристики
 * будь-якого бойового дроїда.
 *
 * Кожен дроїд має ім’я, запас здоров’я, силу атаки
 * та можливість використовувати спеціальну навичку.
 * Конкретна реалізація атаки, отримання шкоди та навички
 * визначається у підкласах.
 */
public abstract class Droid {

    /** Ім’я дроїда. */
    protected String name;

    /** Поточний рівень здоров’я дроїда. */
    protected int health;

    /** Базова шкода, яку дроїд завдає при атаці. */
    protected int damage;

    /** Прапорець, що вказує, чи може дроїд використати навичку. */
    protected boolean canUseSkill = true;

    /**
     * Створює нового дроїда з указаними параметрами.
     *
     * @param name   ім’я дроїда
     * @param health початковий рівень здоров’я
     * @param damage базова шкода
     */
    public Droid(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    /**
     * Перевіряє, чи дроїд ще живий.
     *
     * @return true, якщо здоров’я більше 0; інакше false
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Повертає ім’я дроїда.
     *
     * @return ім’я
     */
    public String getName() {
        return name;
    }

    /**
     * Повертає базову шкоду дроїда.
     *
     * @return шкода
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Повертає стан доступності навички.
     *
     * @return true, якщо дроїд може використати навичку
     */
    public boolean isCanUseSkill() {
        return canUseSkill;
    }

    /**
     * Встановлює можливість використання навички.
     *
     * @param canUseSkill нове значення (true — можна, false — ні)
     */
    public void setCanUseSkill(boolean canUseSkill) {
        this.canUseSkill = canUseSkill;
    }

    /**
     * Виконує атаку по вказаній цілі.
     *
     * @param target дроїд, який отримує атаку
     */
    public abstract void attack(Droid target);

    /**
     * Обробляє отримання шкоди.
     *
     * @param incomingDamage кількість отриманої шкоди
     */
    public abstract void takeDamage(int incomingDamage);

    /**
     * Викликає спеціальну навичку дроїда.
     * Реалізація залежить від конкретного типу дроїда.
     */
    public abstract void skill();

    /**
     * Повертає короткий опис дроїда у форматі:
     * "Name [HP: X, Damage: Y]".
     *
     * @return рядкове представлення дроїда
     */
    @Override
    public String toString() {
        return name + " [HP: " + health + ", Damage: " + damage + "]";
    }
}
