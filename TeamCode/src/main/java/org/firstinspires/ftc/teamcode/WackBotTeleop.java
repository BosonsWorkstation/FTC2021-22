package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "WackBot Teleop", group = "Linear Opmode")
public class WackBotTeleop extends LinearOpMode {


    private WackBot driveTrain;
    private static final WackBot.DirectionEnum direction = WackBot.DirectionEnum.SOUTH;
//   private static final FrenzyDriveTrain.DirectionEnum direction = FrenzyDriveTrain.DirectionEnum.WEST;

    @Override
    public void runOpMode() throws InterruptedException {


        this.driveTrain = new WackBot(this.hardwareMap, this.telemetry, direction);

//        this.driveTrain.initializeGyro(hardwareMap, telemetry);
//        this.driveTrain.initializeMotors(hardwareMap, telemetry);

        this.driveTrain.initializeDriveMotors(hardwareMap);
        this.driveTrain.getHeading();
        this.driveTrain.reset_angle();

        boolean armServoOpen = false;
        boolean dpadOn = false;
        driveTrain.runArmServo(false);

        waitForStart();
        armThread(driveTrain);


        while (opModeIsActive()) {

            double crabValue = 0.0;
            double moveValue = 0.0;
            double turnValue = 0.0;
            double maxPower;

            if (gamepad1.left_bumper) {
                maxPower = 0.4;

                crabValue = gamepad1.left_stick_x / 4;
                moveValue = gamepad1.left_stick_y / 4;
                turnValue = gamepad1.right_stick_x / 4;

            } else {
                maxPower = 0.8;

//                crabValue = -gamepad1.left_stick_x * 1.5;
//                moveValue = -gamepad1.left_stick_y * 1.5;
                crabValue = gamepad1.left_stick_x;
                moveValue = gamepad1.left_stick_y;
                turnValue = gamepad1.right_stick_x;

            }
            if(crabValue > maxPower){
                crabValue = maxPower;
            }
            if(moveValue > maxPower){
                moveValue = maxPower;
            }
            if(turnValue > maxPower){
                turnValue = maxPower;
            }

            this.driveTrain.drive(crabValue, moveValue, turnValue, maxPower);
            idle();


            double simplePower = 0.3;
            double power = 0.3;
            if (gamepad1.dpad_up) { //Forward
//                this.driveTrain.simpleF(simplePower);
            }
            if (gamepad1.dpad_left) { //Left
                this.driveTrain.simpleL(simplePower);
            }
            if (gamepad1.dpad_down) { //Back
                this.driveTrain.simpleB(simplePower);
            }
            if (gamepad1.dpad_right) { //Right
                this.driveTrain.simpleR(simplePower);
            }


            if (gamepad2.left_trigger == 0 && gamepad2.right_trigger == 0) {
                this.driveTrain.linearStop();
            }

            if (gamepad2.right_trigger > 0) {
                power = gamepad2.right_trigger;
//
//                if (driveTrain.linearColor.blue() > 1000) {
//                    driveTrain.linearStop();
//                } else {
//                    if (power > 0.4) {
//                        power = 0.4;
//                    }
                    driveTrain.linear(power / 4);
//                }
            }

            if (gamepad2.left_trigger > 0) {
                power = -gamepad2.left_trigger;
                if (power > 0.4) {
                    power = 0.4;
                }
                this.driveTrain.linear(power / 4);
            }

            if (gamepad2.dpad_up) {
                this.driveTrain.armUp();
            }
            if (gamepad2.dpad_down) {
                this.driveTrain.armDown();
            }
            if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                this.driveTrain.armStop();
            }

            if (gamepad2.b) {
                this.driveTrain.runFly();
            } else {
                this.driveTrain.flyStop();
            }

            if (gamepad2.dpad_up) {
                driveTrain.armUp();
            }
            if (gamepad2.dpad_down) {
                driveTrain.armDown();
            }


            if (gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_down || gamepad1.dpad_right) {
                dpadOn = true;
            } else {
                dpadOn = false;
            }


            //Linear directions in case you want to do straight lines.
//            if(gamepad1.dpad_right){
//                gamepad1.left_stick_x = (float) 0.5;
//            }
//            else if(gamepad1.dpad_left){
//                gamepad1.left_stick_x = (float) -0.5;
//            }
//            if(gamepad1.dpad_up){
//                gamepad1.left_stick_y = (float) -0.5;
//            }
//            else if(gamepad1.dpad_down){
//                gamepad1.left_stick_y = (float) 0.5;
//            }

            if (Math.abs(crabValue) < 0.1 && Math.abs(moveValue) < 0.1 && Math.abs(turnValue) < 0.1) { //TODO add dpadOn stuff
                this.driveTrain.stopNow();
            }

            if (gamepad1.a) {
                this.driveTrain.reset_angle();
            }

            if (gamepad1.b) {
                driveTrain.runArmServo(true);
                sleep(20);
                driveTrain.runArmServo(false);
            }


        }

    }
    public void armThread(final WackBot driveTrain) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean armServoOpen = false;
                while (opModeIsActive()) {
                    if (gamepad1.right_bumper || gamepad2.right_bumper) {
                        driveTrain.runArmServo(armServoOpen);
                        sleep(250);
                        if (armServoOpen) {
                            armServoOpen = false;
                        } else {
                            armServoOpen = true;
                        }
                    } else {
                        sleep(20);
                    }
                }
            }
        });
        t1.start();
    }
}


