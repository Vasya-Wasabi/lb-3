package droids;

public class Tank extends Droid {

    private double attackMultiplier = 1.0;
    private double damageReceivedMultiplier = 1.0;

    public Tank(String name) {
        super(name, 150, 20);
    }

    public void attack(Droid target) {
        target.takeDamage((int)(damage * attackMultiplier));
        attackMultiplier = 1.0;
    }

    public void takeDamage(int incomingDamage) {
        double chance = Math.random();
        int finalIncomingDamage = (int)(incomingDamage * damageReceivedMultiplier);

        if(damageReceivedMultiplier == 1.0 && chance < 0.3)
            finalIncomingDamage = (int)(finalIncomingDamage * 0.3);

        health -= finalIncomingDamage;
        if(health < 0) health = 0;

        damageReceivedMultiplier = 1.0;
    }

    public void skill(){
        attackMultiplier = 0.5;
        damageReceivedMultiplier = 0.5;
    }
}
