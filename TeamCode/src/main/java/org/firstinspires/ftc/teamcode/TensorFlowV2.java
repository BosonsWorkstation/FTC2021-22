package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "TensorFlow Current", group = "Linear OpMode")
public class TensorFlowV2 extends LinearOpMode {

    private FrenzyDriveTrain driveTrain;
    private static final FrenzyDriveTrain.DirectionEnum direction = FrenzyDriveTrain.DirectionEnum.WEST;

    private static final String TFOD_MODEL_ASSET = "Marker.tflite";
    private static final String[] LABELS = {
            "Element"
    };

    private static final String VUFORIA_KEY =
            "AQn/ijj/////AAABmWP8/S4n903JiXWhq0vU/XEQsAo/SC7gZ62b1epD9wcoyBv+EVXle7a3LL4WjrqnhSsUo7WiOadSIa9yfNG++MVRtkt7SFSNAoQag5qjWv7stqhd+4spyGTpuuWNfZ6tbnJrOgGZ5HQ/PLdInVeXkIg9zDrBOIuVBR0i/9iZZM+64q/WKkw6T3aVh+xCtt0ulV5N6xiWsikROeU6j/98AZ3Iigj05uveC4/vf05YHT6hH4/0a0viCfoYBpAx+z5+fnuVsKrWBzQk0Jx4jmjh1dpaaYjGinbjSjBWKq+C75idjOZymZhE1smMLgVAIEWgJPS5R8KY+eGnXufpbGMQVwkRtSqKenEo3lN8YSNq3vVg";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    int level = 0;

    @Override
    public void runOpMode() {
        initVuforia();
        initTfod();

        this.driveTrain = new FrenzyDriveTrain(this.hardwareMap, this.telemetry, direction);

        this.driveTrain.initializeDriveMotors(hardwareMap);
        this.driveTrain.getHeading();
        this.driveTrain.reset_angle();

        this.driveTrain.runArmServo(true);


        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
//            tfod.setZoom(2.5, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
//            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
//                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            i++;
                        }
                        telemetry.update();

                        boolean firstPos = false;
                        boolean secondPos = false;
                        boolean thirdPos = false;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getRight() < 200) {
                                firstPos = true;
                            }
                            if (recognition.getRight() > 200 && recognition.getRight() < 400) {
                                secondPos = true;
                            }
                            if (recognition.getRight() > 500) {
                                thirdPos = true;
                            }
                        }

                        telemetry.addData("first pos", firstPos);
                        telemetry.addData("second pos", secondPos);
                        telemetry.addData("third pos", thirdPos);
                        telemetry.update();

                        this.driveTrain.autoMove(500,.5,true);
                        sleep(500);

                        this.driveTrain.autoCrab(1400, 0.5, true);
                        sleep(500);

                        this.driveTrain.runFly();
                        sleep(1500);
                        this.driveTrain.flyStop();
                        sleep(300);

                        this.driveTrain.flyFast();
                        sleep(800);
                        this.driveTrain.flyStop();

                        this.driveTrain.autoCrab(-3300, 0.5, true);
                        sleep(500);

                        this.driveTrain.autoRotate(-200, 0.8, false);
                        sleep(500);


                        if(firstPos){
                            this.driveTrain.autoMove(1100, 0.5, true);

                            this.driveTrain.autoLinearUp(0.2, 700);

                            level = 1;
//                            sleep(1000);
                        }
                        if(secondPos){
                            this.driveTrain.autoMove(1425, 0.5, true);

                            this.driveTrain.autoLinearUp(0.2, 1200);

                            level = 2;
//                            sleep(1500);
                        }
                        if(thirdPos){
                            this.driveTrain.autoMove(1400, 0.5, true);

                            this.driveTrain.autoLinearUp(0.2, 2200);

                            level = 3;
//                            sleep(2000);
                        }
                        if(!thirdPos && !secondPos & !firstPos){
                            this.driveTrain.autoMove(1400, 0.5, true);
                            this.driveTrain.autoLinearUp(0.2, 2000);
                        }


                        this.driveTrain.linearStop();

                        this.driveTrain.runArmServo(false);
                        sleep(300);

                        this.driveTrain.runArmServo(true);
                        sleep(200);

                        if (level == 3 || level == 2){
                            this.driveTrain.autoMove(-450, 0.5, true);
                            sleep(500);
                        }
                        if (level == 1){
                            this.driveTrain.autoMove(-100, 0.5, true);
                        }


//                        this.driveTrain.autoCrab(-1200, 0.5, true);
//                        sleep(500);

                        this.driveTrain.autoCrab(-5000, 0.8, false);
                        sleep(500);

                        this.driveTrain.autoMove(-700, 0.8, true);
                        sleep(500);

                        this.driveTrain.autoRotate(-1900, 0.3, false);
                        sleep(500);


//                        if (level == 1){
//                            this.driveTrain.autoLinearDown(0.2, 200);
//                        }
//                        if (level == 2){
//                            this.driveTrain.autoLinearDown(0.2, 200);
//                        }
//                        if (level == 3){
//                            this.driveTrain.autoLinearDown(0.2, 800);
//                        }

                        this.driveTrain.autoLinearDown(0.2);

                        this.driveTrain.armDown();
                        sleep(1980);
                        this.driveTrain.armStop();

                        this.sleep(200);
                        this.driveTrain.linearStop();

                    }
                }
            }
        }
//    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
