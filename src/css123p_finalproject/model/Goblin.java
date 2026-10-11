package css123p_finalproject.model;

import java.util.ArrayList;

public class Goblin extends Mob {
    public Goblin(int startX, int startY, double speed, int HP, int ATK, int DEF, int STAMINA) {
        super(startX, startY, speed, HP, ATK, DEF, STAMINA);
    }

    @Override
    public void updateAI(Player player, ArrayList<Mob> allMobs, ArrayList<Projectile> enemyProjectiles) {
        if (!isAlive()) return;

        // 1. Chase Player on X and Y axes
        if (this.x > player.getX() + 5) moveLeft();
        else if (this.x < player.getX() - 5) moveRight();

        if (this.y > player.getY() + 5) moveUp();
        else if (this.y < player.getY() - 5) moveDown();

        // 2. Separation Physics (Prevent overlapping mobs)
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
    }
}