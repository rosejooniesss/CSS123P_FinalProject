package css123p_finalproject.model.weapons;

import css123p_finalproject.model.Weapons;
import css123p_finalproject.model.Player;
import css123p_finalproject.model.Mob;
import css123p_finalproject.model.Projectile;
import java.awt.Color;
import java.util.ArrayList;

public class Staff extends Weapons {
    public Staff() {
        super("STAFF (Magic)", 80, 2000); // 80 dmg, 2.0s
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
                    8, this.damage, Color.CYAN
                ));
                recordAttack(now);
                break;
            }
        }
    }
}