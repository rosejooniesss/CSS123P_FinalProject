package css123p_finalproject.model;

import java.util.ArrayList;
import java.util.Iterator;


public class GameEnvironment {
    private Player pOne;
    private Player pTwo;
    private ArrayList<Mob> enemies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Projectile> enemyProjectiles;
    private int currentFloor = 1;
    private boolean gameWon = false;
    private boolean paused = false;
    private boolean titleScreen = true;
    private boolean waitingForFloorPrompt = false;

    public GameEnvironment() {
        this.pOne = new Player();
        this.pTwo = new Player();
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();
    }

    public boolean isTitleScreen() { return titleScreen; }
    public boolean isPaused() { return paused; }
    public boolean isWaitingForFloorPrompt() { return waitingForFloorPrompt; }
    public void setWaitingForFloorPrompt(boolean waiting) { this.waitingForFloorPrompt = waiting; }

    public void startGame() {
        this.titleScreen = false;
        loadFloor(1);
    }

    public void togglePause() {
        if (!titleScreen && !waitingForFloorPrompt) {
            this.paused = !this.paused;
        }
    }

    public void restartGame() {
        this.gameWon = false;
        this.paused = false;
        this.waitingForFloorPrompt = false;
        this.currentFloor = 1;
        loadFloor(1);
    }

    public void loadFloor(int floor) {
        this.currentFloor = floor;
        enemies.clear();
        projectiles.clear();
        enemyProjectiles.clear();
        player.setX(150);
        player.setY(250);

        switch (floor) {
            case 1:
                for (int i = 0; i < 5; i++) {
                    enemies.add(new Goblin(500 + (i * 40), 100 + (i * 80), 1.6, 200, 10, 5, 50));
                }
                break;
            case 2:
                for (int i = 0; i < 3; i++) {
                    enemies.add(new Skeleton(450 + (i * 80), 150 + (i * 100), 1.0, 150, 25, 5, 50));
                }
                break;
            case 3:
                for (int i = 0; i < 12; i++) {
                    enemies.add(new Goblin(400 + (i * 45), 30 + (i * 40), 1.6, 250, 20, 10, 50));
                }
                break;
        }
    }

    public void updateWorld() {
        if (titleScreen || gameWon || paused || waitingForFloorPrompt) return;

        long currentTime = System.currentTimeMillis();
        boolean floorCleared = true;

        for (Mob mob : enemies) {
            if (!mob.isAlive()) continue;
            floorCleared = false;

            // Pass enemyProjectiles list to support skeletons
            mob.updateAI(player, enemies, enemyProjectiles);

            double distance = Math.hypot(mob.getX() - player.getX(), mob.getY() - player.getY());
            if (distance <= player.getWidth()) {
                if (currentTime - mob.lastAttackTime >= 1000) {
                    player.takeDamage(1);
                    mob.lastAttackTime = currentTime;
                }
            }
        }

        if (floorCleared) {
            if (currentFloor == 1) {
                waitingForFloorPrompt = true; // Signal View to prompt user
                return;
            }
            currentFloor++;
            if (currentFloor > 3) {
                gameWon = true;
            } else {
                loadFloor(currentFloor);
            }
        }

        // Update Player Projectiles
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();
            p.update();

            if (p.getX() < -200 || p.getX() > 1200 || p.getY() < -200 || p.getY() > 1000) {
                it.remove();
                continue;
            }

            boolean hit = false;
            for (Mob mob : enemies) {
                if (mob.isAlive() && p.getX() >= mob.getX() && p.getX() <= mob.getX() + mob.getWidth()
                        && p.getY() >= mob.getY() && p.getY() <= mob.getY() + mob.getHeight()) {
                    mob.takeDamage(p.getDamage());
                    hit = true;
                    break;
                }
            }
            if (hit) it.remove();
        }

        // Update Enemy Projectiles (Collide ONLY with the Player)
        Iterator<Projectile> eIt = enemyProjectiles.iterator();
        while (eIt.hasNext()) {
            Projectile p = eIt.next();
            p.update();

            if (p.getX() < -200 || p.getX() > 1200 || p.getY() < -200 || p.getY() > 1000) {
                eIt.remove();
                continue;
            }

            if (p.getX() >= player.getX() && p.getX() <= player.getX() + player.getWidth()
                    && p.getY() >= player.getY() && p.getY() <= player.getY() + player.getHeight()) {
                player.takeDamage(p.getDamage());
                eIt.remove();
            }
        }
    }

    public Player getPlayer() { return player; }
    public ArrayList<Mob> getEnemies() { return enemies; }
    public ArrayList<Projectile> getProjectiles() { return projectiles; }
    public ArrayList<Projectile> getEnemyProjectiles() { return enemyProjectiles; }
    public int getCurrentFloor() { return currentFloor; }
    public boolean isGameWon() { return gameWon; }
}