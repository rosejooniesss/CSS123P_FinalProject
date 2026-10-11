package css123p_finalproject.model;

public class Damage {
    
    private double DmgBoost = 0.0;
    private double DmgReduction = 0.0;
    private double CurrentDmg = 0.0;
   
    public Damage(double Dmg, double DmgBoost, double DmgReduction) {
        if (Dmg == 0) {
            return; 
        }
        this.DmgBoost = Dmg * DmgBoost;
        this.DmgReduction = DmgReduction;
        this.CurrentDmg = this.DmgBoost - DmgReduction;
    }
 
    public double InflictDmg() {
        return this.CurrentDmg;
    }

    public void ReduceBattleDmg(double DmgReduction) {
        this.CurrentDmg = this.CurrentDmg - (this.CurrentDmg / DmgReduction);
    }

    
    public double ReduceStatsDmg(double DmgReduction) {
        this.CurrentDmg = this.CurrentDmg - (this.CurrentDmg - DmgReduction);
        this.DmgReduction = DmgReduction;
        return this.CurrentDmg;
    }

    public void BoostBattleDmg(double DmgBoost) {
        this.CurrentDmg = this.CurrentDmg + (this.CurrentDmg / DmgBoost); 
    }
    
    public double BoostStatsDmg(double DmgBoost) {
        this.CurrentDmg = this.CurrentDmg + (this.CurrentDmg / DmgBoost);
        this.DmgBoost = DmgBoost;
        return this.CurrentDmg; 
    }
}