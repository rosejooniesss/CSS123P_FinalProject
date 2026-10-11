package css123p_finalproject.model;

import java.awt.Color;
import java.util.ArrayList;

public class Skeleton extends Mob {
    private long lastShotTime = 0;
    private long shootInterval = 2500; // 2.5 seconds

    public Skeleton(int startX, int startY, double speed, int HP, int ATK, int DEF, int STAMINA) {
        super(startX, startY, speed, HP, ATK, DEF, STAMINA);
    }

    @Override
    public void updateAI(Player player, ArrayList<Mob> allMobs, ArrayList<Projectile> enemyProjectiles) {
        if (!isAlive()) return;

        // Slower movement toward player
        if (this.x > player.getX() + 5) moveLeft();
        else if (this.x < player.getX() - 5) moveRight();

        if (this.y > player.getY() + 5) moveUp();
        else if (this.y < player.getY() - 5) moveDown();

        // Prevent overlap
        for (Mob other : allMobs) {
            if (other == this || !other.isAlive()) continue;
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            double distance = Math.hypot(dx, dy);
            if (distance < this.width && distance > 0) {
                double overlap = this.width - distance;
                double pushX = (dx / distance) * (overlap / 2.0);
                double pushY = (dy / distance) * (overlap / 2.0);
                this.adjustPosition(pushX, pushY);
                other.adjustPosition(-pushX, -pushY);
            }
        }

        // Shoot spell at 50% player speed
        long now = System.currentTimeMillis();
        if (now - lastShotTime >= shootInterval) {
            int projSpeed = (int) Math.max(2, player.SPEED * 0.5);
            enemyProjectiles.add(new Projectile(
                this.x + (this.width / 2), this.y + (this.height / 2),
                player.getX() + (player.getWidth() / 2), player.getY() + (player.getHeight() / 2),
                projSpeed, getATK(), Color.MAGENTA, true
            ));
            lastShotTime = now;
        }
    }
}