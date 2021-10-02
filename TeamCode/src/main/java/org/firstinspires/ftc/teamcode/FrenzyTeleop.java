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


        waitForStart();
        while (opModeIsActive()) {

            double crabValue = 0.0;
            double moveValue = 0.0;
            double turnValue = 0.0;
            double maxPower = 0.8;


            if(gamepad1.left_bumper){
                maxPower = 0.4;

                crabValue = -gamepad1.left_stick_x / 1.5;
                moveValue = -gamepad1.left_stick_y / 1.5;
                turnValue = -gamepad1.right_stick_x / 4;
            }
            else{
                maxPower = 0.8;

                crabValue = -gamepad1.left_stick_x * 1.5;
                moveValue = -gamepad1.left_stick_y * 1.5;
                if(Math.abs(gamepad1.left_stick_x) > 0.1 && Math.abs(gamepad1.left_stick_y) > 0.1){
                    turnValue = gamepad1.right_stick_x;
                }
                else{
                    turnValue = gamepad1.right_stick_x * 2;
                }

            }

            this.driveTrain.drive(crabValue, moveValue, turnValue, maxPower);
            idle();

            if(Math.abs(crabValue) < 0.1 && Math.abs(moveValue) < 0.1 && Math.abs(turnValue) < 0.1){
            this.driveTrain.stopNow();
            }

            double simplePower = 0.3;
            double power = 0.3;
            if(gamepad1.dpad_up){ //Forward
                this.driveTrain.simpleF(simplePower);
            }
            if(gamepad1.dpad_left){ //Left
                this.driveTrain.simpleL(simplePower);
            }
            if(gamepad1.dpad_down){ //Back
                this.driveTrain.simpleB(simplePower);
            }
            if(gamepad1.dpad_right){ //Right
                this.driveTrain.simpleR(simplePower);
            }
            }
            if(!gamepad1.dpad_up && !gamepad1.dpad_left && !gamepad1.dpad_down && !gamepad1.dpad_right){
                this.driveTrain.stopNow();
            }
                //Linear directions in case you want to do straight lines.
                if(gamepad1.dpad_right){
                    gamepad1.left_stick_x = (float) 0.5;
                }
                else if(gamepad1.dpad_left){
                    gamepad1.left_stick_x = (float) -0.5;
                }
                if(gamepad1.dpad_up){
                    gamepad1.left_stick_y = (float) -0.5;
                }
                else if(gamepad1.dpad_down){
                    gamepad1.left_stick_y = (float) 0.5;
                }

            if(gamepad1.a){
                this.driveTrain.reset_angle();
            }
            if(gamepad1.b){
                this.driveTrain.autoMove(100, 0.5, true);
            }

        }
    }


