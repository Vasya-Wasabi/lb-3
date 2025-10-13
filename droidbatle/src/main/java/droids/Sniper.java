package droids;

public class Sniper extends Droid {

    private double attackMultiplier = 1.0;
    private double damageReceivedMultiplier = 1.0;

    public Sniper(String name) {
        super(name, 65, 30);
    }

    @Override
    public void attack(Droid target) {
        int actualDamage  = (int)(damage * attackMultiplier);

        double chance = Math.random();

        if(attackMultiplier == 1.0 && chance < 0.3) {
            target.takeDamage(actualDamage * 2);
        } else {
            target.takeDamage(actualDamage);
        }

        attackMultiplier = 1.0;
    }

    @Override
    public void takeDamage(int incomingDamage) {
        int finalIncomingDamage = (int)(incomingDamage * damageReceivedMultiplier);
        health -= finalIncomingDamage;
        if(health < 0) health = 0;

        damageReceivedMultiplier = 1.0;
    }

    public void skill(){
        double chance = Math.random();
        if(chance < 0.7) {
            attackMultiplier = 0.6;
            damageReceivedMultiplier = 0.0;
        }
    }
}
