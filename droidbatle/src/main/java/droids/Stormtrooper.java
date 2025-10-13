package droids;

public class Stormtrooper extends Droid {

    private double multiplier = 1.0;

    public Stormtrooper(String name) {
        super(name, 105, 25);
    }

    public void attack(Droid target) {
        int actualDamage = (int)(damage * multiplier);
        target.takeDamage(actualDamage);
        multiplier = 1.0;    }

    public void takeDamage(int incomingDamage) {
        int finalIncomingDamage = (int)(incomingDamage * multiplier);
        health -= finalIncomingDamage;
        if(health < 0) health = 0;
        multiplier = 1.0;
    }

    public void skill() {
        multiplier = 1.2;
    }
}
