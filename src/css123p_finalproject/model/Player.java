package css123p_finalproject.model;

import css123p_finalproject.model.weapons.Sword;
import css123p_finalproject.model.weapons.Gun;
import css123p_finalproject.model.weapons.Staff;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Player extends Stats {
    private double x, y;
    private int width = 64;
    private int height = 64;
     private BufferedImage sprite;

    public double SPEED;
    public double MAXHP;

    private Weapons[] loadout;
    private int activeSlot = 0; // 0: Sword, 1: Gun, 2: Staff

    public Player(int startX, int startY, int HP, int ATK, int DEF, int STAMINA, double speed) {
        super(HP, ATK, DEF, STAMINA);
        this.MAXHP = HP;
        this.x = startX;
        this.y = startY;
        this.SPEED = speed;
        
        try {
            URL imgUrl = getClass().getResource(spritePath);
            if (imgUrl != null) {
                this.sprite = ImageIO.read(imgUrl);
            } else {
                System.err.println("Could not find sprite at: " + spritePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public Weapons getEquippedWeapon() {
        return loadout[activeSlot];
    }

    public void attack(int targetX, int targetY, ArrayList<Mob> enemies, ArrayList<Projectile> projectiles) {
        getEquippedWeapon().use(this, targetX, targetY, enemies, projectiles);
    }

    public void takeDamage(int amount) {
        setHP(Math.max(0, getHP() - amount));
    }
    public void dash(int dirX, int dirY) {
        if (dirX == 0 && dirY == 0) dirX = 1; // Default to dashing right if stationary
        double dashDistance = SPEED * 10;
        
        if (dirX != 0 && dirY != 0) {
            double factor = 1.0 / Math.hypot(dirX, dirY);
            this.x += dirX * factor * dashDistance;
            this.y += dirY * factor * dashDistance;
        } else {
            this.x += dirX * dashDistance;
            this.y += dirY * dashDistance;
        }
    }
    
        public void updateMovement(String execAnimation) {
            switch (execAnimation) {
                case "Idle":
                    break;
                case "Attack":
                    break;
                case "Walk":
                    break;
                case "Run":
                    break;
                case "Swing":
                    break;
                case "Fire":
                    break;
            }
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