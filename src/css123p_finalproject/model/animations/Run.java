package css123p_finalproject.model.animations;

public class Run {

    private double speed;
    private double currentSpeed;
    private double blendFactor;
    private double maxSwingAngle;
    private double bobHeight;
    private boolean running;
    
    private double previous_step;
    private double new_step;

    private double leanAngle;
    private double swayWidth;
    private double torsoTwist;
    private double headBob;
    private double kneeFlexion;
    private double elbowFlexion;
    
    private double leftHipPitch;
    private double leftHipRoll;
    private double leftHipYaw;
    private double leftKneePitch;
    private double leftAnklePitch;
    
    private double rightHipPitch;
    private double rightHipRoll;
    private double rightHipYaw;
    private double rightKneePitch;
    private double rightAnklePitch;
    
    private double leftShoulderPitch;
    private double leftShoulderRoll;
    private double leftShoulderYaw;
    private double leftElbowPitch;
    private double leftWristPitch;
    
    private double rightShoulderPitch;
    private double rightShoulderRoll;
    private double rightShoulderYaw;
    private double rightElbowPitch;
    private double rightWristPitch;
    
    private double torsoPitch;
    private double torsoYaw;
    private double torsoRoll;
    
    private double headPitch;
    private double headYaw;
    private double headRoll;
    
    private double bodyYOffset;
    private double bodyXOffset;
    private double bodyZOffset;
    
    private int strideCount;
    private boolean airborne;

    public Run() {
        this.speed = 10.0;
        this.currentSpeed = 0.0;
        this.blendFactor = 0.15;
        this.maxSwingAngle = 1.2;
        this.bobHeight = 4.0;
        this.running = false;
        this.previous_step = 0.0;
        this.new_step = 0.0;
        
        this.leanAngle = 0.2;
        this.swayWidth = 1.5;
        this.torsoTwist = 0.25;
        this.headBob = 0.1;
        this.kneeFlexion = 1.2;
        this.elbowFlexion = 0.8;
        this.strideCount = 0;
        this.airborne = false;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public double run_movement(double deltaTime) {
        this.previous_step = this.new_step;
        
        if (this.running) {
            this.currentSpeed = lerp(this.currentSpeed, this.speed, this.blendFactor);
        } else {
            this.currentSpeed = lerp(this.currentSpeed, 0.0, this.blendFactor);
        }

        this.new_step += deltaTime * this.currentSpeed;
        
        if (this.new_step >= Math.PI * 4) {
            this.new_step -= Math.PI * 4;
            this.previous_step -= Math.PI * 4;
            this.strideCount++;
        }
        
        this.calculateKinematics();
        
        if (this.running || this.currentSpeed > 0.1) {
            return this.new_step - this.previous_step;
        }
        return 0.0;
    }

    private void calculateKinematics() {
        double cycle = this.new_step;
        double inverseCycle = this.new_step + Math.PI;
        double doubleCycle = this.new_step * 2.0;

        this.leftHipPitch = Math.sin(cycle) * this.maxSwingAngle;
        this.rightHipPitch = Math.sin(inverseCycle) * this.maxSwingAngle;

        this.leftKneePitch = Math.max(0.0, Math.sin(cycle - Math.PI / 2.5)) * this.kneeFlexion;
        this.rightKneePitch = Math.max(0.0, Math.sin(inverseCycle - Math.PI / 2.5)) * this.kneeFlexion;

        this.leftAnklePitch = Math.cos(cycle) * (this.maxSwingAngle * 0.4);
        this.rightAnklePitch = Math.cos(inverseCycle) * (this.maxSwingAngle * 0.4);

        this.leftShoulderPitch = Math.sin(inverseCycle) * this.maxSwingAngle;
        this.rightShoulderPitch = Math.sin(cycle) * this.maxSwingAngle;

        this.leftElbowPitch = Math.abs(Math.sin(inverseCycle - Math.PI / 4.0)) * this.elbowFlexion + 0.2;
        this.rightElbowPitch = Math.abs(Math.sin(cycle - Math.PI / 4.0)) * this.elbowFlexion + 0.2;

        this.leftShoulderRoll = Math.sin(doubleCycle) * 0.1;
        this.rightShoulderRoll = -Math.sin(doubleCycle) * 0.1;

        this.leftWristPitch = Math.sin(inverseCycle) * 0.2;
        this.rightWristPitch = Math.sin(cycle) * 0.2;

        this.torsoYaw = Math.sin(cycle) * this.torsoTwist;
        this.torsoPitch = this.leanAngle + Math.sin(doubleCycle) * 0.08;
        this.torsoRoll = Math.cos(cycle) * 0.08;

        this.headPitch = Math.sin(doubleCycle - Math.PI / 3.0) * this.headBob - (this.leanAngle * 0.5);
        this.headYaw = -this.torsoYaw * 0.4;
        this.headRoll = -this.torsoRoll * 0.4;

        this.bodyYOffset = Math.abs(Math.sin(cycle)) * this.bobHeight;
        this.bodyXOffset = Math.sin(cycle) * this.swayWidth;
        this.bodyZOffset = Math.abs(Math.cos(cycle)) * (this.bobHeight * 0.5);
        
        this.airborne = this.bodyYOffset > (this.bobHeight * 0.8);
    }

    public void reset() {
        this.new_step = 0.0;
        this.previous_step = 0.0;
        this.currentSpeed = 0.0;
        this.calculateKinematics();
    }

    private double lerp(double start, double end, double alpha) {
        return start + alpha * (end - start);
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getCurrentSpeed() {
        return this.currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public double getBlendFactor() {
        return this.blendFactor;
    }

    public void setBlendFactor(double blendFactor) {
        this.blendFactor = blendFactor;
    }

    public double getMaxSwingAngle() {
        return this.maxSwingAngle;
    }

    public void setMaxSwingAngle(double maxSwingAngle) {
        this.maxSwingAngle = maxSwingAngle;
    }

    public double getBobHeight() {
        return this.bobHeight;
    }

    public void setBobHeight(double bobHeight) {
        this.bobHeight = bobHeight;
    }

    public boolean isRunning() {
        return this.running;
    }

    public double getPrevious_step() {
        return this.previous_step;
    }

    public void setPrevious_step(double previous_step) {
        this.previous_step = previous_step;
    }

    public double getNew_step() {
        return this.new_step;
    }

    public void setNew_step(double new_step) {
        this.new_step = new_step;
    }

    public double getLeanAngle() {
        return this.leanAngle;
    }

    public void setLeanAngle(double leanAngle) {
        this.leanAngle = leanAngle;
    }

    public double getSwayWidth() {
        return this.swayWidth;
    }

    public void setSwayWidth(double swayWidth) {
        this.swayWidth = swayWidth;
    }

    public double getTorsoTwist() {
        return this.torsoTwist;
    }

    public void setTorsoTwist(double torsoTwist) {
        this.torsoTwist = torsoTwist;
    }

    public double getHeadBob() {
        return this.headBob;
    }

    public void setHeadBob(double headBob) {
        this.headBob = headBob;
    }

    public double getKneeFlexion() {
        return this.kneeFlexion;
    }

    public void setKneeFlexion(double kneeFlexion) {
        this.kneeFlexion = kneeFlexion;
    }

    public double getElbowFlexion() {
        return this.elbowFlexion;
    }

    public void setElbowFlexion(double elbowFlexion) {
        this.elbowFlexion = elbowFlexion;
    }

    public double getLeftHipPitch() {
        return this.leftHipPitch;
    }

    public double getLeftHipRoll() {
        return this.leftHipRoll;
    }

    public double getLeftHipYaw() {
        return this.leftHipYaw;
    }

    public double getLeftKneePitch() {
        return this.leftKneePitch;
    }

    public double getLeftAnklePitch() {
        return this.leftAnklePitch;
    }

    public double getRightHipPitch() {
        return this.rightHipPitch;
    }

    public double getRightHipRoll() {
        return this.rightHipRoll;
    }

    public double getRightHipYaw() {
        return this.rightHipYaw;
    }

    public double getRightKneePitch() {
        return this.rightKneePitch;
    }

    public double getRightAnklePitch() {
        return this.rightAnklePitch;
    }

    public double getLeftShoulderPitch() {
        return this.leftShoulderPitch;
    }

    public double getLeftShoulderRoll() {
        return this.leftShoulderRoll;
    }

    public double getLeftShoulderYaw() {
        return this.leftShoulderYaw;
    }

    public double getLeftElbowPitch() {
        return this.leftElbowPitch;
    }

    public double getLeftWristPitch() {
        return this.leftWristPitch;
    }

    public double getRightShoulderPitch() {
        return this.rightShoulderPitch;
    }

    public double getRightShoulderRoll() {
        return this.rightShoulderRoll;
    }

    public double getRightShoulderYaw() {
        return this.rightShoulderYaw;
    }

    public double getRightElbowPitch() {
        return this.rightElbowPitch;
    }

    public double getRightWristPitch() {
        return this.rightWristPitch;
    }

    public double getTorsoPitch() {
        return this.torsoPitch;
    }

    public double getTorsoYaw() {
        return this.torsoYaw;
    }

    public double getTorsoRoll() {
        return this.torsoRoll;
    }

    public double getHeadPitch() {
        return this.headPitch;
    }

    public double getHeadYaw() {
        return this.headYaw;
    }

    public double getHeadRoll() {
        return this.headRoll;
    }

    public double getBodyYOffset() {
        return this.bodyYOffset;
    }

    public double getBodyXOffset() {
        return this.bodyXOffset;
    }

    public double getBodyZOffset() {
        return this.bodyZOffset;
    }

    public int getStrideCount() {
        return this.strideCount;
    }

    public void setStrideCount(int strideCount) {
        this.strideCount = strideCount;
    }

    public boolean isAirborne() {
        return this.airborne;
    }

    public void setAirborne(boolean airborne) {
        this.airborne = airborne;
    }

    public double getLeftLegAngle() {
        return this.leftHipPitch;
    }

    public double getRightLegAngle() {
        return this.rightHipPitch;
    }

    public double getLeftArmAngle() {
        return this.leftShoulderPitch;
    }

    public double getRightArmAngle() {
        return this.rightShoulderPitch;
    }
}