package css123p_finalproject.model.animations;

public class FireGun {

    private String gunPath;
    private double fireProgress = 0.0;
    private double reloadProgress = 0.0;
    private boolean firing = false;
    private boolean reloading = false;

    public FireGun(String gunPath) {
        this.gunPath = gunPath;
    }

    public void fire() {
        this.fireProgress = 0.0;
        this.firing = true;
    }

    public void reload() {
        this.reloadProgress = 0.0;
        this.reloading = true;
    }

    public void update(double deltaTime) {
        if (this.firing) {
            this.fireProgress += deltaTime * 15.0;
            if (this.fireProgress >= 1.0) {
                this.fireProgress = 1.0;
                this.firing = false;
            }
        }
        if (this.reloading) {
            this.reloadProgress += deltaTime * 2.0;
            if (this.reloadProgress >= 1.0) {
                this.reloadProgress = 1.0;
                this.reloading = false;
            }
        }
    }

    public double getBarrelMovement() {
        if (this.firing) {
            return Math.pow(1.0 - this.fireProgress, 4) * -12.0;
        }
        return 0.0;
    }

    public double getMagazineReload() {
        if (this.reloading) {
            return Math.sin(this.reloadProgress * Math.PI) * 20.0;
        }
        return 0.0;
    }
}