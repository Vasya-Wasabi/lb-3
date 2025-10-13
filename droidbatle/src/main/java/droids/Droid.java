package droids;

public abstract class Droid {

    protected String name;
    protected int health;
    protected int damage;
    protected boolean canUseSkill = true;

    public Droid(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    public boolean isAlive() {return health > 0;}
    public String getName() {return name;}
    public int getDamage() {return damage;}
    public boolean isCanUseSkill() {return canUseSkill;}
    public void setCanUseSkill(boolean canUseSkill) {this.canUseSkill = canUseSkill;}

    public abstract void attack(Droid target);
    public abstract void takeDamage(int incomingDamage);
    public abstract void skill();

    @Override
    public String toString() {
        return name + " [HP: " + health + ", Damage: " + damage + "]";
    }
}
