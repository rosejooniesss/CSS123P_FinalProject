package css123p_finalproject.model.weapons;

import css123p_finalproject.model.Weapons;
import css123p_finalproject.model.Player;
import css123p_finalproject.model.Mob;
import css123p_finalproject.model.Projectile;
import java.util.ArrayList;

public class Sword extends Weapons {
    public Sword() {
        super("SWORD (Melee)", 50, 500); // 50 dmg, 0.5s
    }

    @Override
    public void use(Player player, int targetX, int targetY, ArrayList<Mob> enemies, ArrayList<Projectile> projectiles) {
        long now = System.currentTimeMillis();
        if (!canAttack(now)) return;

        for (Mob enemy : enemies) {
            if (enemy.isAlive() && targetX >= enemy.getX() && targetX <= enemy.getX() + enemy.getWidth()
                    && targetY >= enemy.getY() && targetY <= enemy.getY() + enemy.getHeight()) {
                double dist = Math.hypot(enemy.getX() - player.getX(), enemy.getY() - player.getY());
                if (dist <= player.getWidth() * 3.0) {
                    enemy.takeDamage(this.damage);
                    recordAttack(now);
                }
                break;
            }
        }
    }
}