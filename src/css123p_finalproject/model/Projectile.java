package css123p_finalproject.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Projectile {
    private double x, y;
    private double dx, dy;
    private int damage;
    private Color color;
    private boolean enemyOwned;

    // 7-argument constructor for Player weapons (defaults enemyOwned to false)
    public Projectile(double startX, double startY, double targetX, double targetY, int speed, int damage, Color color) {
        this(startX, startY, targetX, targetY, speed, damage, color, false);
    }

    // 8-argument constructor for Enemy Spells (explicitly sets enemyOwned)
    public Projectile(double startX, double startY, double targetX, double targetY, int speed, int damage, Color color, boolean enemyOwned) {
        this.x = startX;
        this.y = startY;
        this.damage = damage;
        this.color = color;
        this.enemyOwned = enemyOwned;

        double angle = Math.atan2(targetY - startY, targetX - startX);
        this.dx = Math.cos(angle) * speed;
        this.dy = Math.sin(angle) * speed;
    }

    public void update() {
        this.x += this.dx;
        this.y += this.dy;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) x - 4, (int) y - 4, 8, 8);
    }

    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public int getDamage() { return damage; }
    public boolean isEnemyOwned() { return enemyOwned; }
}