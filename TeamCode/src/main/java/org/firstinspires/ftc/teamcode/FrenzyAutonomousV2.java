package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaFieldNavigation;
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaFieldNavigationWebcam;
import org.firstinspires.ftc.teamcode.FrenzyDriveTrain;
import org.firstinspires.ftc.teamcode.EasyOpenCV;
import org.firstinspires.ftc.teamcode.Vuforia_Identifier;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

@Autonomous(name = "FrenzyAuto CURRENT", group = "Linear Opmode")
public class FrenzyAutonomousV2<allAvg> extends LinearOpMode//extends EasyOpenCV
{

    private static final double DEFAULT_POWER = 0.4;
    protected FrenzyDriveTrain driveTrain;
    protected Vuforia_Identifier vuforiaDetect;
    OpenCvInternalCamera phoneCam;

    public int[] threeAvg;


    boolean markerPositionFirst = false;
    boolean markerPositionSecond = false;
    boolean markerPositionThird = false;

    //60, 220
    double CAMERA_X_POSITION = 60;
    public double CAMERA_Y_POSITION = 240;

    private static final double CORRECTION_FACTOR = 0.5;

    SkystoneDeterminationPipeline pipeline = null;

    ConceptVuforiaFieldNavigationWebcam pictureDetect;

    static final int SLEEP_TIME = 10;


    protected void createPipeline(double xPosition, double yPosition){
        pipeline = new SkystoneDeterminationPipeline(xPosition, yPosition);
    }

    protected void initOpMode(double xPosition, double yPosition){

        FrenzyDriveTrain driveTrain;
        FrenzyDriveTrain.DirectionEnum direction = FrenzyDriveTrain.DirectionEnum.SOUTH;
        this.createPipeline(xPosition, yPosition);
        this.driveTrain = new FrenzyDriveTrain(this.hardwareMap, this.telemetry, direction);
        this.driveTrain.initialize(hardwareMap, telemetry);
        this.vuforiaDetect = new Vuforia_Identifier();
        this.vuforiaDetect.initVuforia();
        this.pictureDetect = new ConceptVuforiaFieldNavigationWebcam();

        telemetry.setAutoClear(false);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
//        pipeline = new SkystoneDeterminationPipeline();
        phoneCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(864, 480, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    protected int getCorrectedDistance(int originalDistance){
        return (int) (originalDistance * CORRECTION_FACTOR);
    }


//    protected SkystoneDeterminationPipeline.MarkerPosition getMarkerPosition(){
//        SkystoneDeterminationPipeline.MarkerPosition markerPosition = null;
//
//        sleep(20);
//
//        if (pipeline.position == SkystoneDeterminationPipeline.MarkerPosition.THREE) {
//            markerPosition = SkystoneDeterminationPipeline.MarkerPosition.THREE;
//            boolean markerPositionThird = true;
//            telemetry.addData("4 Rings Detected!", markerPosition);
//            telemetry.update();
//        }
//
//        if (pipeline.position == SkystoneDeterminationPipeline.MarkerPosition.TWO) {
//            markerPosition = SkystoneDeterminationPipeline.MarkerPosition.TWO;
//            boolean markerPositionSecond = true;
//            telemetry.addData("1 Ring Detected!", markerPosition);
//            telemetry.update();
//        }
//
//        if (pipeline.position == SkystoneDeterminationPipeline.MarkerPosition.ONE) {
//            markerPosition = SkystoneDeterminationPipeline.MarkerPosition.ONE;
//            boolean markerPositionFirst = true;
//            telemetry.addData("0 Rings Detected!", markerPosition);
//            telemetry.update();
//        }
//        return markerPosition;
//    }

    @Override
    public void runOpMode()  {

        initOpMode(CAMERA_X_POSITION, CAMERA_Y_POSITION);

        waitForStart();

        runAutonomous();

        while(opModeIsActive()){
            sleep(100);
        }
    }

//    public void MarkerPositionNum(SkystoneDeterminationPipeline.MarkerPosition markerPosition){
//        switch (markerPosition){
//            case ONE:
//                markerPositionFirst = true;
//                break;
//            case TWO:
//                markerPositionSecond = true;
//                break;
//            case THREE:
//                markerPositionThird = true;
//                break;
//            default:
//                telemetry.addData(String.valueOf(markerPosition), "NO Rings Detected");
//
//        }
//    }


    protected void runAutonomous(){
        if (opModeIsActive())
        {
//            telemetry.addData("LETS GOOO", this.driveTrain.linearColor.blue());

            telemetry.setAutoClear(false);
//            SkystoneDeterminationPipeline.MarkerPosition markerPositionFirst = getMarkerPosition();


//            double markerOneVal = (int) Core.mean(region1_Cb).val[0];
            telemetry.addData("avg1", pipeline.avg1);
            telemetry.update();

            this.createPipeline(CAMERA_X_POSITION + 220, CAMERA_Y_POSITION);
//            SkystoneDeterminationPipeline.MarkerPosition markerPosition2 = getMarkerPosition();
//            double markerTwoVal = pipeline.avg1;
            telemetry.addData("avg1", threeAvg[0]);
            telemetry.addData("avg2", threeAvg[1]);
            telemetry.addData("avg3", threeAvg[2]);
            telemetry.update();
//
//            this.createPipeline(CAMERA_X_POSITION + 440, CAMERA_Y_POSITION);
//            SkystoneDeterminationPipeline.MarkerPosition markerPosition3 = getMarkerPosition();
//            double markerThreeVal = pipeline.avg1;
//            telemetry.addData("avg3", pipeline.avg1);
//            telemetry.update();
            //ALL  AUTONOMOUS MOVE CODE

//            MarkerPositionNum(getMarkerPosition());

//            if (markerTwoVal > markerOneVal && markerTwoVal > markerThreeVal){
//                this.driveTrain.autoMove(100, 0.3, false);
//            }
        }
    }

    protected double getDefaultPower(){
        return DEFAULT_POWER;

    }

//    protected void leaveBase(SkystoneDeterminationPipeline.RingPosition ringPosition){
//        this.autoOmni.move(getCorrectedDistance(100), 0.3);
//        sleep(SLEEP_TIME);
//        //Crab to avoid rings
//        this.autoOmni.crab(getCorrectedDistance(-450), this.getDefaultPower());
//        sleep(SLEEP_TIME);
//    }


    public int[] getThreeAvg() {
        return threeAvg;
    }

    public class SkystoneDeterminationPipeline extends OpenCvPipeline
    {

        /*
         * An enum to define the skystone position
         */
//        public enum MarkerPosition
//        {
//            ONE,
//            TWO,
//            THREE
//        }

        /*
         * Some color constants
         */
        final Scalar BLUE = new Scalar(0, 0, 255);
        final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */
//        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(195,50);
        private Point REGION1_TOPLEFT_ANCHOR_POINT = null;
        private Point REGION2_TOPLEFT_ANCHOR_POINT = null;

        //WE HAVE DOUBLED THIS FOR TESTING, THE ORIGINAL VALUES ARE 35 AND 25!!!!!!!!! -Shawn
        static final int REGION_WIDTH = 200;
        static final int REGION_HEIGHT = 200;

        final int markerTrue = 140;
        final int ZERO_RING_THRESHOLD = 128;

//        Point region1_pointA = new Point(
//                REGION1_TOPLEFT_ANCHOR_POINT.x,
//                REGION1_TOPLEFT_ANCHOR_POINT.y);
//        Point region1_pointB = new Point(
//                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
//                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        Point region1_pointA = null;
        Point region1_pointB = null;

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();

        int avg1;
        int i = 0;

        // Volatile since accessed by OpMode thread w/o synchronization
//        public volatile MarkerPosition position = MarkerPosition.ONE;
        public SkystoneDeterminationPipeline(double xPosition, double yPosition){

            super();
            this.REGION1_TOPLEFT_ANCHOR_POINT = new Point(xPosition, yPosition);
            this.region1_pointA = new Point(
                    REGION1_TOPLEFT_ANCHOR_POINT.x,
                    REGION1_TOPLEFT_ANCHOR_POINT.y);
            this.region1_pointB = new Point(
                    REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                    REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
            threeAvg[i] = avg1;

        }
        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat firstFrame)
        {
            inputToCb(firstFrame);

            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }


        @Override
        public Mat processFrame(Mat input)
        {




//            for (int i = 0; i > 2; i++) {
//                inputToCb(input);

                avg1 = (int) Core.mean(region1_Cb).val[0];

//                threeAvg[i] = avg1;
//            }


            i++;
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines


//            if(avg1 > markerTrue) {
//                position = MarkerPosition.THREE;
//            }


            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

            return input;
        }

        public int getAnalysis()
        {
            return avg1;
        }
    }

    protected void wobbleDropThread(final FrenzyDriveTrain driveTrain, final long time, final boolean up) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                telemetry.setAutoClear(false);
                while (opModeIsActive()) {

                    break;
                }
            }
        });
        t1.start();
    }

}