package css123p_finalproject.model.animations;

public class IdleMode {

    private boolean idling;
    private double time;
    private double breathingRate;
    private double breathingDepth;
    private double swaySpeed;
    private double swayAmount;
    private double blendFactor;
    
    private double fidgetTimer;
    private boolean isFidgeting;
    private double weightShiftTimer;
    private double weightShiftAmount;

    private double headPitch;
    private double headYaw;
    private double headRoll;
    private double headY;

    private double torsoPitch;
    private double torsoYaw;
    private double torsoRoll;
    private double torsoY;

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

    public IdleMode() {
        this.idling = true;
        this.time = 0.0;
        this.breathingRate = 2.0;
        this.breathingDepth = 1.0;
        this.swaySpeed = 0.5;
        this.swayAmount = 0.2;
        this.blendFactor = 0.1;
        this.fidgetTimer = 0.0;
        this.isFidgeting = false;
        this.weightShiftTimer = 0.0;
        this.weightShiftAmount = 0.0;
    }

    public void setIdle(boolean idling) {
        this.idling = idling;
    }

    public double[] idleMovement(double deltaTime) {
        if (!this.idling) {
            this.resetKinematics();
            return new double[] { 0.0, 0.0, 0.0, 0.0 };
        }

        this.time += deltaTime;
        if (this.time >= Math.PI * 100) {
            this.time -= Math.PI * 100;
        }

        this.fidgetTimer += deltaTime;
        if (this.fidgetTimer > 5.0) {
            this.isFidgeting = Math.random() > 0.8;
            this.fidgetTimer = 0.0;
        }

        this.calculateKinematics();

        return new double[] { 
            this.headY, 
            this.torsoY, 
            this.leftShoulderPitch, 
            this.rightShoulderPitch 
        };
    }

    private void calculateKinematics() {
        double breathCycle = this.time * this.breathingRate;
        double swayCycle = this.time * this.swaySpeed;

        this.torsoY = Math.sin(breathCycle) * this.breathingDepth;
        this.headY = Math.sin(breathCycle * 1.5) * (this.breathingDepth * 1.5);
        
        this.leftShoulderPitch = Math.cos(breathCycle) * 1.2 * (this.breathingDepth * 0.5);
        this.rightShoulderPitch = Math.cos(breathCycle + Math.PI) * 1.2 * (this.breathingDepth * 0.5);

        this.torsoPitch = Math.sin(swayCycle) * this.swayAmount;
        this.torsoYaw = Math.cos(swayCycle * 0.5) * (this.swayAmount * 0.5);
        this.torsoRoll = Math.sin(swayCycle * 0.8) * (this.swayAmount * 0.3);

        this.headPitch = -this.torsoPitch * 0.5;
        this.headYaw = Math.sin(swayCycle * 0.3) * 0.1;
        this.headRoll = -this.torsoRoll * 0.5;

        this.leftShoulderRoll = Math.abs(Math.sin(breathCycle)) * 0.05;
        this.rightShoulderRoll = -Math.abs(Math.sin(breathCycle)) * 0.05;

        this.leftElbowPitch = Math.abs(Math.cos(breathCycle)) * 0.1;
        this.rightElbowPitch = Math.abs(Math.cos(breathCycle)) * 0.1;

        if (this.isFidgeting) {
            double fidgetOffset = Math.sin(this.time * 15.0) * 0.05;
            this.headYaw += fidgetOffset;
            this.headPitch += fidgetOffset * 0.5;
            this.leftWristPitch = Math.sin(this.time * 10.0) * 0.2;
            this.rightWristPitch = Math.cos(this.time * 10.0) * 0.2;
        } else {
            this.leftWristPitch = lerp(this.leftWristPitch, 0.0, this.blendFactor);
            this.rightWristPitch = lerp(this.rightWristPitch, 0.0, this.blendFactor);
        }

        this.weightShiftAmount = Math.sin(this.time * 0.2) * 0.1;
        this.leftHipPitch = this.weightShiftAmount;
        this.rightHipPitch = -this.weightShiftAmount;
        this.leftKneePitch = Math.max(0, this.weightShiftAmount * 0.5);
        this.rightKneePitch = Math.max(0, -this.weightShiftAmount * 0.5);
    }

    private void resetKinematics() {
        this.headY = 0.0;
        this.torsoY = 0.0;
        this.leftShoulderPitch = 0.0;
        this.rightShoulderPitch = 0.0;
        this.headPitch = 0.0;
        this.headYaw = 0.0;
        this.headRoll = 0.0;
        this.torsoPitch = 0.0;
        this.torsoYaw = 0.0;
        this.torsoRoll = 0.0;
        this.leftShoulderRoll = 0.0;
        this.rightShoulderRoll = 0.0;
        this.leftElbowPitch = 0.0;
        this.rightElbowPitch = 0.0;
        this.leftWristPitch = 0.0;
        this.rightWristPitch = 0.0;
        this.leftHipPitch = 0.0;
        this.rightHipPitch = 0.0;
        this.leftKneePitch = 0.0;
        this.rightKneePitch = 0.0;
    }

    private double lerp(double start, double end, double alpha) {
        return start + alpha * (end - start);
    }

    public boolean isIdling() {
        return this.idling;
    }

    public double getTime() {
        return this.time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getBreathingRate() {
        return this.breathingRate;
    }

    public void setBreathingRate(double breathingRate) {
        this.breathingRate = breathingRate;
    }

    public double getBreathingDepth() {
        return this.breathingDepth;
    }

    public void setBreathingDepth(double breathingDepth) {
        this.breathingDepth = breathingDepth;
    }

    public double getSwaySpeed() {
        return this.swaySpeed;
    }

    public void setSwaySpeed(double swaySpeed) {
        this.swaySpeed = swaySpeed;
    }

    public double getSwayAmount() {
        return this.swayAmount;
    }

    public void setSwayAmount(double swayAmount) {
        this.swayAmount = swayAmount;
    }

    public double getBlendFactor() {
        return this.blendFactor;
    }

    public void setBlendFactor(double blendFactor) {
        this.blendFactor = blendFactor;
    }

    public double getFidgetTimer() {
        return this.fidgetTimer;
    }

    public void setFidgetTimer(double fidgetTimer) {
        this.fidgetTimer = fidgetTimer;
    }

    public boolean isFidgeting() {
        return this.isFidgeting;
    }

    public void setFidgeting(boolean fidgeting) {
        this.isFidgeting = fidgeting;
    }

    public double getWeightShiftTimer() {
        return this.weightShiftTimer;
    }

    public void setWeightShiftTimer(double weightShiftTimer) {
        this.weightShiftTimer = weightShiftTimer;
    }

    public double getWeightShiftAmount() {
        return this.weightShiftAmount;
    }

    public void setWeightShiftAmount(double weightShiftAmount) {
        this.weightShiftAmount = weightShiftAmount;
    }

    public double getHeadPitch() {
        return this.headPitch;
    }

    public void setHeadPitch(double headPitch) {
        this.headPitch = headPitch;
    }

    public double getHeadYaw() {
        return this.headYaw;
    }

    public void setHeadYaw(double headYaw) {
        this.headYaw = headYaw;
    }

    public double getHeadRoll() {
        return this.headRoll;
    }

    public void setHeadRoll(double headRoll) {
        this.headRoll = headRoll;
    }

    public double getHeadY() {
        return this.headY;
    }

    public void setHeadY(double headY) {
        this.headY = headY;
    }

    public double getTorsoPitch() {
        return this.torsoPitch;
    }

    public void setTorsoPitch(double torsoPitch) {
        this.torsoPitch = torsoPitch;
    }

    public double getTorsoYaw() {
        return this.torsoYaw;
    }

    public void setTorsoYaw(double torsoYaw) {
        this.torsoYaw = torsoYaw;
    }

    public double getTorsoRoll() {
        return this.torsoRoll;
    }

    public void setTorsoRoll(double torsoRoll) {
        this.torsoRoll = torsoRoll;
    }

    public double getTorsoY() {
        return this.torsoY;
    }

    public void setTorsoY(double torsoY) {
        this.torsoY = torsoY;
    }

    public double getLeftShoulderPitch() {
        return this.leftShoulderPitch;
    }

    public void setLeftShoulderPitch(double leftShoulderPitch) {
        this.leftShoulderPitch = leftShoulderPitch;
    }

    public double getLeftShoulderRoll() {
        return this.leftShoulderRoll;
    }

    public void setLeftShoulderRoll(double leftShoulderRoll) {
        this.leftShoulderRoll = leftShoulderRoll;
    }

    public double getLeftShoulderYaw() {
        return this.leftShoulderYaw;
    }

    public void setLeftShoulderYaw(double leftShoulderYaw) {
        this.leftShoulderYaw = leftShoulderYaw;
    }

    public double getLeftElbowPitch() {
        return this.leftElbowPitch;
    }

    public void setLeftElbowPitch(double leftElbowPitch) {
        this.leftElbowPitch = leftElbowPitch;
    }

    public double getLeftWristPitch() {
        return this.leftWristPitch;
    }

    public void setLeftWristPitch(double leftWristPitch) {
        this.leftWristPitch = leftWristPitch;
    }

    public double getRightShoulderPitch() {
        return this.rightShoulderPitch;
    }

    public void setRightShoulderPitch(double rightShoulderPitch) {
        this.rightShoulderPitch = rightShoulderPitch;
    }

    public double getRightShoulderRoll() {
        return this.rightShoulderRoll;
    }

    public void setRightShoulderRoll(double rightShoulderRoll) {
        this.rightShoulderRoll = rightShoulderRoll;
    }

    public double getRightShoulderYaw() {
        return this.rightShoulderYaw;
    }

    public void setRightShoulderYaw(double rightShoulderYaw) {
        this.rightShoulderYaw = rightShoulderYaw;
    }

    public double getRightElbowPitch() {
        return this.rightElbowPitch;
    }

    public void setRightElbowPitch(double rightElbowPitch) {
        this.rightElbowPitch = rightElbowPitch;
    }

    public double getRightWristPitch() {
        return this.rightWristPitch;
    }

    public void setRightWristPitch(double rightWristPitch) {
        this.rightWristPitch = rightWristPitch;
    }

    public double getLeftHipPitch() {
        return this.leftHipPitch;
    }

    public void setLeftHipPitch(double leftHipPitch) {
        this.leftHipPitch = leftHipPitch;
    }

    public double getLeftHipRoll() {
        return this.leftHipRoll;
    }

    public void setLeftHipRoll(double leftHipRoll) {
        this.leftHipRoll = leftHipRoll;
    }

    public double getLeftHipYaw() {
        return this.leftHipYaw;
    }

    public void setLeftHipYaw(double leftHipYaw) {
        this.leftHipYaw = leftHipYaw;
    }

    public double getLeftKneePitch() {
        return this.leftKneePitch;
    }

    public void setLeftKneePitch(double leftKneePitch) {
        this.leftKneePitch = leftKneePitch;
    }

    public double getLeftAnklePitch() {
        return this.leftAnklePitch;
    }

    public void setLeftAnklePitch(double leftAnklePitch) {
        this.leftAnklePitch = leftAnklePitch;
    }

    public double getRightHipPitch() {
        return this.rightHipPitch;
    }

    public void setRightHipPitch(double rightHipPitch) {
        this.rightHipPitch = rightHipPitch;
    }

    public double getRightHipRoll() {
        return this.rightHipRoll;
    }

    public void setRightHipRoll(double rightHipRoll) {
        this.rightHipRoll = rightHipRoll;
    }

    public double getRightHipYaw() {
        return this.rightHipYaw;
    }

    public void setRightHipYaw(double rightHipYaw) {
        this.rightHipYaw = rightHipYaw;
    }

    public double getRightKneePitch() {
        return this.rightKneePitch;
    }

    public void setRightKneePitch(double rightKneePitch) {
        this.rightKneePitch = rightKneePitch;
    }

    public double getRightAnklePitch() {
        return this.rightAnklePitch;
    }

    public void setRightAnklePitch(double rightAnklePitch) {
        this.rightAnklePitch = rightAnklePitch;
    }
}