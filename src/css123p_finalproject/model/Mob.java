package css123p_finalproject.model;

import java.util.ArrayList;

public abstract class Mob extends Stats {
    protected double x, y;
    protected int width = 64;
    protected int height = 64;
    protected double speed;
    public long lastAttackTime = 0;

    public Mob(int startX, int startY, double speed, int HP, int ATK, int DEF, int STAMINA) {
        super(HP, ATK, DEF, STAMINA);
        this.x = startX;
        this.y = startY;
        this.speed = speed;
    }

    public int getX() { return (int)this.x; }
    public int getY() { return (int)this.y; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public void moveUp() { this.y -= this.speed; }
    public void moveDown() { this.y += this.speed; }
    public void moveLeft() { this.x -= this.speed; }
    public void moveRight() { this.x += this.speed; }

    public void adjustPosition(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void takeDamage(int amount) {
        setHP(Math.max(0, getHP() - amount));
    }

    public boolean isAlive() {
        return getHP() > 0;
    }

    // Abstract AI contract for specific enemy types
    public abstract void updateAI(Player player, ArrayList<Mob> allMobs, ArrayList<Projectile> enemyProjectiles);

    @Override public int getHP(){ return this.HP; }
    @Override public void setHP(int hp){ this.HP = hp; }
    @Override public int getDEF(){ return this.DEF; }
    @Override public void setDEF(int defense){ this.DEF = defense; }
    @Override public int getATK(){ return this.ATK; }
    @Override public void setATK(int atk){ this.ATK = atk; }
    @Override public int getSTAM(){ return this.STAMINA; }
    @Override public void setSTAM(int stamina){ this.STAMINA = stamina; }
}