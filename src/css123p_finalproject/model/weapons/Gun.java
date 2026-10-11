package css123p_finalproject.model.weapons;

import css123p_finalproject.model.Weapons;
import css123p_finalproject.model.Player;
import css123p_finalproject.model.Mob;
import css123p_finalproject.model.Projectile;
import java.awt.Color;
import java.util.ArrayList;

public class Gun extends Weapons {
    public Gun() {
        super("GUN (Ranged)", 30, 300); // 30 dmg, 0.3s
    }

    @Override
    public void use(Player player, int targetX, int targetY, ArrayList<Mob> enemies, ArrayList<Projectile> projectiles) {
        long now = System.currentTimeMillis();
        if (!canAttack(now)) return;

        for (Mob enemy : enemies) {
            if (enemy.isAlive() && targetX >= enemy.getX() && targetX <= enemy.getX() + enemy.getWidth()
                    && targetY >= enemy.getY() && targetY <= enemy.getY() + enemy.getHeight()) {
                projectiles.add(new Projectile(
                    player.getX(), player.getY(),
                    enemy.getX() + (enemy.getWidth() / 2),
                    enemy.getY() + (enemy.getHeight() / 2),
                    15, this.damage, Color.YELLOW
                ));
                recordAttack(now);
                break;
            }
        }
    }
}