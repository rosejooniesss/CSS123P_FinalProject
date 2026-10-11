package css123p_finalproject.model;

public abstract class Stats {
    protected int HP;
    protected int ATK;
    protected int DEF;
    protected int STAMINA;
    
    public Stats(int HP, int ATK, int DEF, int STAMINA) {
        this.HP = HP;
        this.ATK = ATK;
        this.DEF = DEF;
        this.STAMINA = STAMINA;
    }

    public abstract int getHP();
    public abstract int getATK();
    public abstract int getDEF();
    public abstract int getSTAM();

    public abstract void setHP(int HP);
    public abstract void setATK(int ATK);
    public abstract void setDEF(int DEF);
    public abstract void setSTAM(int STAMINA);
}