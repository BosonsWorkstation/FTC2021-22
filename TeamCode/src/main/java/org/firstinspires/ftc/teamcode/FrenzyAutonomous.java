package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Frenzy Autonomous", group = "Linear Opmode")
public class FrenzyAutonomous extends LinearOpMode {

    private FrenzyDriveTrain driveTrain;
    private static final FrenzyDriveTrain.DirectionEnum direction = FrenzyDriveTrain.DirectionEnum.SOUTH;

    @Override
    public void runOpMode() throws InterruptedException {
        this.driveTrain = new FrenzyDriveTrain(this.hardwareMap, this.telemetry, direction);

        this.driveTrain.initializeDriveMotors(hardwareMap);
        this.driveTrain.getHeading();
        this.driveTrain.reset_angle();

        waitForStart();

        this.driveTrain.autoCrab(1000,.5,true);
        sleep(100);

        this.driveTrain.linearAuto(0.3);
        sleep(1000);


    }

}
