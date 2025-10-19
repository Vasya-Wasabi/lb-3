package droids;

/**
 * Абстрактний клас {@code Droid} описує базову модель будь-якого бойового дроїда.
 * Він визначає спільні характеристики та поведінку, такі як:
 * ім’я, здоров’я, базову шкоду, отримання ушкоджень і можливість використання навички.
 * Конкретна реалізація атаки, захисту та навички задається у підкласах.
 */
public abstract class Droid {

    /** Ім’я дроїда. */
    protected String name;

    /** Поточний рівень здоров’я дроїда. */
    protected double health;

    /** Базова шкода, яку дроїд завдає при атаці. */
    protected double damage;

    /** Кількість шкоди, яку дроїд отримав останньою атакою. */
    protected double incomingDamage;

    /** Вказує, чи може дроїд використати свою спеціальну навичку. */
    protected boolean canUseSkill = true;

    /** Максимальний рівень здоров’я дроїда. */
    protected final double maxHealth;

    /**
     * Створює нового дроїда з указаними параметрами.
     *
     * @param name   ім’я дроїда
     * @param health початковий рівень здоров’я
     * @param damage базова шкода, яку дроїд завдає
     */
    public Droid(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        maxHealth = health;
    }

    /**
     * Перевіряє, чи дроїд ще живий.
     *
     * @return true, якщо здоров’я більше ніж 0; інакше false
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Повертає ім’я дроїда.
     *
     * @return ім’я дроїда
     */
    public String getName() {
        return name;
    }

    /**
     * Повертає інформацію, чи може дроїд використати навичку.
     *
     * @return true, якщо навичку можна використати; false — якщо ні
     */
    public boolean isCanUseSkill() {
        return canUseSkill;
    }

    /**
     * Встановлює можливість використання навички.
     *
     * @param canUseSkill true — дозволити використання, false — заборонити
     */
    public void setCanUseSkill(boolean canUseSkill) {
        this.canUseSkill = canUseSkill;
    }

    /**
     * Виконує атаку по вказаній цілі.
     * Реалізується в підкласах відповідно до типу дроїда.
     *
     * @param target дроїд, який отримує атаку
     */
    public abstract void attack(Droid target);

    /**
     * Обробляє отримання шкоди.
     * Реалізується в підкласах, оскільки різні типи дроїдів
     * можуть по-різному реагувати на ушкодження.
     *
     * @param incomingDamage кількість отриманої шкоди
     */
    public abstract void takeDamage(double incomingDamage);

    /**
     * Встановлює кількість отриманої шкоди для поточного дроїда.
     *
     * @param incomingDamage кількість отриманої шкоди
     */
    protected void setIncomingDamage(double incomingDamage) {
        this.incomingDamage = incomingDamage;
    }

    /**
     * Повертає кількість отриманої шкоди під час останньої атаки.
     *
     * @return кількість отриманої шкоди
     */
    public double getIncomingDamage() {
        return incomingDamage;
    }

    /**
     * Повертає максимальний рівень здоров’я дроїда.
     *
     * @return максимальне здоров’я
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Викликає спеціальну навичку дроїда.
     * Базова реалізація порожня, оскільки
     * кожен тип дроїда має власну унікальну навичку.
     */
    public void skill() {}

    /**
     * Повертає короткий опис дроїда у форматі:
     * "Name [HP: X, Damage: Y]".
     *
     * @return рядкове представлення стану дроїда
     */
    @Override
    public String toString() {
        return name + " [HP: " + health + ", Damage: " + damage + "]";
    }
}
