package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "WackBotAutonomous", group = "Linear Opmode")
public class WackBotAutonomous extends LinearOpMode {

    private WackBot driveTrain;
    private static final WackBot.DirectionEnum direction = WackBot.DirectionEnum.WEST;

    @Override
    public void runOpMode() throws InterruptedException {
        this.driveTrain = new WackBot(this.hardwareMap, this.telemetry, direction);

        this.driveTrain.initializeDriveMotors(hardwareMap);
        //this.driveTrain.getHeading();
        //this.driveTrain.reset_angle();

       // this.driveTrain.runArmServo(false);

        waitForStart();

        this.driveTrain.autoMove(500,.5,true);
        sleep(500);

        /*this.driveTrain.autoCrab(1350, 0.5, true);
        sleep(500);

        this.driveTrain.runFly();
        sleep(3000);
        this.driveTrain.flyStop();

        this.driveTrain.autoCrab(-3500, 0.5, true);
        sleep(500);

        this.driveTrain.autoRotate(-200, 0.8, false);
        sleep(500);

        this.driveTrain.autoMove(400, 0.5, false);

        this.driveTrain.autoLinearUp(0.2, 2000);

        this.driveTrain.runArmServo(false);

        this.driveTrain.autoCrab(-1000, 0.5, true);
        sleep(500);

        this.driveTrain.autoMove(1000, 0.5, true);
        sleep(500);

        this.driveTrain.autoCrab(-3000, 0.8, false);
*/



    }

}
