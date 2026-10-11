package css123p_finalproject.model;

import java.util.ArrayList;

public abstract class Weapons {
    protected String name;
    protected int damage;
    protected long attackInterval; // in milliseconds
    protected long lastAttackTime = 0;

    public Weapons(String name, int damage, long attackInterval) {
        this.name = name;
        this.damage = damage;
        this.attackInterval = attackInterval;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public boolean canAttack(long currentTime) {
        return (currentTime - lastAttackTime) >= attackInterval;
    }

    public void recordAttack(long currentTime) {
        this.lastAttackTime = currentTime;
    }

    public abstract void use(Player player, int targetX, int targetY, ArrayList<Mob> enemies, ArrayList<Projectile> projectiles);
}