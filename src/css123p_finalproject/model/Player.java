package css123p_finalproject.model;

import css123p_finalproject.model.weapons.Sword;
import css123p_finalproject.model.weapons.Gun;
import css123p_finalproject.model.weapons.Staff;
import java.util.ArrayList;

public class Player extends Stats {
    private double x, y;
    private int width = 64;
    private int height = 64;

    public double SPEED;
    public double MAXHP;

    private Weapons[] loadout;
    private int activeSlot = 0;
    private long invincibilityEndTime = 0;

    public Player(int startX, int startY, int HP, int ATK, int DEF, int STAMINA, double speed) {
        super(HP, ATK, DEF, STAMINA);
        this.MAXHP = HP;
        this.x = startX;
        this.y = startY;
        this.SPEED = speed;

        this.loadout = new Weapons[]{
            new Sword(),
            new Gun(),
            new Staff()
        };
    }

    public int getX() { return (int) this.x; }
    public int getY() { return (int) this.y; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }

    public void setX(double x) { this.x = x; }
    public void setX(int x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setY(int y) { this.y = y; }

    public void selectSlot(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < loadout.length) {
            this.activeSlot = slotIndex;
        }
    }

    public int getActiveSlot() { return activeSlot; }

    public Weapons getEquippedWeapon() {
        return loadout[activeSlot];
    }

    public void dash(int dirX, int dirY) {
        if (dirX == 0 && dirY == 0) dirX = 1;
        double dashDistance = SPEED * 6;
        
        if (dirX != 0 && dirY != 0) {
            double factor = 1.0 / Math.hypot(dirX, dirY);
            this.x += dirX * factor * dashDistance;
            this.y += dirY * factor * dashDistance;
        } else {
            this.x += dirX * dashDistance;
            this.y += dirY * dashDistance;
        }

        this.invincibilityEndTime = System.currentTimeMillis() + 750;
    }

    public boolean isInvincible() {
        return System.currentTimeMillis() < invincibilityEndTime;
    }

    public void attack(int targetX, int targetY, ArrayList<Mob> enemies, ArrayList<Projectile> projectiles) {
        getEquippedWeapon().use(this, targetX, targetY, enemies, projectiles);
    }

    public void takeDamage(int amount) {
        if (isInvincible()) return;
        setHP(Math.max(0, getHP() - amount));
    }

    @Override public int getHP() { return this.HP; }
    @Override public void setHP(int hp) { this.HP = hp; }
    @Override public int getDEF() { return this.DEF; }
    @Override public void setDEF(int defense) { this.DEF = defense; }
    @Override public int getATK() { return this.ATK; }
    @Override public void setATK(int atk) { this.ATK = atk; }
    @Override public int getSTAM() { return this.STAMINA; }
    @Override public void setSTAM(int stamina) { this.STAMINA = stamina; }
}