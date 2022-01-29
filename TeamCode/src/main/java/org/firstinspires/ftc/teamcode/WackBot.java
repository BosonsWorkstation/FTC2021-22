package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class WackBot {

    private final Telemetry telemetry;

    public WackBot(HardwareMap hardwareMap, Telemetry telemetry, org.firstinspires.ftc.teamcode.WackBot.DirectionEnum direction) {
        this.telemetry = telemetry;
        this.correction_factor = direction.getCorrection();
    }

    private double correction_factor;
    float rotate_angle = 0;
    double reset_angle = 0;

    protected DcMotor front_left_wheel = null;
    protected DcMotor back_left_wheel = null;
    protected DcMotor back_right_wheel = null;
    protected DcMotor front_right_wheel = null;

    protected DcMotor arm = null;
    protected DcMotor fly = null;

    protected Servo armServoL = null;
//    protected Servo armServoR = null;

    private static BNO055IMU imu;


    public void initializeDriveMotors(HardwareMap hardwareMap) {
        front_left_wheel = hardwareMap.dcMotor.get("front_left_wheel");
        back_left_wheel = hardwareMap.dcMotor.get("back_left_wheel");
        back_right_wheel = hardwareMap.dcMotor.get("back_right_wheel");
        front_right_wheel = hardwareMap.dcMotor.get("front_right_wheel");

        arm = hardwareMap.dcMotor.get("Arm");

        fly = hardwareMap.dcMotor.get("fly");

        front_left_wheel.setDirection(DcMotor.Direction.FORWARD);
        back_left_wheel.setDirection(DcMotor.Direction.FORWARD);
        front_right_wheel.setDirection(DcMotor.Direction.REVERSE);
        back_right_wheel.setDirection(DcMotor.Direction.REVERSE);

        arm.setDirection(DcMotor.Direction.FORWARD);
        fly.setDirection(DcMotor.Direction.REVERSE);

        front_left_wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left_wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right_wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right_wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        front_left_wheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_left_wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_right_wheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right_wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_left_wheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left_wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_right_wheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right_wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fly.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fly.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        linearL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        linearL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        linearR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        linearR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu.initialize(parameters);

        this.initializeSensors(hardwareMap);

        this.initializeServos(hardwareMap);


    }

    public void initializeSensors(HardwareMap hardwareMap) {
//        this.linearColor = hardwareMap.colorSensor.get("Linear_Color");
    }

    public void initializeServos(HardwareMap hardwareMap) {
        this.armServoL = hardwareMap.servo.get("Arm_Servo");
    }







    public enum DirectionEnum{
        NORTH(90), SOUTH(-90), EAST(180), WEST(0);
        private double correction;
        DirectionEnum(double correction) {
            this.correction = correction;
        }
        public double getCorrection() {
            return correction;
        }
    }

    public void runArmServo(boolean armServoOpen){
        if(armServoOpen){
            armServoL.setPosition(0.37);
            //ADIN ARM
//            armServoR.setPosition(0.10);
            //LUKE ARM
//            armServoR.setPosition(0.5);

            return;
        }
        else{
            armServoL.setPosition(0.7);
            //ADIN ARM
//            armServoR.setPosition(0.00);
            //LUKE ARM
//            armServoR.setPosition(0.75);
            return;
        }
    }

    public void linear(double power){
//        linearR.setPower(power);
//        linearL.setPower(power);
    }

    public void linearStop(){
//        linearL.setPower(0);
//        linearR.setPower(0);
    }


    public void armUp() {
        arm.setPower(-0.1);
    }

    public void armDown() {
        arm.setPower(0.1);
    }

    public void armStop() {
        arm.setPower(0);
    }

    public void runFly(){
        fly.setPower(0.5);
    }

    public void flyStop(){
        fly.setPower(0.0);
    }

    public void stopNow(){
        front_left_wheel.setPower(0);
        back_left_wheel.setPower(0);
        back_right_wheel.setPower(0);
        front_right_wheel.setPower(0);
    }



    public void reset_angle(){
        this.reset_angle = this.getHeading() + this.reset_angle;
    }



    public void drive(double crabValue, double moveValue, double turnValue, double maxPower) {
//        double Protate = gamepad1.right_stick_x / 4;
//        double stick_x = gamepad1.left_stick_x * Math.sqrt(Math.pow(1 - Math.abs(Protate), 2) / 2); //Accounts for Protate when limiting magnitude to be less than 1
//        double stick_y = gamepad1.left_stick_y * Math.sqrt(Math.pow(1 - Math.abs(Protate), 2) / 2);
        double Protate = turnValue;
        double stick_x = crabValue * Math.sqrt(Math.pow(1-Math.abs(Protate), 2)/2); //Accounts for Protate when limiting magnitude to be less than 1
        double stick_y = moveValue * Math.sqrt(Math.pow(1-Math.abs(Protate), 2)/2);
        double theta = 0;
        double Px = 0;
        double Py = 0;

        double gyroAngle = getHeading() * Math.PI / 180; //Converts gyroAngle into radians
        if (gyroAngle <= 0) {
            gyroAngle = gyroAngle + (Math.PI / 2);
        } else if (0 < gyroAngle && gyroAngle < Math.PI / 2) {
            gyroAngle = gyroAngle + (Math.PI / 2);
        } else if (Math.PI / 2 <= gyroAngle) {
            gyroAngle = gyroAngle - (3 * Math.PI / 2);
        }
        gyroAngle = -1 * gyroAngle;


        theta = Math.atan2(stick_y, stick_x) - gyroAngle - (Math.PI / 2);
        Px = Math.sqrt(Math.pow(stick_x, 2) + Math.pow(stick_y, 2)) * (Math.sin(theta + Math.PI / 4));
        Py = Math.sqrt(Math.pow(stick_x, 2) + Math.pow(stick_y, 2)) * (Math.sin(theta - Math.PI / 4));


        front_left_wheel.setPower(Py - Protate);
        back_left_wheel.setPower(Px - Protate);
        back_right_wheel.setPower(Py + Protate);
        front_right_wheel.setPower(Px + Protate);


//
//        telemetry.addData("crab val", crabValue);
//        telemetry.addData("move val", moveValue);
//        telemetry.addData("turn val", turnValue);
//        telemetry.addData("Blue Val", linearColor.blue());
        telemetry.addData("Magnitude", Math.sqrt(Math.pow(stick_x, 2) + Math.pow(stick_y, 2)));
        telemetry.addData("Front Left", Py - Protate);
        telemetry.addData("Back Left", Px - Protate);
        telemetry.addData("Back Right", Py + Protate);
        telemetry.addData("Front Right", Px + Protate);
        telemetry.addData("Gyro Angle", imu.getAngularOrientation());

        telemetry.update();

    }

    //    public void simpleF(double simplePower){
//        front_left_wheel.setPower(-simplePower);
//        back_left_wheel.setPower(-simplePower);
//        back_right_wheel.setPower(-simplePower);
//        front_right_wheel.setPower(-simplePower);
//    }
    public void simpleL(double simplePower){
        front_left_wheel.setPower(simplePower);
        back_left_wheel.setPower(-simplePower);
        back_right_wheel.setPower(simplePower);
        front_right_wheel.setPower(-simplePower);
    }
    public void simpleB(double simplePower){
        front_left_wheel.setPower(simplePower);
        back_left_wheel.setPower(simplePower);
        back_right_wheel.setPower(simplePower);
        front_right_wheel.setPower(simplePower);
    }
    public void simpleR(double simplePower){
        front_left_wheel.setPower(-simplePower);
        back_left_wheel.setPower(simplePower);
        back_right_wheel.setPower(-simplePower);
        front_right_wheel.setPower(simplePower);
    }
    public void simpleRotate(double simplePower){
        front_left_wheel.setPower(-simplePower);
        back_left_wheel.setPower(-simplePower);
        back_right_wheel.setPower(simplePower);
        front_right_wheel.setPower(simplePower);
    }

    public double getHeading(){
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double heading = angles.firstAngle;
        if(heading < -180) {
            heading = heading + 360;
        }
        else if(heading > 180){
            heading = heading - 360;
        }
        heading = heading - reset_angle;
        return heading + correction_factor;
    }




    //Autonomous Code
    public void rampDown(double distance, double rampDownPercent){
        if (front_right_wheel.getPower() > 0.2 && Math.abs(distance) > 400) {
            if (Math.abs(front_right_wheel.getTargetPosition() - front_right_wheel.getCurrentPosition()) < 400) {
                front_right_wheel.setPower(front_right_wheel.getPower() * rampDownPercent);
                front_left_wheel.setPower(front_left_wheel.getPower() * rampDownPercent);
                back_left_wheel.setPower(back_left_wheel.getPower() * rampDownPercent);
                back_right_wheel.setPower(back_right_wheel.getPower() * rampDownPercent);
            }
        }
    }

    public void autoMove(int distance, double power, boolean rampDown) {

        int frontLeftPosition = front_left_wheel.getCurrentPosition();
        int frontRightPosition= front_right_wheel.getCurrentPosition();
        int backLeftPosition =  back_left_wheel.getCurrentPosition();
        int backRightPosition  = back_right_wheel.getCurrentPosition();

        double rampDownPercent = 0.8;

        front_left_wheel.setTargetPosition(frontLeftPosition - distance);
        front_right_wheel.setTargetPosition(frontRightPosition + distance);
        back_left_wheel.setTargetPosition(backLeftPosition + distance);
        back_right_wheel.setTargetPosition(backRightPosition - distance);

        front_left_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_right_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_left_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_right_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        front_left_wheel.setPower(power);
        front_right_wheel.setPower(power);
        back_left_wheel.setPower(power);
        back_right_wheel.setPower(power);

        while(front_left_wheel.isBusy() && front_right_wheel.isBusy() && back_left_wheel.isBusy() && back_right_wheel.isBusy()){
            sleep(5);
            if (rampDown) {
                rampDown(distance, rampDownPercent);
            }
        }
        stopNow();
    }

    public void autoCrab(int distance, double power, boolean rampDown) {

        int frontLeftPosition = front_left_wheel.getCurrentPosition();
        int frontRightPosition= front_right_wheel.getCurrentPosition();
        int backLeftPosition =  back_left_wheel.getCurrentPosition();
        int backRightPosition  = back_right_wheel.getCurrentPosition();

        double rampDownPercent = 0.8;

        front_left_wheel.setTargetPosition(frontLeftPosition + distance);
        front_right_wheel.setTargetPosition(frontRightPosition + distance);
        back_left_wheel.setTargetPosition(backLeftPosition + distance);
        back_right_wheel.setTargetPosition(backRightPosition + distance);

        front_left_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_right_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_left_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_right_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        front_left_wheel.setPower(power);
        front_right_wheel.setPower(power);
        back_left_wheel.setPower(power);
        back_right_wheel.setPower(power);

        while(front_left_wheel.isBusy() && front_right_wheel.isBusy() && back_left_wheel.isBusy() && back_right_wheel.isBusy()){
            sleep(5);
            if (rampDown) {
                rampDown(distance, rampDownPercent);
            }
        }
        stopNow();
    }

    public void autoRotate(int distance, double power, boolean rampDown) {

        int frontLeftPosition = front_left_wheel.getCurrentPosition();
        int frontRightPosition= front_right_wheel.getCurrentPosition();
        int backLeftPosition =  back_left_wheel.getCurrentPosition();
        int backRightPosition  = back_right_wheel.getCurrentPosition();

        double rampDownPercent = 0.8;

        front_left_wheel.setTargetPosition(frontLeftPosition + distance);
        front_right_wheel.setTargetPosition(frontRightPosition + distance);
        back_left_wheel.setTargetPosition(backLeftPosition - distance);
        back_right_wheel.setTargetPosition(backRightPosition - distance);

        front_left_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_right_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_left_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_right_wheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        front_left_wheel.setPower(power);
        front_right_wheel.setPower(power);
        back_left_wheel.setPower(power);
        back_right_wheel.setPower(power);

        while(front_left_wheel.isBusy() && front_right_wheel.isBusy() && back_left_wheel.isBusy() && back_right_wheel.isBusy()){
            sleep(5);
            if (rampDown) {
                rampDown(distance, rampDownPercent);
            }
        }
        stopNow();
    }


    private void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}