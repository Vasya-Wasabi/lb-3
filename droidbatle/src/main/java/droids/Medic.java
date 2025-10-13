package droids;

public class Medic extends Droid {

    public Medic(String name) {
        super(name, 80, 10);
    }

    public void attack(Droid target) {
        target.takeDamage(damage);
        double chance = Math.random();

        if(chance < 0.3) heal(10);
    }

    public void takeDamage(int incomingDamage) {
        health -= incomingDamage;
        if(health < 0) health = 0;
    }

    private void heal(int amount) {
        final int maxHealth = 80;
        health += amount;
        if(health > maxHealth) health = maxHealth;
    }

    public void skill() {
        heal(25);
    }
}
