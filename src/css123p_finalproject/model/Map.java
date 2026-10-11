package css123p_finalproject.model;

import java.awt.image.BufferedImage;

interface LevelFloor {
    void EnemyList(int count);
    void ChestList(int count);
}

class LevelOne implements LevelFloor {
    int EnemyCount;
    private Enemy[] Enemies;
    // Assuming Chest class exists elsewhere. If it throws an error, change Chest to Object.
    private Object[] Chests; 
    
    public void EnemyList(int count) {
        // Fixed: Changed this.Enemy to this.Enemies
        this.Enemies = new Enemy[count];
        this.EnemyCount = this.Enemies.length;
    }

    public void ChestList(int count) {
        this.Chests = new Object[count];
    }

    public BufferedImage GetMap() {
        // Fixed: Must return null instead of a class name
        return null; 
    }
}

class LevelTwo implements LevelFloor {
    int EnemyCount;
    private Enemy[] Enemies;
    private Object[] Chests;
    
    public void EnemyList(int count) {
        this.Enemies = new Enemy[count];
        this.EnemyCount = this.Enemies.length;
    }

    public void ChestList(int count) {
        this.Chests = new Object[count];
    }

    public BufferedImage GetMap() {
        return null; 
    }
}

// Fixed: LevelThree must implement LevelFloor, not LevelThree
class LevelThree implements LevelFloor {
    int EnemyCount;
    private Enemy[] Enemies;
    private Object[] Chests;
    
    public void EnemyList(int count) {
        this.Enemies = new Enemy[count];
        this.EnemyCount = this.Enemies.length;
    }

    public void ChestList(int count) {
        this.Chests = new Object[count];
    }

    public BufferedImage GetMap() {
        return null; 
    }
}

public class Map {
    public Map() {}
}