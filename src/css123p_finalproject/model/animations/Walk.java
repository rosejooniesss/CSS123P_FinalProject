package css123p_finalproject.model.animations;

public class Walk {

    private double time;
    private double speed;
    private double targetSpeed;
    private double blendFactor;
    private double maxLegSwing;
    private double maxArmSwing;
    private double bobHeight;
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
    private boolean isMoving;
    private boolean isSprinting;
    private boolean isSneaking;
    private int stepCount;

    public Walk() {
        this.time = 0.0;
        this.speed = 5.0;
        this.targetSpeed = 5.0;
        this.blendFactor = 0.1;
        this.maxLegSwing = 0.5;
        this.maxArmSwing = 0.5;
        this.bobHeight = 2.0;
        this.swayWidth = 1.0;
        this.torsoTwist = 0.15;
        this.headBob = 0.05;
        this.kneeFlexion = 0.6;
        this.elbowFlexion = 0.4;
        this.isMoving = false;
        this.isSprinting = false;
        this.isSneaking = false;
        this.stepCount = 0;
    }

    public void update(double deltaTime) {
        this.speed = lerp(this.speed, this.targetSpeed, this.blendFactor);
        this.time += deltaTime * this.speed;
        if (this.time >= Math.PI * 4) {
            this.time -= Math.PI * 4;
            this.stepCount++;
        }
        this.calculateKinematics();
    }

    private void calculateKinematics() {
        double cycle = this.time;
        double inverseCycle = this.time + Math.PI;
        double doubleCycle = this.time * 2.0;

        this.leftHipPitch = Math.sin(cycle) * this.maxLegSwing;
        this.rightHipPitch = Math.sin(inverseCycle) * this.maxLegSwing;

        this.leftKneePitch = Math.max(0.0, Math.sin(cycle - Math.PI / 2.0)) * this.kneeFlexion;
        this.rightKneePitch = Math.max(0.0, Math.sin(inverseCycle - Math.PI / 2.0)) * this.kneeFlexion;

        this.leftAnklePitch = Math.cos(cycle) * (this.maxLegSwing / 2.0);
        this.rightAnklePitch = Math.cos(inverseCycle) * (this.maxLegSwing / 2.0);

        this.leftShoulderPitch = Math.sin(inverseCycle) * this.maxArmSwing;
        this.rightShoulderPitch = Math.sin(cycle) * this.maxArmSwing;

        this.leftElbowPitch = Math.abs(Math.sin(inverseCycle - Math.PI / 4.0)) * this.elbowFlexion;
        this.rightElbowPitch = Math.abs(Math.sin(cycle - Math.PI / 4.0)) * this.elbowFlexion;

        this.leftShoulderRoll = Math.sin(doubleCycle) * 0.05;
        this.rightShoulderRoll = -Math.sin(doubleCycle) * 0.05;

        this.leftWristPitch = Math.sin(inverseCycle) * 0.1;
        this.rightWristPitch = Math.sin(cycle) * 0.1;

        this.torsoYaw = Math.sin(cycle) * this.torsoTwist;
        this.torsoPitch = Math.sin(doubleCycle) * 0.05;
        this.torsoRoll = Math.cos(cycle) * 0.05;

        this.headPitch = Math.sin(doubleCycle - Math.PI / 4.0) * this.headBob;
        this.headYaw = -this.torsoYaw * 0.5;
        this.headRoll = -this.torsoRoll * 0.5;

        this.bodyYOffset = Math.abs(Math.sin(cycle)) * this.bobHeight;
        this.bodyXOffset = Math.sin(cycle) * this.swayWidth;
        this.bodyZOffset = Math.abs(Math.cos(cycle)) * (this.bobHeight * 0.2);
    }

    public void reset() {
        this.time = 0.0;
        this.targetSpeed = 0.0;
        this.calculateKinematics();
    }

    public void setSprinting(boolean sprinting) {
        this.isSprinting = sprinting;
        if (sprinting) {
            this.targetSpeed = 10.0;
            this.maxLegSwing = 0.9;
            this.maxArmSwing = 1.0;
            this.bobHeight = 3.5;
            this.kneeFlexion = 1.0;
        } else {
            this.targetSpeed = 5.0;
            this.maxLegSwing = 0.5;
            this.maxArmSwing = 0.5;
            this.bobHeight = 2.0;
            this.kneeFlexion = 0.6;
        }
    }

    public void setSneaking(boolean sneaking) {
        this.isSneaking = sneaking;
        if (sneaking) {
            this.targetSpeed = 2.5;
            this.maxLegSwing = 0.3;
            this.maxArmSwing = 0.2;
            this.bobHeight = 0.5;
            this.kneeFlexion = 0.8;
            this.torsoPitch = 0.4;
        } else {
            this.targetSpeed = 5.0;
            this.maxLegSwing = 0.5;
            this.maxArmSwing = 0.5;
            this.bobHeight = 2.0;
            this.kneeFlexion = 0.6;
            this.torsoPitch = 0.0;
        }
    }

    private double lerp(double start, double end, double alpha) {
        return start + alpha * (end - start);
    }

    public double getTime() {
        return this.time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getTargetSpeed() {
        return this.targetSpeed;
    }

    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public double getBlendFactor() {
        return this.blendFactor;
    }

    public void setBlendFactor(double blendFactor) {
        this.blendFactor = blendFactor;
    }

    public double getMaxLegSwing() {
        return this.maxLegSwing;
    }

    public void setMaxLegSwing(double maxLegSwing) {
        this.maxLegSwing = maxLegSwing;
    }

    public double getMaxArmSwing() {
        return this.maxArmSwing;
    }

    public void setMaxArmSwing(double maxArmSwing) {
        this.maxArmSwing = maxArmSwing;
    }

    public double getBobHeight() {
        return this.bobHeight;
    }

    public void setBobHeight(double bobHeight) {
        this.bobHeight = bobHeight;
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

    public boolean isMoving() {
        return this.isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public boolean isSprinting() {
        return this.isSprinting;
    }

    public boolean isSneaking() {
        return this.isSneaking;
    }

    public int getStepCount() {
        return this.stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}