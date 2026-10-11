package css123p_finalproject.model.animations;

public class AttackMode {

    private double progress = 0.0;
    private double speed = 4.0;
    private double startAngle = -0.785;
    private double endAngle = 2.356;
    private boolean attacking = false;

    public AttackMode() {
    }

    public void attack() {
        this.progress = 0.0;
        this.attacking = true;
    }

    public void update(double deltaTime) {
        if (this.attacking) {
            this.progress += deltaTime * this.speed;
            if (this.progress >= 1.0) {
                this.progress = 1.0;
                this.attacking = false;
            }
        }
    }

    public double getAttackAngle() {
        if (this.attacking) {
            return this.startAngle + (this.endAngle - this.startAngle) * Math.sin(this.progress * Math.PI / 2.0);
        }
        return 0.0;
    }

    public boolean isAttacking() {
        return this.attacking;
    }
}