package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Frenzy Teleop", group = "Linear Opmode")
public class FrenzyTeleop extends LinearOpMode {


    private FrenzyDriveTrain driveTrain;
    private static final FrenzyDriveTrain.DirectionEnum direction = FrenzyDriveTrain.DirectionEnum.SOUTH;
//   private static final FrenzyDriveTrain.DirectionEnum direction = FrenzyDriveTrain.DirectionEnum.WEST;

    @Override
    public void runOpMode() throws InterruptedException {


        this.driveTrain = new FrenzyDriveTrain(this.hardwareMap, this.telemetry, direction);

//        this.driveTrain.initializeGyro(hardwareMap, telemetry);
//        this.driveTrain.initializeMotors(hardwareMap, telemetry);

        this.driveTrain.initializeDriveMotors(hardwareMap);
        this.driveTrain.getHeading();
        this.driveTrain.reset_angle();

        boolean armServoOpen = false;
        boolean dpadOn = false;
        driveTrain.runArmServo(true);

        waitForStart();
        armThread(driveTrain);


        while (opModeIsActive()) {

            double crabValue = 0.0;
            double moveValue = 0.0;
            double turnValue = 0.0;
            double maxPower;
 
            if (gamepad1.left_bumper) {

                maxPower = 0.6;

                crabValue = gamepad1.left_stick_x / 2;
                moveValue = gamepad1.left_stick_y / 2;
                turnValue = gamepad1.right_stick_x / 2;
//TODO
                this.driveTrain.drive(crabValue, moveValue, turnValue, maxPower);
                idle();


            } else {
                maxPower = 0.8;

//                crabValue = -gamepad1.left_stick_x * 1.5;
//                moveValue = -gamepad1.left_stick_y * 1.5;
                crabValue = gamepad1.left_stick_x * 2;
                moveValue = gamepad1.left_stick_y * 2;
                turnValue = gamepad1.right_stick_x;

                this.driveTrain.drive(crabValue, moveValue, turnValue, maxPower);
                idle();
            }





//            if(crabValue > maxPower){
//                crabValue = maxPower;
//            }
//            if(moveValue > maxPower){
//                moveValue = maxPower;
//            }
//            if(turnValue > maxPower){
//                turnValue = maxPower;
//            }

//            this.driveTrain.drive(crabValue, moveValue, turnValue, maxPower);
//            idle();


            double simplePower = 0.5;
            double power = 0.3;
//            if (gamepad1.dpad_up) { //Forward
////                this.driveTrain.simpleF(simplePower);
//            }
//            if (gamepad1.dpad_left) { //Left
//                this.driveTrain.simpleL(simplePower);
//            }
//            if (gamepad1.dpad_down) { //Back
//                this.driveTrain.simpleB(simplePower);
//            }
//            if (gamepad1.dpad_right) { //Right
//                this.driveTrain.simpleR(simplePower);
//            }


            if (gamepad2.left_trigger == 0 && gamepad2.right_trigger == 0) {
                this.driveTrain.linearStop();
            }

            if (gamepad2.right_trigger > 0) {

                if (driveTrain.linearColorTop.blue() > 1000 && gamepad2.right_trigger > 0) {
                    driveTrain.linearStop();
                }
                else
                {
                    power = -gamepad2.right_trigger;
                    if (power > 0.4) {
                        power = 0.4;
                    }
                    driveTrain.linear(power / 4);
                }
            }

            if (gamepad2.left_trigger > 0) {
                if (driveTrain.linearColorDown.blue() > 400 && gamepad2.left_trigger > 0) {
                    driveTrain.linearStop();
                }
                else
                {
                    power = gamepad2.left_trigger;
                    if (power > 0.4) {
                        power = 0.4;
                    }
                    driveTrain.linear(power / 4);
                }
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

            if (gamepad2.x) {
                this.driveTrain.runFly();
            }
            if (gamepad2.b) {
                this.driveTrain.runFlyRed();
            }
            if (!gamepad2.x && !gamepad2.b){
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
//                this.driveTrain.autoCrab(100, 0.5, true);
                this.driveTrain.stopNow();
            }


        }

    }
    public void armThread(final FrenzyDriveTrain driveTrain) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean armServoClosed = false;
                while (opModeIsActive()) {
                    if (gamepad1.right_bumper || gamepad2.right_bumper) {
                        driveTrain.runArmServo(false);
                        sleep(20);
//                        sleep(250);
//                        if (armServoOpen) {
//                            armServoOpen = false;
//                        } else {
//                            armServoOpen = true;
//                        }
                    } else {
                        sleep(20);
                        driveTrain.runArmServo(true);
//                        sleep(250);
                    }
                }
            }
        });
        t1.start();
    }
    }


