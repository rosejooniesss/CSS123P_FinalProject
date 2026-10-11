package css123p_finalproject.model;

import css123p_finalproject.model.Stats;

interface EnemyAttack {
    int execute(Player player);
}

interface EnemyDefense {
    int execute(Player player, Enemy enemy);
}

class GoblinSmash implements EnemyAttack {
    @Override
    public int execute(Player player) {
        return 10;
    }
}

class GoblinDodge implements EnemyDefense {
    @Override
    public int execute(Player player, Enemy enemy) {
        // Dummy return to force compilation
        return 5;
    }
}

public class Enemy extends Stats {

    public String type;

    public Enemy(String type, int HP, int ATK, int DEF, int EXP) {
        super(HP, ATK, DEF, EXP); 
        this.type = type;
    }
    
    // Fixed: Added all missing required abstract methods from Stats
    @Override
    public int getHP() { return this.HP; }
    
    @Override
    public void setHP(int hp) { this.HP = hp; }
    
    @Override
    public int getDEF() { return this.DEF; }

    @Override
    public void setDEF(int defense) { this.DEF = defense; }

    @Override
    public int getATK() { return this.ATK; }

    @Override
    public void setATK(int atk) { this.ATK = atk; }
    
    @Override
    public int getSTAM() { return this.STAMINA; }

    @Override
    public void setSTAM(int stamina) { this.STAMINA = stamina; }

    public static void main(String[] args) {
        Enemy goblin = new Enemy("Goblin", 200, 15, 35, 300);
        Enemy skeleton = new Enemy("Skeleton", 100, 35, 15, 150);
        Enemy slime = new Enemy("Slime", 50, 10, 5, 100);
    }
}