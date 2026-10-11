package css123p_finalproject.model.animations;

public class SwordSwing {
    private double progress = 0.0;
    private double speed = 4.0;
    private double startAngle = -0.785;
    private double endAngle = 2.356;
    private boolean swinging = false;

    public SwordSwing() {
    }

    public void swing() {
        progress = 0.0;
        swinging = true;
    }

    public void update(double deltaTime) {
        if (swinging) {
            progress += deltaTime * speed;
            if (progress >= 1.0) {
                progress = 1.0;
                swinging = false;
            }
        }
    }

    public double getAngle() {
        return startAngle + (endAngle - startAngle) * Math.sin(progress * Math.PI / 2.0);
    }

    public boolean isSwinging() {
        return swinging;
    }
}